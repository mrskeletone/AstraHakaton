package com.example.astrahakaton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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

    public static void selectFilter(String currentPath, ActionEvent e, List<String> listFilter) {
        ObservableList<Logs> logs = FXCollections.observableArrayList();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
            LogsController.setTable(null);
            if (((CheckMenuItem) e.getSource()).isSelected()) {
                listFilter.add(((CheckMenuItem) e.getSource()).getText());
            } else {
                listFilter.remove(((CheckMenuItem) e.getSource()).getText());
            }
            if (listFilter.isEmpty()) {
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    logs.add(Util.stringToLogs(line));
                }
            } else {
                while (bufferedReader.ready()) {
                    StringBuilder s = new StringBuilder();
                    String string = bufferedReader.readLine();
                    String[] arrLine = string.split(" ");
                    String subString = Util.getType(arrLine[4]);
                    if (listFilter.contains(subString)) {
                        for (int j = 5; j < arrLine.length; j++) {
                            s.append(" ").append(arrLine[j]);
                        }
                        logs.add(new Logs(arrLine[0] + " " + arrLine[1] + " " + arrLine[2], arrLine[3], subString, s.toString()));
                    }
                }
            }
            LogsController.setTable(logs);
        } catch (IOException ignored) {

        }
       createBuffer(currentPath,logs);
    }
    public static void createBuffer(String currentPath,ObservableList<Logs> logs){
        int index = currentPath.lastIndexOf("/");
        String path = currentPath.substring(0, index) + "/buffer";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (var i :
                    logs) {
                bufferedWriter.write(i.toString()+"\n");
            }
        } catch (IOException ignored) {}
    }
    public static LocalDate getEndDate() {
        return endDate;
    }

    public static void createFileForConvertor(String path) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            LogsController logsController = LogsController.getCurrentFXMLLoader().getController();
            ObservableList<Logs> logs = logsController.getDataFromTable();
            for (var log :
                    logs) {
                bufferedWriter.write(log.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static void createCharts(String path, FXMLLoader fxmlLoader, int i, int i1, int i2, int i3, String type) throws FileNotFoundException {
        Map<LocalDate, Long> data = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while (bufferedReader.ready()) {
                String s = bufferedReader.readLine();
                String parseString = getParseString(s);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate localDate = LocalDate.parse(parseString, formatter);

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
        XYChart.Series<String, Long> series = new XYChart.Series<>();


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
    public static String processTime(LocalTime curTime) {
        StringBuilder sb = new StringBuilder(curTime.toString());
        int i = sb.length() - 1;
        for (; true; ) {
            if (sb.toString().charAt(i) == '.') {
                sb.deleteCharAt(i);
                break;
            }
            sb.deleteCharAt(i);
            i--;
        }
        return sb.toString();
    }


    private Util() {
    }


    /*
     * Метод getType используется для форматирования источника
     *   ошибки в строке лога в вид, используемый при фильтрации
     *
     * @param1 - сроковое значени s строка лога
     *
     * на выходе получаем обработанную строку лога
     *
     * */
    public static String getType(String s) {
        int flag = s.indexOf("[");
        if (flag < 0) {
            flag = s.indexOf(":");
        }
        return s.substring(0, flag);
    }

    /*
     * Метод stringToLogs переводит строку с логов в формат класса Logs
     *   для дальнейшего использования объекта при отображении в таблице
     *
     * @param 1 - строковое значение лога line
     *
     * на выходе получаем объект класса Logs, преобразованный из строки лога
     * */
    public static Logs stringToLogs(String line) {
        StringBuilder s = new StringBuilder();
        String[] split = line.split(" ");
        for (int j = 5; j < split.length; j++) {
            s.append(" ").append(split[j]);
        }
        String[] arrLine = line.split(" ");
        String subString = Util.getType(arrLine[4]);
        return new Logs(split[0] + " " + split[1] + " " + split[2], split[3], subString, s.toString());
    }

    //Создает таблицу с колонками под логи и нужными размерами
    /*
     *
     *
     *
     * */
    public static TableView<Logs> createLogsTable(ObservableList<Logs> logs) {
        TableView<Logs> table = new TableView<>(logs);
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
        table.setRowFactory(tv -> new TableRow<Logs>() {
            @Override
            public void updateItem(Logs item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.getComment().contains("ACPI: EC: interrupt blocked")||item.getComment().contains("suspend")||item.getComment().contains("Suspend")
            ||item.getComment().contains("incorrect password attempts")||item.getComment().contains("Unknown kernel command line parameters")
                ||item.getComment().contains("Failed to enable")) {
                    setStyle("-fx-background-color: tomato;");
                } else {
                  setStyle("");
                }
            }
        });
        table.setPrefSize(1000, 500);
        return table;

    }

    //Выводит кол-во строк в файле
    static long lineNumber(String path) {
        long noOfLines = -1;

        try (LineNumberReader lineNumberReader =
                     new LineNumberReader(new FileReader(new File(path)))) {
            //Skip to last line
            lineNumberReader.skip(Long.MAX_VALUE);
            noOfLines = lineNumberReader.getLineNumber();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noOfLines;
    }

    //Возвращает Map с ключом лог и значением кол-во
    static Map<String, Long> allTypesLogs() {
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

