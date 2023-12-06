package ClientApplications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class RestaurantClientApp extends Application{

        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("restaurantClientApp.fxml"));
            Parent root = loader.load();
            RestaurantClientAppController controller = loader.getController();
            // Add an event handler to detect the application close action
            primaryStage.setOnCloseRequest(event -> {
                // Perform custom actions here before the application closes
                System.out.println("Application is closing. Performing custom actions...");
                controller.closeClient();
                primaryStage.close();
            });
            primaryStage.setTitle("Restaurant Client App");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            primaryStage.setResizable(false);
        }

        public static void main(String[] args) {
            launch(args);
        }
}
