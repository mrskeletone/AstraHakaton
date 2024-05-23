package com.example.astrahakaton;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage primaryStage;

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    /*
    * Метод start запускает начальное окно, в котором пользователь выбирает дату
    *
    * */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("start-view.fxml"));
        primaryStage=stage;
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo/Logo.png")));
        primaryStage.setTitle("AnLogger");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}