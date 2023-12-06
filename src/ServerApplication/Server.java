package ServerApplication;
import javafx.application.Platform;
import NetworkUtilities.NetworkUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import javafx.collections.ObservableList;
import RestaurantUtilities.RestaurantManager;
public class Server implements Runnable {

    public RestaurantManager restaurantManager;

    private ServerSocket serverSocket;
    private NetworkUtil networkUtil;

    public static volatile ObservableList<String> connectedCustomerListItems;
    public static volatile ObservableList<String> connectedRestaurantListItems;

    //user map
    public static volatile HashMap<String, NetworkUtil> customerMap = new HashMap<>();
    public static volatile HashMap<String, NetworkUtil> restaurantMap = new HashMap<>();

    public int portNumber;
    public Thread thr;

    public volatile boolean isRunning = true;
    public Server(String port){
        TransferDataThread.connectedCustomerListItems = connectedCustomerListItems;
        TransferDataThread.connectedRestaurantListItems = connectedRestaurantListItems;
        TransferDataThread.customerMap = customerMap;
        TransferDataThread.restaurantMap = restaurantMap;
        this.portNumber = Integer.parseInt(port);
        this.thr = new Thread(this);
        thr.start();
    }
    @Override
    public void run() {
        LoadRestaurantManager();
        try {
            serverSocket = new ServerSocket(portNumber);
            while (true) {
                if(!isRunning) break;
                Socket clientSocket = serverSocket.accept();
                networkUtil = new NetworkUtil(clientSocket);
                //update client observable list
                String userType = (String) networkUtil.read();
                System.out.println(userType);
                if (userType.contains("customer")){
                    System.out.println("Customer connected");
                    String customerName = (String) networkUtil.read();
                    Platform.runLater(() -> {
                        connectedCustomerListItems.add(customerName);
                        customerMap.put(customerName, networkUtil);
                        new TransferDataThread(networkUtil, "customer");
                        try{
                            networkUtil.write(restaurantManager.getRestaurantList());
                        }catch(Exception e){
                            System.out.println(e);
                        }
                    });
                }else{
                    System.out.println("Restaurant connected");
                    String restaurantName = (String) networkUtil.read();
                    System.out.println(restaurantName);
                    if(!restaurantManager.restaurantNameExists(restaurantName)){
                        networkUtil.write("Restaurant name does not exist");
                        continue;
                    }else{
                        networkUtil.write("Restaurant name exists");
                        System.out.println("Restaurant name exists");
                    }
                    networkUtil.write(restaurantManager.getRestaurant(restaurantName));
                    String finalRestaurantName = restaurantManager.getRestaurant(restaurantName).getName();
                    Platform.runLater(() -> {
                        restaurantMap.put(finalRestaurantName, networkUtil);
                        connectedRestaurantListItems.add(finalRestaurantName);
                        System.out.println("Restaurant added");
                    });
                    new TransferDataThread(networkUtil, "restaurant");
                }
            }
        } catch (Exception e) {
            System.out.println("Server starts:" + e);
        }
    }

    public void LoadRestaurantManager(){
        restaurantManager = new RestaurantManager();
        try{
        restaurantManager.loadRestaurants("/Users/smammahdi/Library/CloudStorage/OneDrive-BUET/BUET/L1T2/CSE 108/Java/Term Project/Project/src/ServerApplication/restaurant.txt");
        restaurantManager.loadFoodMenu("/Users/smammahdi/Library/CloudStorage/OneDrive-BUET/BUET/L1T2/CSE 108/Java/Term Project/Project/src/ServerApplication/menu.txt");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void stopServer(){
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Error in closing server socket");
        }
    }
}
