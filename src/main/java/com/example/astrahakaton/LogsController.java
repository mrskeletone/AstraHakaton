package com.example.astrahakaton;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogsController {
    @FXML
    private TextArea textArea;


    public void setTextArea(String textArea){
        this.textArea.setText(textArea);
    }

    @FXML
    protected void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
    }
    @FXML
    protected void onClickMenuAlerts()throws IOException{
        onClickMenu("alertsLogs-view.fxml","src/main/java/logFiles/alerts");
    }
    @FXML
    protected void onClickMenuAllLogs()throws IOException{
       onClickMenu("allLogs-view.fxml","src/main/java/logFiles/all_types");
    }
    @FXML
    protected void onClickMenuCrit()throws IOException{
        onClickMenu("critLogs-view.fxml","src/main/java/logFiles/critical");
    }
    @FXML
    protected void onClickMenuDebug()throws IOException{
        onClickMenu("debugLogs-view.fxml","src/main/java/logFiles/debug");
    }
    @FXML
    protected void onClickMenuEmerg()throws IOException{
        onClickMenu("emergLogs-view.fxml","src/main/java/logFiles/emergency");
    }
    @FXML
    protected void onClickMenuErrors()throws IOException{
        onClickMenu("errorsLogs-view.fxml","src/main/java/logFiles/errors");
    }
    @FXML
    protected void onClickMenuInfo()throws IOException{
        onClickMenu("InfoLogs-view.fxml","src/main/java/logFiles/info");
    }
    @FXML
    protected void onClickMenuNotice()throws IOException{
        onClickMenu("noticeLogs-view.fxml","src/main/java/logFiles/notice");
    }
    @FXML
    protected void onClickMenuWarning()throws IOException{
        onClickMenu("warningLogs-view.fxml","src/main/java/logFiles/warning");
    }
    //метод для переключение между типами логов
    private void onClickMenu(String fxml,String path) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage=new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
        StringBuilder s= new StringBuilder();
        int i=0;
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(path))){
            while (bufferedReader.ready()){
                s.append(bufferedReader.readLine()).append("\n");
                i++;
                if(i>1000){
                    break;
                }
            }
        }
        LogsController logsController= fxmlLoader.getController();
        logsController.setTextArea(String.valueOf(s));
    }
}
