package com.company;
import java.awt.Desktop;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();
    Label label = new Label();
    ScrollPane scroller = new ScrollPane(label);
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Subtitles Extractor");

        final FileChooser fileChooser = new FileChooser();
        final DirectoryChooser dirChooser = new DirectoryChooser();
        TextField textField = new TextField ();
        final Button openButton = new Button("Wybierz plik");
        final Button openMultipleButton = new Button("Wybierz folder");
        final Button MKVExamine = new Button("Pokaz specyfikacje");
        final Button MKVExtract = new Button("Ekstraktuj");
        final ComboBox comboBox = new ComboBox();

        label.setWrapText(true);
        label.setStyle("-fx-font-family: \"Tahoma\"; -fx-font-size: 12; -fx-text-fill: black;");
        comboBox.getItems().addAll(
                0, 1, 2,3,4,5,6,7,8,9,10
        );


        openButton.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(final ActionEvent e) {
                        configureFileChooser(fileChooser);
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            textField.setText(file.getAbsolutePath());

                        }
                    }
                });

        MKVExtract.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String path = "C:/Program Files/MKVToolNix/mkvextract.exe";
                String command = "";
                int selTrack = (Integer)comboBox.getValue();
                String napis = "";
                File file = new File(textField.getText());
                if(file.isDirectory()){
                    ArrayList<String> files = new ArrayList<String>();
                    File[] fList = finder(textField.getText());
                    for(File fi : fList){
                        files.add(textField.getText() +"/" +  fi.getName());
                    }
                    for(String str : files){
                        System.out.println(str);
                        path = "C:/Program Files/MKVToolNix/mkvextract.exe";
                        napis = str.replaceAll(".mkv", ".srt");
                        command = path + " tracks \"" + str +  "\"" +" " + selTrack + ":" +"\"" + napis +"\"";
                        execute(command);
                    }
                }



                else{
                    command = path + " tracks \"" +  textField.getText() + "\"" +" " + selTrack + ":" +"\"" + textField.getText().replaceAll(".mkv", ".srt")+"\"";
                    execute(command);
                }
            }
        });

        MKVExamine.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String path = "C:/Program Files/MKVToolNix/mkvextract.exe";
                String command = "";
                path = "C:/Program Files/MKVToolNix/mkvinfo.exe";
                command = path + " \"" +  textField.getText() + "\"";
                execute(command);
            }
        });

        openMultipleButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        configureDirectoryChooser(dirChooser);
                        File file = dirChooser.showDialog(stage);
                        if (file != null) {
                            textField.setText(file.getAbsolutePath());
                        }
                    }
                });

        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 0);
        GridPane.setConstraints(openMultipleButton, 1, 0);
        GridPane.setConstraints(textField, 0, 1,5 ,1);
        GridPane.setConstraints(MKVExamine, 2, 0);
        GridPane.setConstraints(MKVExtract, 3, 0);
        GridPane.setConstraints(comboBox, 4, 0);
        GridPane.setConstraints(scroller, 0, 2,5 ,1);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, openMultipleButton,textField,MKVExamine,MKVExtract,scroller,comboBox);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.setHeight(600);
        stage.show();
    }

    public File[] finder(String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.endsWith(".mkv"); }
        } );
    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    public void setLabel(String text){
        label.setText(text);
    }

    private static void configureDirectoryChooser(final DirectoryChooser dirChooser){

        dirChooser.setTitle("Wybierz Folder");
        File defaultDirectory = new File("c:/");
        dirChooser.setInitialDirectory(defaultDirectory);
    }
    public void execute(String command){
        System.out.println(command);
        Commander myRunnable = new Commander(command, this);
        Thread t = new Thread(myRunnable);
        t.start();
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }
}