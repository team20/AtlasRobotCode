package org.usfirst.frc.team20.robot;
/**
 *
 * @author Jared "jarebear" Gentner <jargen3d@yahoo.com>
 */
public class Trapezoidal{

    private double[] table;
    private boolean tabular;
    private double deltaTime;
    private boolean constantsDirty;

    private double acceleration, deceleration;
    private double distance;
    private double maxDeltaPosition;

    //amount of time for each operation, NOT start time
    //thus start times are cumulative
    private double tAccelerating, tConstant, tDecelerating;
    
    private Trapezoidal(){}

    /**
     * Get a new trapezoidal instance.
     * @return a new trapezoidal
     */
    public static Trapezoidal create(){
        return new Trapezoidal();
    }
    
    /**
     * Get a copy of another trapezoidal instance.
     * @param trapezoidal the trapezoidal to copy
     * @return a copy of the given trapezoidal
     */
    public static Trapezoidal copyOf(Trapezoidal trapezoidal){
        Trapezoidal copy = new Trapezoidal();
        copy.acceleration = trapezoidal.acceleration;
        copy.deceleration = trapezoidal.deceleration;
        copy.distance = trapezoidal.distance;
        copy.maxDeltaPosition = trapezoidal.maxDeltaPosition;
        copy.table = trapezoidal.table;
        copy.tabular = trapezoidal.tabular;
        copy.deltaTime = trapezoidal.deltaTime;
        copy.calculateConstants();
        return copy;
    }

    /**
     * Set the acceleration and return the object.
     * @param acceleration the acceleration
     * @return the object
     */
    public Trapezoidal withAcceleration(double acceleration){
        this.acceleration = acceleration;
        this.constantsDirty = true;
        return this;
    }

    /**
     * Set the deceleration and return the object.
     * @param deceleration the deceleration
     * @return the object
     */
    public Trapezoidal withDeceleration(double deceleration){
        this.deceleration = deceleration;
        this.constantsDirty = true;
        return this;
    }

    /**
     * Set the maximum distance between setpoints and return the object.  This
     * is equivalent to velocity.
     * @param maxDeltaPosition the maximum setpoint distance
     * @return the object
     */
    public Trapezoidal withMaxSetPointDistance(double maxDeltaPosition){
        this.maxDeltaPosition = maxDeltaPosition;
        this.constantsDirty = true;
        return this;
    }

    /**
     * Set the total distance desired and return the object.
     * @param distance the distance
     * @return the object
     */
    public Trapezoidal withDistance(double distance){
        this.distance = distance;
        this.constantsDirty = true;
        return this;
    }

    /**
     * Set the desired time scale and return the object.  Calling this method will 
     * activate tabular mode and generate a lookup table.
     * @param deltaTime the change in time
     * @return the object
     */
    public Trapezoidal withTimeDelta(double deltaTime){
        this.deltaTime = deltaTime;
        this.tabular = true;
        sanitize();
        return this;
    }

    /**
     * Get the acceleration.
     * @return the acceleration
     */
    public double getAcceleration(){
        return this.acceleration;
    }

    /**
     * Get the deceleration.
     * @return the deceleration
     */
    public double getDeceleration(){
        return this.deceleration;
    }

    /**
     * Get the total distance.
     * @return the total distance
     */
    public double getDistance(){
        return this.distance;
    }

    /**
     * Get the maximum distance between setpoints.  This is equivalent to velocity.
     * @return the maximum distance between setpoints
     */
    public double getMaxSetPointDistance(){
        return this.maxDeltaPosition;
    }

    /**
     * Get the change in time being used for this object's lookup table.
     * @return the change in time
     */
    public double getTimeDelta(){
        return this.deltaTime;
    }

    /**
     * Get the time for the current operation.
     * @return the time.
     */
    public double getTime(){
        sanitize();
        return this.getRawTime();
    }

    /**
     * Get the number of entries in this object's lookup table.
     * @return the size of the lookup table
     */
    public int getTableSize(){
        if(table == null)
            return 0;
        return this.table.length;
    }

