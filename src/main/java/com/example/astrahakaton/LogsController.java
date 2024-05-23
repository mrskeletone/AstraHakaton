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
    private static boolean flagFiles;
    private static TableView<Logs> table;

    private static String currentPath;
    private static FXMLLoader currentFXMLLoader;

    private static final List<String> listFilter = new ArrayList<>();
    private static final List<String> listUsers = new ArrayList<>();

    public static boolean getFlagFiles() {
        return flagFiles;
    }

    public static void setNewTable(TableView<Logs> tables) {
        table = tables;
    }

    public static void setCurrentPath(String path) {
        currentPath = path;
    }

    public static void setCurrentFXMLLoader(FXMLLoader fxmlLoader) {
        currentFXMLLoader = fxmlLoader;
    }

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
    public static void setTable(ObservableList<Logs> s) {
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
            Util.selectFilter(currentPath, e, listFilter);
        };
        EventHandler<ActionEvent> reset = e -> {
            resetFilter(1);
            listFilter.clear();
            listUsers.clear();
        };
        if (data != null) {
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

    }

    private void resetFilter(int index) {

        StringBuilder s = new StringBuilder();
        setTable(null);
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
        Menu sel = menu.get(index);
        Menu re = menu.get(2);
        for (var i :
                sel.getItems()) {
            if (i instanceof CheckMenuItem) {
                ((CheckMenuItem) i).setSelected(false);
            }

        }
        for (var i :
                re.getItems()) {
            if (i instanceof CheckMenuItem) {
                ((CheckMenuItem) i).setSelected(false);
            }

        }
    }

    public void setUser(Set<String> name, int q) {
        EventHandler<ActionEvent> selectFilter = e -> {
            ObservableList<Logs> logs = FXCollections.observableArrayList();
            ObservableList<Logs> list = FXCollections.observableArrayList();
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(currentPath.substring(0, currentPath.lastIndexOf("/")) + "/buffer"))) {
                while (bufferedReader.ready())
                    list.add(Util.stringToLogs(bufferedReader.readLine()));
            } catch (IOException ignore) {

            }
            if (((CheckMenuItem) e.getSource()).isSelected()) {
                listUsers.add(((CheckMenuItem) e.getSource()).getText());
            } else {
                listUsers.remove(((CheckMenuItem) e.getSource()).getText());
            }
            if (!listUsers.isEmpty()) {
                for (var i :
                        list) {
                    if (listUsers.contains(i.getUser())) {
                        logs.add(i);
                    }
                }
            } else {
                logs = list;
            }
            table.getItems().setAll(logs);
        };
        EventHandler<ActionEvent> reset = e -> {
            listUsers.clear();
            var menu = getMenuBar();
            Menu re;
            if (currentPath.equals("src/main/java/logFiles/allTypesLogs/all_types")) {
                 re = menu.get(1);
            } else {
                 re = menu.get(2);
            }
            for (var i :
                    re.getItems()) {
                if (i instanceof CheckMenuItem) {
                    ((CheckMenuItem) i).setSelected(false);
                }

            }
            ObservableList<Logs> list = FXCollections.observableArrayList();
            try (BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(currentPath.substring(0, currentPath.lastIndexOf("/")) + "/buffer"))) {
                while (bufferedReader.ready())
                    list.add(Util.stringToLogs(bufferedReader.readLine()));
            } catch (IOException ignore) {

            }
            table.getItems().setAll(list);
        };

        for (var i :
                name) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(i);
            checkMenuItem.setOnAction(selectFilter);
            menuBar.getMenus().get(q).getItems().add(checkMenuItem);
        }
        MenuItem menuItem = new MenuItem("Сбросить фильтры");
        menuItem.setOnAction(reset);
        menuBar.getMenus().get(q).getItems().add(menuItem);
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
        Set<String> name = new HashSet<>();
        int i = 0;
        ObservableList<Logs> logs = FXCollections.observableArrayList();
        Map<String, Long> data = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                String subString = Util.getType(line.split(" ")[4]);
                name.add(line.split(" ")[3]);
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
        Util.createBuffer(currentPath, logs);
        LogsController logsController = fxmlLoader.getController();
        logsController.getAllLogsView(table);
        if (!fxml.equals("allLogs-view.fxml")) {
            logsController.setMenuBar(data);
            logsController.setUser(name, 2);
        } else {
            logsController.setUser(name, 1);
        }
        logsController.setPieData(data);
    }

    //метод для кнопки обновления
    @FXML
    protected void onClickUpdate() throws IOException {
        String date = Util.getEndDate().toString() + " " + Util.getTime();
        String[] command = {"bash", "src/main/java/scrypt/writer/log_updater.sh",
                date + " " + Util.getTime(), date};

        Util.saveTime(Util.processTime(LocalTime.now()));

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);

    }

    //метод для кнопки графики
    @FXML
    protected void onClickAnalysis() throws IOException {
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("path-view.fxml"));
        PathController.setFxmlLoader(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        PathController.setStage(stage);
        flagFiles = true;
        stage.show();
    }

    @FXML
    protected void onClickCSV() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("path-view.fxml"));
        PathController.setFxmlLoader(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        PathController.setStage(stage);
        flagFiles = false;
        stage.show();
    }

    public static FXMLLoader getCurrentFXMLLoader() {
        return currentFXMLLoader;
    }
}
