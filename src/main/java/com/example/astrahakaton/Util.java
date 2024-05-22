package com.example.astrahakaton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//Класс предназначен методов ускоряющих написание программы
public class Util {
    private static LocalDate endDate;
    private static String time;
    public static LocalDate getEndDate() {
        return endDate;
    }

    public static void saveEndDate(LocalDate endDate) {
        Util.endDate = endDate;
    }

    public static String getTime() {
        return time;
    }

    public static void saveTime(String time) {
        Util.time = time;
    }
    public static void createCharts(String path, FXMLLoader fxmlLoader,int i,int i1,int i2,int i3,String type) throws FileNotFoundException {
        Map<LocalDate, Long> data = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine();
                String parseString = getParseString(s);
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate localDate = LocalDate.parse(parseString,formatter);

                if (!data.containsKey(localDate)) {
                    data.put(localDate, 0L);
                } else {
                    long l = data.get(localDate);
                    l++;
                    data.put(localDate, l);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CategoryAxis newxaxis = new CategoryAxis();
        NumberAxis yaxis = new NumberAxis();
        newxaxis.setLabel("Date");
        yaxis.setLabel("Count");
        BarChart<String, Long> newbar = new BarChart(newxaxis, yaxis);
        newbar.setTitle(type);
        XYChart.Series<String , Long> series = new XYChart.Series<>();


        for (var t :
                data.entrySet()) {
            series.getData().add(new XYChart.Data<>(t.getKey().toString(), t.getValue()));

        }
        series.getData().sort((o1, o2) -> o1.getXValue().compareTo(o2.getXValue()));
        newbar.getData().add(series);
        LogsController logsController = fxmlLoader.getController();
        logsController.getCharts(newbar, i, i1, i2, i3);
    }

    private static String getParseString(String s) {
        String time = s.substring(0, 6);
        return switch (time.substring(0, 3)) {
            case "мая" -> time.substring(4, 6) + "-05-" + "2024";
            case "янв" -> time.substring(4, 6) + "-01-" + "2024";
            case "фев" -> time.substring(4, 6) + "-02-" + "2024";
            case "мар" -> time.substring(4, 6) + "-03-" + "2024";
            case "апр" -> time.substring(4, 6) + "-04-" + "2024";
            case "июн" -> time.substring(4, 6) + "-06-" + "2024";
            case "июл" -> time.substring(4, 6) + "-07-" + "2024";
            case "авг" -> time.substring(4, 6) + "-08-" + "2024";
            case "сен" -> time.substring(4, 6) + "-09-" + "2024";
            case "окт" -> time.substring(4, 6) + "-10-" + "2024";
            case "ноя" -> time.substring(4, 6) + "-11-" + "2024";
            case "дек" -> time.substring(4, 6) + "-12-" + "2024";
            default -> "";
        };
    }

    /* Метод processTime обрабатывает время, обрезая миллисекунды
    *  Используется для форматирования времени, которое в последствии
    *   используется для обновления логов
    *
    * @param1: curTime - объект класса LocalTime, который показывает время
    *   в формате hh:mm:ss.ms
    *
    * на выходе получаем строковое значение времени, в формате hh:mm:ss
    * */
    public static String processTime(LocalTime curTime){
         StringBuilder sb = new StringBuilder(curTime.toString());
         int i = sb.length()-1;
         for(;true;){
             if(sb.toString().charAt(i)=='.'){
                 sb.deleteCharAt(i);
                 break;
             }
             sb.deleteCharAt(i);
             i--;
         }
         return sb.toString();
    }



    private Util(){
    }
    // Возвращает тип лога без лишних символов
    public static String getType(String s){
        int flag = s.indexOf("[");
        if (flag < 0) {
            flag = s.indexOf(":");
        }
        return s.substring(0, flag);
    }
    //Переводит string  в класс Logs
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
    //Создает таблицу с колонками под логи и нужными размерами
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
    //Выводит кол-во строк в файле
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
    //Возвращает Map с ключом лог и значением кол-во
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

