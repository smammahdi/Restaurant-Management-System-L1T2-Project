package ClientApplications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;


public class CustomerClientApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("customerClientApp.fxml"));
        Parent root = loader.load();
        CustomerClientAppController controller = loader.getController();
        // Add an event handler to detect the application close action
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // Perform custom actions here before the application closes
                System.out.println("Application is closing. Performing custom actions...");
                controller.closeClient();
                primaryStage.close();
            }
        });
        primaryStage.setTitle("Customer Client App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
