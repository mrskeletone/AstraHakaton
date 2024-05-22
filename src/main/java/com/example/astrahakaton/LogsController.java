package com.example.astrahakaton;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LogsController {
    @FXML
    private PieChart allLogsPie;
    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane view;

    private static TableView<Logs> table;

    private static String currentPath;
    private static FXMLLoader currentFXMLLoader;

    private static final List<String> listFilter = new ArrayList<>();

    //Создает таблицу в ячейках с (0,1) до (5,4)
    public void getAllLogsView(TableView<Logs> logsTableView) {
        view.add(logsTableView, 0, 1, 5, 4);
    }

    public void getCharts(BarChart barChart, int i, int i1, int i2, int i3) {
        view.add(barChart, i, i1, i2, i3);
    }

    public ObservableList<Logs> getDataFromTable() {
        return table.getItems();
    }

    //Заполняет график полученными данными
    public void setPieData(Map<String, Long> data) {
        PieChart.Data[] pie = new PieChart.Data[data.size()];
        int i = 0;
        for (var entry :
                data.entrySet()) {
            pie[i] = new PieChart.Data(entry.getKey(), entry.getValue());
            i++;
        }
        allLogsPie.setData(FXCollections.observableArrayList(pie));
    }

    //Добавляет новую строку или чистит если получет null
    public void setTable(ObservableList<Logs> s) {
        if (s == null) {
            table.getItems().clear();
        } else {
            table.getItems().addAll(s);

        }
    }

    //Возвращает лист меню
    public ObservableList<Menu> getMenuBar() {
        return menuBar.getMenus();
    }

    //Создание и настройка меню "фильр по"
    //selectFilter событие при нажание кнопку фильтров
    //reset событие при нажание на кнопку сброса фильтров
    public void setMenuBar(Map<String, Long> data) {
        EventHandler<ActionEvent> selectFilter = e -> {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
                setTable(null);
                ObservableList<Logs> logs = FXCollections.observableArrayList();
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
                setTable(logs);
            } catch (IOException ignored) {

            }
        };
        EventHandler<ActionEvent> reset = e -> {
            StringBuilder s = new StringBuilder();
            setTable(null);
            listFilter.clear();
            ObservableList<Logs> logs = FXCollections.observableArrayList();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    logs.add(Util.stringToLogs(line));
                }
            } catch (IOException exception) {

            }
            setTable(logs);
            var menu = getMenuBar();
            Menu sel = menu.get(1);
            for (var i :
                    sel.getItems()) {
                if (i instanceof CheckMenuItem) {
                    ((CheckMenuItem) i).setSelected(false);
                }

            }
        };
        for (var i :
                data.keySet()) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(i);
            checkMenuItem.setOnAction(selectFilter);
            menuBar.getMenus().get(1).getItems().add(checkMenuItem);

        }
        MenuItem menuItem = new MenuItem("Сбросить фильтры");
        menuItem.setOnAction(reset);
        menuBar.getMenus().get(1).getItems().add(menuItem);
    }

    //Действие при нажатие кнопки назад
    @FXML
    protected void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
    }

    //Действие при выборе в меню alerts
    @FXML
    protected void onClickMenuAlerts() throws IOException {
        onClickMenu("alertsLogs-view.fxml", "src/main/java/logFiles/alertsLogs/alerts");
    }

    //Действие при выборе в меню allLogs
    @FXML
    protected void onClickMenuAllLogs() throws IOException {
        onClickMenu("allLogs-view.fxml", "src/main/java/logFiles/allTypesLogs/all_types");
    }

    //Действие при выборе в меню Critical
    @FXML
    protected void onClickMenuCrit() throws IOException {
        onClickMenu("critLogs-view.fxml", "src/main/java/logFiles/criticalLogs/critical");
    }

    //Действие при выборе в меню Debug
    @FXML
    protected void onClickMenuDebug() throws IOException {
        onClickMenu("debugLogs-view.fxml", "src/main/java/logFiles/debugLogs/debug");
    }

    //Действие при выборе в меню Emergency
    @FXML
    protected void onClickMenuEmerg() throws IOException {
        onClickMenu("emergLogs-view.fxml", "src/main/java/logFiles/emergencyLogs/emergency");
    }

    //Действие при выборе в меню Errors
    @FXML
    protected void onClickMenuErrors() throws IOException {
        onClickMenu("errorsLogs-view.fxml", "src/main/java/logFiles/errorLogs/errors");
    }

    //Действие при выборе в меню Info
    @FXML
    protected void onClickMenuInfo() throws IOException {
        onClickMenu("infoLogs-view.fxml", "src/main/java/logFiles/infoLogs/info");
    }

    //Действие при выборе в меню Notice
    @FXML
    protected void onClickMenuNotice() throws IOException {
        onClickMenu("noticeLogs-view.fxml", "src/main/java/logFiles/noticeLogs/notice");
    }

    //Действие при выборе в меню Warning
    @FXML
    protected void onClickMenuWarning() throws IOException {
        onClickMenu("warningLogs-view.fxml", "src/main/java/logFiles/warningLogs/warning");
    }

    //метод для переключение между типами логов с заполнием таблицы новыми данными и создание нового графика
    private void onClickMenu(String fxml, String path) throws IOException {
        listFilter.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        currentPath = path;
        currentFXMLLoader = fxmlLoader;
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
        int i = 0;
        ObservableList<Logs> logs = FXCollections.observableArrayList();
        Map<String, Long> data = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String subString = Util.getType(line.split(" ")[4]);
                logs.add(Util.stringToLogs(line));
                if (!fxml.equals("allLogs-view.fxml")) {
                    if (!data.containsKey(subString)) {
                        data.put(subString, 0L);
                    } else {
                        long l = data.get(subString);
                        l++;
                        data.put(subString, l);
                    }
                } else {
                    data = Util.allTypesLogs();
                }
            }
        }
        table = (Util.createLogsTable(logs));
        table.setId("table");
        LogsController logsController = fxmlLoader.getController();
        logsController.getAllLogsView(table);
        if (!fxml.equals("allLogs-view.fxml")) {
            logsController.setMenuBar(data);
        }
        logsController.setPieData(data);
    }

    //метод для кнопки обновления
    @FXML
    protected void onClickUpdate() throws IOException {
        String date = Util.getEndDate().toString() + " " + Util.getTime();
        String [] command = {"bash","src/main/java/scrypt/writer/log_updater.sh",
                date + " " + Util.getTime(),date};

        Util.saveTime(Util.processTime(LocalTime.now()));

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);
    }

    //метод для кнопки графики
    @FXML
    protected void onClickAnalysis() throws IOException {
        System.out.println("сработало");
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("analysis-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
        Util.createCharts("src/main/java/logFiles/allTypesLogs/all_types", fxmlLoader, 0, 1, 1, 2, "all_types");
        Util.createCharts("src/main/java/logFiles/alertsLogs/alerts", fxmlLoader, 2, 1, 3, 2, "alerts");
        Util.createCharts("src/main/java/logFiles/debugLogs/debug", fxmlLoader, 5, 1, 6, 2, "debug");
        Util.createCharts("src/main/java/logFiles/emergencyLogs/emergency", fxmlLoader, 11, 1, 12, 2, "emergency");
        Util.createCharts("src/main/java/logFiles/errorLogs/errors", fxmlLoader, 0, 6, 1, 6, "errors");
        Util.createCharts("src/main/java/logFiles/infoLogs/info", fxmlLoader, 2, 6, 3, 6, "info");
        Util.createCharts("src/main/java/logFiles/noticeLogs/notice", fxmlLoader, 5, 6, 6, 6, "notice");
        Util.createCharts("src/main/java/logFiles/warningLogs/warning", fxmlLoader, 11, 6, 12, 6, "warning");
    }

    @FXML
    protected void onClickJSON() throws IOException {
        Util.createFileForConvertor("src/main/java/jsonFiles/JSON");

        String [] command = {"bash","src/main/java/scrypt/conventor/json_convertor.sh",
                "src/main/java/jsonFiles/JSON", "src/main/java/jsonFiles/file.json"};

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);
        //Активация скрипта конвертации
    }
    @FXML
    protected void onClickCSV() throws IOException {
        Util.createFileForConvertor("src/main/java/csvFiles/CSV");
        //Активация скрипта конвертации
        String [] command = {"bash","src/main/java/scrypt/conventor/csv_convertor.sh",
                "src/main/java/csvFiles/CSV", "src/main/java/csvFiles/file.csv"};

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);
        //

    }
    public static FXMLLoader getCurrentFXMLLoader(){
        return currentFXMLLoader;
    }
}
