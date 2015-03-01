package org.usfirst.frc.team20.robot;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileWrite {
	public boolean saveFile() {
        File file = new File("c:\newFile.txt");
		boolean success;
        FileWriter fwriter;
        PrintWriter outputFile;
        try {
            fwriter = new FileWriter(file);
            outputFile = new PrintWriter(fwriter);
            outputFile.close();
            success = true;
        } catch (IOException e) {
            success = false;

        }
        return success;
    }
}