    /**
     * Ask if this object is using a table to return setpoints or not.
     * @return whether this object is using a table
     */
    public boolean isTabular(){
        return this.tabular;
    }

    /**
     * Get the velocity at some time.
     * @param time the time
     * @return the velocity
     */
    public double getVelocity(double time){
        if(time <= this.tAccelerating)
            return this.acceleration * time;
        else if(time <= this.tConstant + this.tAccelerating)
            return this.maxDeltaPosition;
        else if(time <= this.tConstant + this.tAccelerating + this.tDecelerating)
        	return this.maxDeltaPosition - this.deceleration * (time - this.tAccelerating - this.tConstant);
        else
        	return 0;
    }

    /**
     * Get the position at some time.
     * @param time the time
     * @return the position
     */
    public double getPositionByTime(double time){
        this.sanitize();
        if(this.tabular){
            int index = (int)(time / this.deltaTime + .5);
            if(index >= this.table.length)
                return 0.;
            return this.table[index];
        }
        return calculateSetPoint(time);
    }

    /**
     * Get the position at some index.  Note that this style lookup is intended
     * for use with a table and calls to it in on-the-fly mode will attempt to
     * coerce the given index into a time.
     * @param index the index
     * @return the position
     */
    public double getPositionByIndex(int index){
        this.sanitize();
        if(this.tabular){
            return table[index];
        }
        return calculateSetPoint(index * this.deltaTime);
    }

    /**
     * Creates a table to be used if this object is in tabular mode.
     */
    public void createTable(){
        double totalTime = getTime();
        this.table = new double[(int)(totalTime / this.deltaTime) + 1];
        for(int i = 0; i < this.table.length; ++i){
            //use calculateSetPoint because it doesn't check to see if we are in tabular mode
            this.table[i] = calculateSetPoint(this.deltaTime * i);
        }
    }

    /**
     * Put this object into on-the-fly mode.
     */
    public void destroyTable(){
        this.tabular = false;
        this.table = null;
    }

    private double calculateSetPoint(double time){
    	time /= 1000;
        if(time < this.tAccelerating){
            return .5 * this.acceleration * time * time;
        }else if(time < this.tConstant + this.tAccelerating){
            return .5 * this.tAccelerating * this.maxDeltaPosition
                + (time - this.tAccelerating) * this.maxDeltaPosition;
        }else if(time < this.tConstant + this.tAccelerating + this.tDecelerating){
            double decelTime = time - this.tAccelerating - this.tConstant;
            return .5 * this.tAccelerating * this.maxDeltaPosition
                + this.tConstant * this.maxDeltaPosition
                + this.maxDeltaPosition * decelTime - .5 * this.deceleration * decelTime * decelTime;
        }else{
        	return distance;
        }
    }

    private double getRawTime(){
        return this.tAccelerating + this.tDecelerating + this.tConstant;
    }

    private void sanitize(){
        if(this.constantsDirty){
            this.calculateConstants();
            this.constantsDirty = false;
            if(this.tabular){
                this.createTable();
            }
        }
    }

    private void calculateConstants(){
        this.tAccelerating = this.maxDeltaPosition / this.acceleration;
        this.tDecelerating = this.maxDeltaPosition / this.deceleration;
        this.tConstant = (this.distance
            - .5 * this.acceleration * this.tAccelerating * this.tAccelerating
            - .5 * this.deceleration * this.tDecelerating * this.tDecelerating)
            / this.maxDeltaPosition;
        if(this.tConstant <= 0){
            double total = this.getRawTime();
            this.tConstant = 0;
            this.tDecelerating = Math.pow((2 * this.distance * this.acceleration)/
                (this.deceleration * this.deceleration + this.deceleration
                * this.acceleration), .5);
            this.tAccelerating = (this.tDecelerating * this.deceleration) / this.acceleration;
            this.maxDeltaPosition = this.tAccelerating * this.acceleration;
        }
    }
}
