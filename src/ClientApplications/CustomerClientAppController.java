package ClientApplications;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import NetworkUtilities.NetworkUtil;

import java.util.List;
import java.util.ArrayList;

import RestaurantUtilities.Food;
import RestaurantUtilities.Order;
import RestaurantUtilities.Restaurant;
import RestaurantUtilities.RestaurantManager;




public class CustomerClientAppController implements Initializable{

    public TextField customerName;
    public Pane loginDisplay;
    public TextField portNumber;

    public NetworkUtil networkUtil;

    public  List<Restaurant>restaurantList;

    public RestaurantManager restaurantManager;
    public Text nameShow;
    public Rectangle nameBox;
    public Pane afterLogin;
    public Pane cartPanel;
    public Pane searchPanel;
    public ChoiceBox<String> searchChoice;
    public ChoiceBox<String> searchOptionsForRestaurant;
    public ChoiceBox<String> searchOptionsForFood;
    public TextField searchBox;
    public ChoiceBox<String> restaurantSelection;
    public TableView<Food> searchResultForFood;

    public Boolean stateOfOrderList = false;// false meaning current order item view


    // order list for this user
    public List<Order> orderList = new ArrayList<>();

    public Order currentOrder;
    public TableColumn foodNameColumn;
    public TableColumn restaurantColumn;
    public TableColumn categoryColumn;
    public TableColumn priceColumn;
    public ListView<String> searchResultForRestaurants;
    public ListView<String> searchResultListForFood;
    public ListView<String> orderListView;


    @FXML
    private Stage primaryStage;

    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        // TODO Auto-generated method stub
        searchChoice.getItems().addAll("Search By Restaurant", "Search By Food");
        searchChoice.setOnAction(this::searchChoiceAction);

        searchOptionsForRestaurant.getItems().addAll("By Name", "By Score", "By Category", "By Price", "By Zip Code", "Different Category Wise List of Restaurants");
        searchOptionsForRestaurant.setOnAction(this::searchOptionsForRestaurantAction);

        searchOptionsForFood.getItems().addAll("By Name", "By Name in a Given Restaurant", "By Category", "By Category in a Given Restaurant", "By Price Range", "By Price Range in a Given Restaurant", "Costliest Food Item(s) on the Menu in a Given Restaurant", "List of Restaurants and Total Food Item on the Menu");
        searchOptionsForFood.setOnAction(this::searchOptionsForFoodAction);

        foodNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        restaurantColumn.setCellValueFactory(new PropertyValueFactory<>("restaurantName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        addButtonToTable();

    }



    public void searchChoiceAction(ActionEvent actionEvent) {
        System.out.println(searchChoice.getValue());
        if(searchChoice.getValue().equals("Search By Restaurant")){
            searchOptionsForRestaurant.setVisible(true);
            searchOptionsForFood.setVisible(false);
            searchResultForRestaurants.setVisible(true);
            searchResultForFood.setVisible(false);
            restaurantSelection.setVisible(false);
            searchResultListForFood.setVisible(false);
            searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.getRestaurantList()));

        }else{
            searchOptionsForFood.setVisible(true);
            searchOptionsForRestaurant.setVisible(false);
            searchResultForRestaurants.setVisible(false);
            searchResultForFood.setVisible(true);
            fillTable(restaurantManager.getFoodList());
        }
    }

    public void searchOptionsForRestaurantAction(ActionEvent actionEvent) {
        //searchOptionsForRestaurant.setVisible(true);
        restaurantSelection.setVisible(false);
        System.out.println(searchOptionsForRestaurant.getValue());
        if(searchOptionsForRestaurant.getValue().equals("By Name")){
            searchBox.setVisible(true);}
        else if(searchOptionsForRestaurant.getValue().equals("By Score")){
            searchBox.setVisible(true);}
        else if(searchOptionsForRestaurant.getValue().equals("By Category")){
            searchBox.setVisible(true);}
        else if(searchOptionsForRestaurant.getValue().equals("By Price")){
            searchBox.setVisible(true);}
        else if(searchOptionsForRestaurant.getValue().equals("By Zip Code")){
            searchBox.setVisible(true);}
        else if(searchOptionsForRestaurant.getValue().equals("Different Category Wise List of Restaurants")){
            searchBox.setVisible(false);
        }
    }

    public void searchOptionsForFoodAction(ActionEvent actionEvent) {
        //searchOptionsForFood.setVisible(true);
        System.out.println(searchOptionsForFood.getValue());
        if(searchOptionsForFood.getValue().equals("By Name")){
            restaurantSelection.setVisible(false);
            searchBox.setVisible(true);
        }
        else if(searchOptionsForFood.getValue().equals("By Name in a Given Restaurant")){
            restaurantSelection.setVisible(true);
            searchBox.setVisible(true);
        }
        else if(searchOptionsForFood.getValue().equals("By Category")){
            restaurantSelection.setVisible(false);
            searchBox.setVisible(true);
        }
        else if(searchOptionsForFood.getValue().equals("By Category in a Given Restaurant")){
            restaurantSelection.setVisible(true);
            searchBox.setVisible(true);
        }
        else if(searchOptionsForFood.getValue().equals("By Price Range")){
            restaurantSelection.setVisible(false);
            searchBox.setVisible(true);
        }
        else if(searchOptionsForFood.getValue().equals("By Price Range in a Given Restaurant")){
            restaurantSelection.setVisible(true);
            searchBox.setVisible(true);
        }
        else if(searchOptionsForFood.getValue().equals("Costliest Food Item(s) on the Menu in a Given Restaurant")){
            restaurantSelection.setVisible(true);
            searchBox.setVisible(false);
        }
        else if(searchOptionsForFood.getValue().equals("List of Restaurants and Total Food Item on the Menu")){
            restaurantSelection.setVisible(false);
            searchBox.setVisible(false);
        }
    }

    public void clientService(ActionEvent actionEvent)throws Exception{
        String name = customerName.getText();
        String port = portNumber.getText();
        System.out.println(port);
        System.out.println(customerName);
        try {

            networkUtil = new NetworkUtil("127.0.0.1", Integer.parseInt(port));
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
            networkUtil.write("customer");
            networkUtil.write(name);
            loginDisplay.setVisible(false);
        Object o =networkUtil.read();
        restaurantList = (List<Restaurant>) o;
        restaurantManager = new RestaurantManager(restaurantList);

        restaurantSelection.getItems().addAll(restaurantManager.getRestaurantNames());

        loginDisplay.setVisible(false);
        nameBox.setVisible(true);
        nameShow.setVisible(true);
        afterLogin.setVisible(true);
        nameShow.setText(name);
        currentOrder = new Order(1, name);
        //orderList.add(currentOrder);
        //print food list
        for (var f: restaurantManager.getFoodList()){
            //f.print();
        }
        //fillTable(restaurantManager.getFoodList());
        searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.getRestaurantList()));
        searchResultForRestaurants.setVisible(true);
        customerReadThread();
        System.out.println("Client is running");


    }


    public void closeClient() {
        // Handle the close request (e.g., perform custom actions)
        System.out.println("Close button pressed. Performing custom actions...");
        // Add your custom actions here
        //close the client through controller class and send a message to the server before closing
        try{
            networkUtil.write("customer close: " + customerName.getText());
            networkUtil.closeConnection();
        }catch(Exception e){
            System.out.println(e);
        }

        // Exit the application gracefully
        Platform.exit();
    }

    //add a  button "add to cart" on search results for food table
    public void addButtonToTable(){
        TableColumn<Food, Void> colBtn = new TableColumn("Add to Cart");

        Callback<TableColumn<Food, Void>, TableCell<Food, Void>> cellFactory = new Callback<TableColumn<Food, Void>, TableCell<Food, Void>>() {
            @Override
            public TableCell<Food, Void> call(final TableColumn<Food, Void> param) {
                final TableCell<Food, Void> cell = new TableCell<Food, Void>() {
                    private final Button btn = new Button("Add to Cart");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Food data = getTableView().getItems().get(getIndex());
                            data.print();
                            currentOrder.addFood(data);
                            orderListView.setItems(currentOrder.getOrderList());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        }
                        else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        searchResultForFood.getColumns().add(colBtn);
    }

    public void customerReadThread(){
        new Thread(() -> {
            try {
                while (true) {
                    System.out.println("Reading from server");
                    Object o = networkUtil.read();
                    System.out.println(o);
                    if (o != null) {
                        if (o instanceof Order) {
                            Order order = (Order) o;
                            System.out.println("Order received");
                            System.out.println(order.getInfo());
                            for(Order o1 : orderList){
                                Platform.runLater(() -> {
                                    if(o1.equals(order)){
                                        o1.setStatus("Accepted");
                                        orderListUpdate();
                                    }
                                });
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }).start();
    }

    // fill table
    public void fillTable(List<Food> foodList){
        //create observable list of the food list
        ObservableList<Food> observableList = FXCollections.observableArrayList();
        observableList.addAll(foodList);
        searchResultForFood.getItems().clear();
        searchResultForFood.setItems(observableList);
    }

    public ObservableList<String> getRestaurantInfoList(List<Restaurant> restaurantList){
        ObservableList<String> restaurantInfoList = FXCollections.observableArrayList();
        for(Restaurant restaurant : restaurantList){
            restaurantInfoList.add(restaurant.printFX());
        }
        return restaurantInfoList;
    }

    public void searchAction(ActionEvent actionEvent) {
        if(searchChoice.getValue().equals("Search By Restaurant")){
            searchResultForRestaurants.setVisible(true);
            searchResultForFood.setVisible(false);
            if(searchOptionsForRestaurant.getValue().equals("By Name")){
                searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.searchRestaurantByName(searchBox.getText())));
            }
            else if(searchOptionsForRestaurant.getValue().equals("By Score")){
                //split the searchbox result into two parts
                double lowerBound;
                double upperBound;
                try {
                    String[] parts = searchBox.getText().split(",");
                    //parse two double from  them
                    lowerBound = Double.parseDouble(parts[0]);
                    upperBound = Double.parseDouble(parts[1]);
                }
                catch (Exception e){
                    System.out.println("Invalid input");
                    // show a message in the fxml window that port number is invalid
                    // Create an Alert with an error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("The entered input is invalid.");

                    // Show the Alert in a pop-up window
                    alert.showAndWait();
                    return;
                }
                searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.searchRestaurantByScoreRange(lowerBound, upperBound)));
            }
            else if(searchOptionsForRestaurant.getValue().equals("By Category")){
                searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.searchRestaurantByCategory(searchBox.getText())));
            }
            else if(searchOptionsForRestaurant.getValue().equals("By Price")){
                searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.searchRestaurantByPrice(searchBox.getText())));
            }
            else if(searchOptionsForRestaurant.getValue().equals("By Zip Code")){
                searchResultForRestaurants.setItems(getRestaurantInfoList(restaurantManager.searchRestaurantByZipCode(searchBox.getText())));
            }
            else if(searchOptionsForRestaurant.getValue().equals("Different Category Wise List of Restaurants")){
                searchResultForRestaurants.setItems(restaurantManager.getDifferentCategoryWiseListForFX());
            }

        }else{
            searchResultForRestaurants.setVisible(false);
            searchResultForFood.setVisible(true);
            searchResultListForFood.setVisible(false);
            if(searchOptionsForFood.getValue().equals("By Name")){
                fillTable(restaurantManager.searchFoodByName(searchBox.getText(),""));
            }
            else if(searchOptionsForFood.getValue().equals("By Category")){
                fillTable(restaurantManager.searchFoodByName(searchBox.getText(),""));
            }
            else if(searchOptionsForFood.getValue().equals("By Price Range")){
                //parse two double from  them
                double lowerBound;
                double upperBound;
                try {
                    String[] parts = searchBox.getText().split(",");
                    //parse two double from  them
                    lowerBound = Double.parseDouble(parts[0]);
                    upperBound = Double.parseDouble(parts[1]);
                }
                catch (Exception e){
                    System.out.println("Invalid input");
                    // Create an Alert with an error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("The entered input is invalid.");
                    return;
                }
                fillTable(restaurantManager.searchFoodByPriceRange(lowerBound, upperBound,""));
            }
            else if(searchOptionsForFood.getValue().equals("Costliest Food Item(s) on the Menu in a Given Restaurant")){
                searchResultListForFood.setVisible(true);
                searchResultForFood.setVisible(false);
                var foodList = restaurantManager.costliestFoodsInTheRestaurant(restaurantSelection.getValue());
                ObservableList<String> observableList = FXCollections.observableArrayList();
                for(Food food : foodList){
                    observableList.add(food.printFX());
                }
                searchResultListForFood.setItems(observableList);
            }
            else if(searchOptionsForFood.getValue().equals("List of Restaurants and Total Food Item on the Menu")){
                searchResultListForFood.setVisible(true);
                searchResultForFood.setVisible(false);
                restaurantSelection.setVisible(false);
                var restaurantList = restaurantManager.getRestaurantList();
                ObservableList<String> observableList = FXCollections.observableArrayList();
                for(Restaurant restaurant : restaurantList){
                    observableList.add(restaurant.getName()+": "+restaurant.getFoodList().size());
                }
                searchResultListForFood.setItems(observableList);
            }
            else if(searchOptionsForFood.getValue().equals("By Name in a Given Restaurant")){
                fillTable(restaurantManager.searchFoodByName(searchBox.getText(),restaurantSelection.getValue()));
            }
            else if(searchOptionsForFood.getValue().equals("By Category in a Given Restaurant")){
                fillTable(restaurantManager.searchFoodByCategory(searchBox.getText(),restaurantSelection.getValue()));
            }
            else if(searchOptionsForFood.getValue().equals("By Price Range in a Given Restaurant")){
                //parse two double from  them
                double lowerBound;
                double upperBound;
                try {
                    String[] parts = searchBox.getText().split(",");
                    //parse two double from  them
                    lowerBound = Double.parseDouble(parts[0]);
                    upperBound = Double.parseDouble(parts[1]);
                }
                catch (Exception e){
                    System.out.println("Invalid input");
                    // show a message in the fxml window that port number is invalid
                    // Create an Alert with an error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("The entered input is invalid.");
                    return;
                }
                fillTable(restaurantManager.searchFoodByPriceRange(lowerBound, upperBound,restaurantSelection.getValue()));
            }
        }
    }

    public void processOrder(ActionEvent actionEvent) {
        System.out.println("Order processed");
        //if no food in order
        if(currentOrder.getFoodQuantityMap().size() == 0){
            System.out.println("No food in order");
            // Create an Alert with an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No food in order");
            alert.setContentText("Please add food to order");
            alert.showAndWait();
            return;
        }
        //send order to server
        try{
            //networkUtil.write("order");
            networkUtil.write(currentOrder);
            System.out.println("Order sent");
            //add order to order list
            orderList.add(currentOrder);
            //clear current order
            int id = currentOrder.getId()+1;
            String prevName=currentOrder.getCustomerName();
            currentOrder = new Order( id,prevName );
            //clear order list view
            orderListView.setItems(currentOrder.getOrderList());
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void clearOrder(ActionEvent actionEvent) {
        currentOrder = new Order(currentOrder.getId(), currentOrder.getCustomerName());
        orderListView.setItems(currentOrder.getOrderList());
    }



    public void orderListUpdate(){
            ObservableList<String> observableList = FXCollections.observableArrayList();
            for (Order order : orderList) {
                observableList.add(order.getInfo());
            }
            if(stateOfOrderList){
                orderListView.setItems(observableList);
            }
            else{
                orderListView.setItems(currentOrder.getOrderList());
            }
    }

    public void viewCurrentOrderItems(ActionEvent actionEvent) {
        stateOfOrderList = false;
        orderListView.setItems(currentOrder.getOrderList());
    }

    public void showOrderHistoryList(ActionEvent actionEvent) {
        stateOfOrderList = true;
        ObservableList<String> observableList = FXCollections.observableArrayList();
        for (Order order : orderList) {
            observableList.add(order.getInfo());
        }
        orderListView.setItems(observableList);
    }
}
