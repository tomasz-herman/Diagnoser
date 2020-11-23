package pl.edu.pw.mini.msi.diagnoser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.edu.pw.mini.msi.diagnoser.controllers.MainController;

public class MainPanel extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setStageAndSetupListeners(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add("/main.css");
        primaryStage.show();
    }
}
