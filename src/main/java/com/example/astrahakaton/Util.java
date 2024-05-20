package com.example.astrahakaton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    private Util(){
    }
    public static String getType(String s){
        int flag = s.indexOf("[");
        if (flag < 0) {
            flag = s.indexOf(":");
        }
        return s.substring(0, flag);
    }
    public static Logs stringToLogs(String line){
        StringBuilder s = new StringBuilder();
        String[] split = line.split(" ");
        for (int j = 5; j < split.length; j++) {
            s.append(" ").append(split[j]);
        }
        String[] arrLine = line.split(" ");
        String subString = Util.getType(arrLine[4]);
        return  new Logs(split[0] + " " + split[1] + " " + split[2], split[3], subString, s.toString());
    }
  public static TableView<Logs> createLogsTable(ObservableList<Logs> logs){
      TableView<Logs> table=new TableView<>(logs);
      TableColumn<Logs, String> dateColumn = new TableColumn<>("Date");
      dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
      table.getColumns().add(dateColumn);
      TableColumn<Logs, String> userColumn = new TableColumn<>("User");
      userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
      table.getColumns().add(userColumn);
      TableColumn<Logs, String> typeColumn = new TableColumn<>("Type");
      typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
      table.getColumns().add(typeColumn);
      TableColumn<Logs, String> commentColumn = new TableColumn<>("Comment");
      commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
      table.getColumns().add(commentColumn);

      table.setPrefSize(1000,500);
      return table;

  }
   static long lineNumber(String path){
       long noOfLines = -1;

        try(LineNumberReader lineNumberReader =
                    new LineNumberReader(new FileReader(new File(path)))) {
            //Skip to last line
            lineNumberReader.skip(Long.MAX_VALUE);
            noOfLines = lineNumberReader.getLineNumber() ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noOfLines;
    }
    static Map<String,Long> allTypesLogs(){
        Map<String, Long> data = new HashMap<>();
        long alert = Util.lineNumber("src/main/java/logFiles/alertsLogs/alerts");
        long critical = Util.lineNumber("src/main/java/logFiles/criticalLogs/critical");
        long debug = Util.lineNumber("src/main/java/logFiles/debugLogs/debug");
        long emergency = Util.lineNumber("src/main/java/logFiles/emergencyLogs/emergency");
        long errors = Util.lineNumber("src/main/java/logFiles/errorLogs/errors");
        long info = Util.lineNumber("src/main/java/logFiles/infoLogs/info");
        long sum = alert + critical + debug + emergency + emergency + info;
        data.put("alert " + String.format("%.2f", (double) alert / (double) sum * 100) + "%", alert);
        data.put("critical " + String.format("%.2f", (double) critical / (double) sum * 100) + "%", critical);
        data.put("debug " + String.format("%.2f", (double) debug / (double) sum * 100) + "%", debug);
        data.put("emergency " + String.format("%.2f", (double) emergency / (double) sum * 100) + "%", emergency);
        data.put("errors " + String.format("%.2f", (double) errors / (double) sum * 100) + "%", errors);
        data.put("info " + String.format("%.2f", (double) info / (double) sum * 100) + "%", info);
        return data;
    }
}
