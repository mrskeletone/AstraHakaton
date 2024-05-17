package com.example.astrahakaton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class HelloController {

    @FXML
    private DatePicker datePicker1;
    @FXML
    private DatePicker datePicker2;


    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        LocalDate begin = datePicker1.getValue();
        LocalDate end = datePicker2.getValue();
        if (end == null)
            end = LocalDate.now();
        if (begin != null) {
            // активация скрипта
//            String [] command = {"bash","src/main/java/scrypt/writer/log_writer_by_date.sh",
//            begin.toString(),end.toString()};
//
//            Process process = Runtime.getRuntime().exec(command);
//
//            process.getInputStream().transferTo(System.out);
//            process.getErrorStream().transferTo(System.out);

            //
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("allLogs-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new HelloApplication().getPrimaryStage();
            stage.setScene(scene);
            StringBuilder s = new StringBuilder();
            int i = 0;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/logFiles/allTypesLogs/all_types"))) {
                while (bufferedReader.ready()) {
                    s.append(bufferedReader.readLine()).append("\n");
                }
            }
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

            LogsController logsController = fxmlLoader.getController();
            logsController.setTextArea(String.valueOf(s));
            logsController.setPieData(data);
        }
    }

}
