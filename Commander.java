package com.company;

import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Piotr on 2017-07-09.
 */
public class Commander {
    public void execute(String command, Main main) {
        if (command.contains("mkvextract")) {
            main.setLabel("Trwa wyodrebnianie");
            CommandExtract myRunnable = new CommandExtract(command, main);
            Thread t = new Thread(myRunnable);
            t.start();
        }
        else {
            CommandInfo myRunnable = new CommandInfo(command, main);
            Thread t = new Thread(myRunnable);
            t.start();
        }

    }
}
