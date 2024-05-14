package com.example.astrahakaton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;

    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        if (textField.getText() != null && !textField.getText().isEmpty()) {
            // активация скрипта

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("allLogs-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new HelloApplication().getPrimaryStage();
            stage.setScene(scene);
            StringBuilder s= new StringBuilder();
            try(BufferedReader bufferedReader=new BufferedReader(new FileReader("C:\\Users\\dever\\IdeaProjects\\AstraHakaton\\logs.txt"))){
                while (bufferedReader.ready()){
                    s.append(bufferedReader.readLine()).append("\n");
                }
            }
            textArea.setText(s.toString());
        }

    }
    @FXML
    protected void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
    }
    @FXML
    protected void onClick() throws IOException{
        StringBuilder s= new StringBuilder();
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader("C:\\Users\\dever\\IdeaProjects\\AstraHakaton\\logs.txt"))){
            while (bufferedReader.ready()){
                s.append(bufferedReader.readLine()).append("\n");
            }
        }
            textArea.setText(s.toString());
    }

}