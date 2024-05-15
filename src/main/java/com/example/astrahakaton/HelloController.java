package com.example.astrahakaton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HelloController {

    @FXML
    private DatePicker datePicker1;
    @FXML
    private DatePicker datePicker2;


    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        LocalDate begin =datePicker1.getValue();
        LocalDate end=datePicker2.getValue();
        if(end==null)
            end=LocalDate.now();
        if(begin!=null){
            System.out.println(begin.toString());
            System.out.println(end.toString());

            // активация скрипта
            Runtime.getRuntime().exec(new String[]{"/bin/sh" ,"log_writer_by_date.sh", begin.toString(),end.toString(),"23:59:59"});
            //
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("allLogs-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage=new HelloApplication().getPrimaryStage();
            stage.setScene(scene);
            StringBuilder s= new StringBuilder();
            int i=0;
            try(BufferedReader bufferedReader=new BufferedReader(new FileReader("src/main/java/logFiles/all_types"))){
                while (bufferedReader.ready()){
                    s.append(bufferedReader.readLine()).append("\n");
                }
            }
            LogsController logsController=fxmlLoader.getController();
            logsController.setTextArea(String.valueOf(s));
        }

    }


}