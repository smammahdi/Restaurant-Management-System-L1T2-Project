package ServerApplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import NetworkUtilities.IPAddressProvider;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ServerAppController implements Initializable {

        public Button startServerCall;
        public Pane beforeServerStart;
        public TextField portNumber;

        @FXML
        public Pane afterServerStart;
        public Text portNumberDisplay;
        public Text IPAddressShow;

        public ListView connectedRestaurantList;

        public ListView connectedCustomerList;

        public ObservableList<String> connectedRestaurantListItems;
        public ObservableList<String> connectedCustomerListItems;


        public Button stopServerButton;
        public Button portNumCopyButton;


        private Server server;

        private boolean serverStatus=false;


        @FXML
        void startServer(ActionEvent event) {
                String port = portNumber.getText();
                portNumber.clear();
                System.out.println(port);
                beforeServerStart.setVisible(false);
                System.out.println("Server is running");
                try{
                        int portNumber = Integer.parseInt(port);
                        if(portNumber<0 || portNumber>65535) throw new Exception();
                }catch (Exception e){
                        System.out.println("Invalid port number");
                        // show a message in the fxml window that port number is invalid
                        // Create an Alert with an error message
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Port Number");
                        alert.setHeaderText(null);
                        alert.setContentText("The entered port number is invalid.");

                        // Show the Alert in a pop-up window
                        alert.showAndWait();

                        beforeServerStart.setVisible(true);
                        return;
                }
                connectedRestaurantListItems = FXCollections.observableArrayList();
                connectedCustomerListItems = FXCollections.observableArrayList();
                connectedRestaurantList.setItems(connectedRestaurantListItems);
                connectedCustomerList.setItems(connectedCustomerListItems);
                Server.connectedCustomerListItems = connectedCustomerListItems;
                Server.connectedRestaurantListItems = connectedRestaurantListItems;
                server = new Server(port);
                serverStatus=true;
                afterServerStart.setVisible(true);
                portNumberDisplay.setText("Port Number\n"+port);
                IPAddressShow.setText("IP Address\n"+ IPAddressProvider.getIP());

        }

        @Override
        public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1){
                connectedRestaurantList.setPlaceholder(new Label("No connected restaurants client"));
                connectedCustomerList.setPlaceholder(new Label("No connected customer client"));
                portNumCopyButton.setOnAction(this::copyToClipboard);
        }

        private void copyToClipboard(ActionEvent actionEvent) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(portNumberDisplay.getText());
                //remove the portnumber part
                String[] parts = portNumberDisplay.getText().split("\n");
                content.putString(parts[1]);
                clipboard.setContent(content);
        }

        @FXML
        void stopServer(ActionEvent event) {
                System.out.println("Server is stopped");
                serverStatus=false;
                afterServerStart.setVisible(false);
                beforeServerStart.setVisible(true);
                server.stopServer();
        }
        

}
