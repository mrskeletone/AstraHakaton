package com.example.astrahakaton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
    protected void onClickJSON(String path) throws IOException {
        Util.createFileForConvertor("src/main/java/jsonFiles"+"/JSON");

//        String [] command = {"bash","src/main/java/scrypt/conventor/json_convertor.sh",
//                "src/main/java/jsonFiles/JSON", "src/main/java/jsonFiles/file.json"};
//
//        Process process = Runtime.getRuntime().exec(command);
//
//        process.getInputStream().transferTo(System.out);
//        process.getErrorStream().transferTo(System.out);
        //Активация скрипта конвертации
    }
    protected void onClickCSV(String path) throws IOException {
        Util.createFileForConvertor(path+"/CSV");
        //Активация скрипта конвертации
        String [] command = {"bash","src/main/java/scrypt/conventor/csv_convertor.sh",
                "src/main/java/csvFiles/CSV", "src/main/java/csvFiles/file.csv"};

        Process process = Runtime.getRuntime().exec(command);

        process.getInputStream().transferTo(System.out);
        process.getErrorStream().transferTo(System.out);
        //

    }
}
