package ClientApplications;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import NetworkUtilities.NetworkUtil;

import java.util.List;
import java.util.ArrayList;

import RestaurantUtilities.Order;
import RestaurantUtilities.Restaurant;

public class RestaurantClientAppController {

    public Button startServerCall;

    public  NetworkUtil networkUtil;

    public String restaurantName;

    public Restaurant restaurant;

    public List<Order> orderList;

    public List<Order> orderHistoryList;

    public ImageView coverPic;
    public TextField restaurantNameEnter;
    public Pane loginDisplay;
    public TextField portNumber;
    public Pane restaurantInfoPane;
    public TextArea restaurantInfoShow;
    public Pane orderPane;
    public ListView<String> orderListShow;

    public volatile boolean threadStatus = true;




    public void closeClient(){
        System.out.println("Closing Restaurant Client App...");
        try {
            networkUtil.write("restaurant close: "+restaurantName);
            networkUtil.closeConnection();
        }

        catch (Exception e){
            System.out.println(e);
        }
        threadStatus = false;
        Platform.exit();
    }

    public void clientService(ActionEvent actionEvent) {
        String portNum = portNumber.getText();
        portNumber.clear();
        System.out.println(portNum);
        loginDisplay.setVisible(false);
        restaurantName = restaurantNameEnter.getText();
        int port;
        System.out.println("Server is running");
        try{
            port = Integer.parseInt(portNum);
            if(port<0 || port>65535) throw new Exception();
        }catch (Exception e){
            System.out.println("Invalid port number");
            // show a message in the fxml window that port number is invalid
            // Create an Alert with an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Port Number");
            alert.setContentText("Please enter a valid port number");
            alert.showAndWait();
            loginDisplay.setVisible(true);
            return;
        }
        //create network util
        try {
            networkUtil = new NetworkUtil("127.0.0.1", port);
            System.out.println("Connected to server");
            networkUtil.write("restaurant");
            //send restaurant name

            networkUtil.write(restaurantName);
            //read restaurant info
            if(networkUtil.read().equals("Restaurant name does not exist")){
                System.out.println("Restaurant name does not exist");
                // show a message in the fxml window that port number is invalid
                // Create an Alert with an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Restaurant name does not exist");
                alert.setContentText("Please enter a valid restaurant name");
                alert.showAndWait();
                loginDisplay.setVisible(true);
                return;
            }
            restaurant = (Restaurant) networkUtil.read();
        }catch (Exception e){
            System.out.println(e);
            System.out.println("Server not found");
            // show a message in the fxml window that port number is invalid
            // Create an Alert with an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Server not found");
            alert.setContentText("Please check if the server is running");
            alert.showAndWait();
            loginDisplay.setVisible(true);
            return;
        }
        //show restaurant info
        restaurantName=restaurant.getName();
        restaurantInfoShow.setText(restaurant.printFX());
        loginDisplay.setVisible(false);
        //set cover pic
        Image image = new Image(getClass().getResourceAsStream(restaurant.getName()+".jpg"));
        coverPic.setImage(image);

        //show restaurant info pane
        restaurantInfoPane.setVisible(true);
        orderList = new ArrayList<>();
        orderHistoryList = new ArrayList<>();
        //show order pane
        orderPane.setVisible(true);
        restaurantReadThread();
        System.out.println("Restaurant Client App is running");
    }

    public void restaurantReadThread(){
        new Thread(() -> {
            System.out.println("Restaurant read thread started");
            while (threadStatus) {
                try {
                    System.out.println("Reading from server");
                    Object o = networkUtil.read();
                    System.out.println(o);

                    if(o instanceof Order){
                        Order order = (Order) o;
                        System.out.println("me heere");
                        System.out.println(order.getInfo());
                        if(order.getRestaurantName().equals(restaurantName)){
                            System.out.println("Restaurant is connected");
                            Platform.runLater(() -> {
                                orderList.add(order);
                                orderListUpdate();
                            });
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }).start();
    }


    public void acceptOrders(ActionEvent actionEvent) {
        List<Order> ordersToRemove = new ArrayList<>();
        for(var order: orderList){
                //take a copy of order
                Order copyOrder = new Order(order);
                ordersToRemove.add(order);
                copyOrder.setStatus("Accepted");
                try{
                    networkUtil.write(copyOrder);
                }catch(Exception e){
                    System.out.println(e);
                }
                orderHistoryList.add(copyOrder);
        }
        orderList.removeAll(ordersToRemove);
        orderListUpdate();
        orderHistoryListUpdate();
        System.out.println("Orders accepted");
    }

    public void orderListUpdate(){
        orderListShow.getItems().clear();
        ObservableList<String> orderListObs = FXCollections.observableArrayList();
        for(var order: orderList){
            orderListObs.add(order.getInfo());
        }
        orderListShow.setItems(orderListObs);
    }

    public void orderHistoryListUpdate(){
//        orderHistoryListShow.getItems().clear();
//        ObservableList<String> orderListObs = FXCollections.observableArrayList();
//        for(var order: orderHistoryList){
//            orderListObs.add(order.getInfo());
//        }
//        orderListShow.setItems(orderListObs);
    }
}
