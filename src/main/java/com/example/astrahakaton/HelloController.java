package com.example.astrahakaton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
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
    private DatePicker datePicker;


    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
         {
            // активация скрипта
            System.out.println(datePicker.getEditor());
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("allLogs-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new HelloApplication().getPrimaryStage();
            stage.setScene(scene);
            StringBuilder s= new StringBuilder();
            int i=0;
            try(BufferedReader bufferedReader=new BufferedReader(new FileReader("src/main/java/logFiles/all_types"))){
                while (bufferedReader.ready()){
                    s.append(bufferedReader.readLine()).append("\n");
                    i++;
                    if(i>1000){
                        break;
                    }
                }
            }
            LogsController logsController=fxmlLoader.getController();
            logsController.setTextArea(String.valueOf(s));
        }

    }


}