package com.example.astrahakaton;

import javafx.application.Platform;
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

    private static FXMLLoader fxmlLoader;

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
             fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("allLogs-view.fxml"));
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
            Platform.runLater(() -> {
                //Сюда вписать добавление скрипта

                //
                LogsController logsController = fxmlLoader.getController();
                Map<String, Long> data = Util.allTypesLogs();

                logsController.setPieData(data);

            });

            LogsController logsController = fxmlLoader.getController();
            logsController.setTextArea(String.valueOf(s));
            // logsController.setPieData(data);
        }
    }

}
