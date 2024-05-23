package com.example.astrahakaton;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
* Класс HelloController отвечает за обработку входных данных
*   таких как начало и конец времени просмотра логов
* Методы класса: onHelloButtonClick
* */
public class HelloController {

    @FXML
    private DatePicker datePicker1;
    @FXML
    private DatePicker datePicker2;


    private static FXMLLoader fxmlLoader;

    /*
    * Метод onHelloButtonClick используется для описания событий
    *   которвые произойдут, когда пользователь введёт дату на
    *   первом экране
    *
    * В данном методе активируются два скрипта: log_writer_by_date.sh и
    *                                            writer_zero-six.sh
    * Команды активации скриптов помечены
    *
    * На выходе получаем таблицу для вывода логов (реализуется на базе класса LogsController)
    *   а так-же график по соотношению логов
    * */
    @FXML
    protected void onHelloButtonClick() throws IOException, InterruptedException {
        LocalDate begin = datePicker1.getValue();
        LocalDate end = datePicker2.getValue();
        Util.saveEndDate(end);

        if (begin != null&& end!=null) {

            String [] command = {"bash","src/main/java/scrypt/writer/log_writer_by_date.sh",
            begin.toString(),end.toString()};

            Util.saveTime(Util.processTime(LocalTime.now()));

            /* Активация скрипта log_writer_by_date.sh */
//            Process process = Runtime.getRuntime().exec(command);
//
//            process.getInputStream().transferTo(System.out);
//            process.getErrorStream().transferTo(System.out);


            fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("allLogs-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new HelloApplication().getPrimaryStage();
            stage.setScene(scene);
            ObservableList<Logs> logs = FXCollections.observableArrayList();
            int i = 0;
            Set<String> name=new HashSet<>();

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/logFiles/allTypesLogs/all_types"))) {
                while (bufferedReader.ready()) {
                    String string = bufferedReader.readLine();
                    name.add(string.split(" ")[3]);
                    logs.add(Util.stringToLogs(string));
                }
            }
            TableView<Logs> table = Util.createLogsTable(logs);
            table.setId("table");
            Thread thread = new Thread(() -> {

                String[] command1 = {"bash", "src/main/java/scrypt/writer/writer_zero-six.sh",
                        begin.toString(), end.toString()};
//                try {
//
//                    /* Активация скрипта writer_zero-six.sh */
//                    Process process2 = Runtime.getRuntime().exec(command1);
//
//                    process2.getInputStream().transferTo(System.out);
//                    process2.getErrorStream().transferTo(System.out);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }


                Map<String, Long> data = Util.allTypesLogs();
                Platform.runLater(() -> {
                    LogsController logsController = fxmlLoader.getController();
                    logsController.setPieData(data);

                });
            });
            thread.start();

            LogsController logsController = fxmlLoader.getController();
            logsController.getAllLogsView(table);
            LogsController.setCurrentFXMLLoader(fxmlLoader);
            LogsController.setNewTable(table);
            LogsController.setCurrentPath("src/main/java/logFiles/allTypesLogs/all_types");
            logsController.setUser(name,1);
            Util.createBuffer("src/main/java/logFiles/allTypesLogs/all_types",logs);
        }
    }

}
