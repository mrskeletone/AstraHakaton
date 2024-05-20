package com.example.astrahakaton;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogsController {

    @FXML
    private PieChart allLogsPie;
    @FXML
    private MenuBar menuBar;
    @FXML
    private GridPane view;

    private static TableView<Logs> table;

    private static String currentPath;

    private static List<String> listFilter = new ArrayList<>();

    //Создает таблицу в ячейках с (0,1) до (5,4)
    public void getAllLogsView(TableView<Logs> logsTableView) {
        view.add(logsTableView, 0, 1, 5, 4);
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
}
