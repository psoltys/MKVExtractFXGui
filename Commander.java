package com.company;

import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Piotr on 2017-07-09.
 */
public class Commander implements Runnable {
    String command;
    Main main;

    Commander (String command, Main main){
        this.command = command;
        this.main = main;
    }
    @Override
    public void run() {
        try {
            Runtime runTime = Runtime.getRuntime();
            Process process = runTime.exec(command);
            InputStream inputStream = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream);
            InputStream errorStream = process.getErrorStream();
            InputStreamReader esr = new InputStreamReader(errorStream);

            int n1;
            char[] c1 = new char[1024];
            StringBuffer standardOutput = new StringBuffer();
            while ((n1 = isr.read(c1)) > 0) {
                standardOutput.append(c1, 0, n1);
            }
            System.out.println("Standard Output: " + standardOutput.toString());
            String output = standardOutput.toString();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    main.setLabel(output);
                }
            });

            int n2;
            char[] c2 = new char[1024];
            StringBuffer standardError = new StringBuffer();
            while ((n2 = esr.read(c2)) > 0) {
                standardError.append(c2, 0, n2);
            }
            System.out.println("Standard Error: " + standardError.toString());
            if(!standardError.toString().isEmpty()){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        main.setLabel(standardError.toString());
                    }
                });

            }
            //append(standardError.toString());
        } catch (IOException d) {
            d.printStackTrace();
        }
    }

}
