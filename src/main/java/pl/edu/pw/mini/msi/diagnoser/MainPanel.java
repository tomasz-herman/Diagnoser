package pl.edu.pw.mini.msi.diagnoser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainPanel extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add("/main.css");
        primaryStage.show();
    }
}