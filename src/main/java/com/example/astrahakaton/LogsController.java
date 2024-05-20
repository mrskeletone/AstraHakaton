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


    public void getAllLogsView(TableView<Logs> logsTableView) {
        view.add(logsTableView, 0, 1, 5, 4);
    }

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

    public void setTable(ObservableList<Logs> s) {
        if (s == null) {
            table.getItems().clear();
        } else {
            table.getItems().addAll(s);

        }
    }

    public ObservableList<Menu> getMenuBar() {
        return menuBar.getMenus();
    }

    public void setMenuBar(Map<String, Long> data) {
        EventHandler<ActionEvent> selectFilter = e -> {
            System.out.println(currentPath);
            setTable(null);
            ObservableList<Logs> logs = FXCollections.observableArrayList();
            if (((CheckMenuItem) e.getSource()).isSelected()) {
                listFilter.add(((CheckMenuItem) e.getSource()).getText());
            } else {
                listFilter.remove(((CheckMenuItem) e.getSource()).getText());
            }
            if (listFilter.isEmpty()) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
                    while (bufferedReader.ready()) {
                        String line = bufferedReader.readLine();
                        logs.add(Util.stringToLogs(line));
                    }
                } catch (IOException ignored) {

                }
            } else {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
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
                } catch (IOException ignored) {

                }
            }
            setTable(logs);
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


    @FXML
    protected void onClickBackButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
    }

    @FXML
    protected void onClickMenuAlerts() throws IOException {
        onClickMenu("alertsLogs-view.fxml", "src/main/java/logFiles/alertsLogs/alerts");
    }

    @FXML
    protected void onClickMenuAllLogs() throws IOException {
        onClickMenu("allLogs-view.fxml", "src/main/java/logFiles/allTypesLogs/all_types");
    }

    @FXML
    protected void onClickMenuCrit() throws IOException {
        onClickMenu("critLogs-view.fxml", "src/main/java/logFiles/criticalLogs/critical");
    }

    @FXML
    protected void onClickMenuDebug() throws IOException {
        onClickMenu("debugLogs-view.fxml", "src/main/java/logFiles/debugLogs/debug");
    }

    @FXML
    protected void onClickMenuEmerg() throws IOException {
        onClickMenu("emergLogs-view.fxml", "src/main/java/logFiles/emergencyLogs/emergency");
    }

    @FXML
    protected void onClickMenuErrors() throws IOException {
        onClickMenu("errorsLogs-view.fxml", "src/main/java/logFiles/errorLogs/errors");
    }

    @FXML
    protected void onClickMenuInfo() throws IOException {
        onClickMenu("infoLogs-view.fxml", "src/main/java/logFiles/infoLogs/info");
    }

    @FXML
    protected void onClickMenuNotice() throws IOException {
        onClickMenu("noticeLogs-view.fxml", "src/main/java/logFiles/noticeLogs/notice");
    }

    @FXML
    protected void onClickMenuWarning() throws IOException {
        onClickMenu("warningLogs-view.fxml", "src/main/java/logFiles/warningLogs/warning");
    }

    //метод для переключение между типами логов
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
        table=(Util.createLogsTable(logs));
        table.setId("table");
        LogsController logsController = fxmlLoader.getController();
        logsController.getAllLogsView(table);
        if (!fxml.equals("allLogs-view.fxml")) {
            logsController.setMenuBar(data);
        }
        logsController.setPieData(data);
    }
}
