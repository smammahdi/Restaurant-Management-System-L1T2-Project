package ServerApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("serverApp.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Server Application");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        //fix window size
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
