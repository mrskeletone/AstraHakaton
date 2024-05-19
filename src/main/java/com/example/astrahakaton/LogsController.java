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
    private TextArea textArea;
    @FXML
    private PieChart allLogsPie;
    @FXML
    private MenuBar menuBar;

    private boolean flagBase = false;

    private static String currentPath;

    private static List<String> listFilter = new ArrayList<>();

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

    public String getTextArea() {
        return textArea.getText();
    }

    public ObservableList<Menu> getMenuBar() {
        return menuBar.getMenus();
    }

    public void setMenuBar(Map<String, Long> data) {
        EventHandler<ActionEvent> selectFilter = e -> {
            System.out.println(currentPath);
            setTextArea("");
            StringBuilder s = new StringBuilder();
            if (((CheckMenuItem) e.getSource()).isSelected()) {
                listFilter.add(((CheckMenuItem) e.getSource()).getText());
            } else {
                listFilter.remove(((CheckMenuItem) e.getSource()).getText());
            }
            if (listFilter.isEmpty()) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
                    while (bufferedReader.ready()) {
                        String string = bufferedReader.readLine();
                        s.append(string).append("\n");
                    }
                } catch (IOException ignored) {

                }
            } else {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
                    while (bufferedReader.ready()) {
                        String string = bufferedReader.readLine();
                        String[] arrLine=string.split(" ");
                        int flag = arrLine[4].indexOf("[");
                        if (flag < 0) {
                            flag = arrLine[4].indexOf(":");
                        }
                        String subString = arrLine[4].substring(0, flag);
                        if (listFilter.contains(subString)) {
                            s.append(string).append("\n");
                        }
                    }
                } catch (IOException ignored) {

                }
            }
            setTextArea(s.toString());
        };
        EventHandler<ActionEvent> reset = e -> {
            StringBuilder s = new StringBuilder();
            setTextArea("");
            listFilter.clear();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentPath))) {
                while (bufferedReader.ready()) {
                    String line = bufferedReader.readLine();
                    s.append(line).append("\n");

                }
            } catch (IOException exception) {

            }
            setTextArea(s.toString());
            flagBase = false;
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

    public void setTextArea(String textArea) {
        this.textArea.setText(textArea);
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        currentPath = path;
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new HelloApplication().getPrimaryStage();
        stage.setScene(scene);
        StringBuilder s = new StringBuilder();
        int i = 0;
        Map<String, Long> data = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                s.append(line).append("\n");
                if (!fxml.equals("allLogs-view.fxml")) {
                    String[] arrLine = line.split(" ");
                    int flag = arrLine[4].indexOf("[");
                    if (flag < 0) {
                        flag = arrLine[4].indexOf(":");
                    }
                    String subString = arrLine[4].substring(0, flag);
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

        LogsController logsController = fxmlLoader.getController();
        logsController.setTextArea(String.valueOf(s));
        if (!fxml.equals("allLogs-view.fxml")) {
            logsController.setMenuBar(data);
            logsController.setPieData(data);
        } else {
            logsController.setPieData(data);
        }
    }
}
