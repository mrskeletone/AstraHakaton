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
        onClickMenu("alertsLogs-view.fxml","src/main/java/logFiles/alertsLogs/alerts");
    }
    @FXML
    protected void onClickMenuAllLogs()throws IOException{
       onClickMenu("allLogs-view.fxml","src/main/java/logFiles/allTypesLogs/all_types");
    }
    @FXML
    protected void onClickMenuCrit()throws IOException{
        onClickMenu("critLogs-view.fxml","src/main/java/logFiles/criticalLogs/critical");
    }
    @FXML
    protected void onClickMenuDebug()throws IOException{
        onClickMenu("debugLogs-view.fxml","src/main/java/logFiles/debugLogs/debug");
    }
    @FXML
    protected void onClickMenuEmerg()throws IOException{
        onClickMenu("emergLogs-view.fxml","src/main/java/logFiles/emergencyLogs/emergency");
    }
    @FXML
    protected void onClickMenuErrors()throws IOException{
        onClickMenu("errorsLogs-view.fxml","src/main/java/logFiles/errorsLogs/errors");
    }
    @FXML
    protected void onClickMenuInfo()throws IOException{
        onClickMenu("infoLogs-view.fxml","src/main/java/logFiles/infoLogs/info");
    }
    @FXML
    protected void onClickMenuNotice()throws IOException{
        onClickMenu("noticeLogs-view.fxml","src/main/java/logFiles/noticeLogs/notice");
    }
    @FXML
    protected void onClickMenuWarning()throws IOException{
        onClickMenu("warningLogs-view.fxml","src/main/java/logFiles/warningLogs/warning");
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
            }
        }
        LogsController logsController= fxmlLoader.getController();
        logsController.setTextArea(String.valueOf(s));
    }
}
