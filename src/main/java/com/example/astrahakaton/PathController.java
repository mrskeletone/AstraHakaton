package com.example.astrahakaton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


/*
* Класс PathController нужен для обработки пути
* при процессе конвертации логов в формат Json/CSV
*
* Методы: onClickSelect, onClickJson, onClickCSV
* */
public class PathController {
    @FXML
    private TextField TextView;
    private static FXMLLoader fxmlLoader;


    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        PathController.stage = stage;
    }

    public TextField getTextView() {
        return TextView;
    }

    public void setTextView(TextField textView) {
        this.TextView = textView;
    }

    public static FXMLLoader getFxmlLoader() {
        return fxmlLoader;
    }

    public static void setFxmlLoader(FXMLLoader fxmlLoader) {
        PathController.fxmlLoader = fxmlLoader;
    }

    /*
    * Метод onClickSelect обрабатывает нажатие на
    * элемент списка "конвертация" и в зависимости
    * от выбора формата конвертации, запускает
    * соответсвующие методы
    * */
    @FXML
    protected void onClickSelect()throws IOException{
        PathController pathController=fxmlLoader.getController();
        if(LogsController.getFlagFiles()){
            onClickJSON(pathController.getTextView().getText());
        }else{
            onClickCSV(pathController.getTextView().getText());
        }
        stage.close();
    }


    /*
    * Метод onClickJSON отвечает за конвертацию логов в
    * json формат
    *
    * @param 1 - строковое значение пути path сохранения файла
    *            в формате json
    *
    * После завершения выполнения метода получаем файл в формате json
    * сохраненный по выбранному пользователем пути
    * */
    protected void onClickJSON(String path) throws IOException {
        Util.createFileForConvertor("src/main/java/jsonFiles/JSON");

        String [] command = {"bash","src/main/java/scrypt/conventor/json_convertor.sh",
                "src/main/java/jsonFiles/JSON", path};

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);
    }

    /*
     * Метод onClickCSV отвечает за конвертацию логов в
     * csv формат
     *
     * @param 1 - строковое значение пути path сохранения файла
     *            в формате csv
     *
     * После завершения выполнения метода получаем файл в формате csv
     * сохраненный по выбранному пользователем пути
     * */
    protected void onClickCSV(String path) throws IOException {
        Util.createFileForConvertor("src/main/java/csvFiles/CSV");

        String [] command = {"bash","src/main/java/scrypt/conventor/csv_convertor.sh",
                "src/main/java/csvFiles/CSV", path};

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);


    }
}
