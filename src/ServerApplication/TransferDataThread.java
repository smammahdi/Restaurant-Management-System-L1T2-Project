package ServerApplication;

import javafx.application.Platform;
import NetworkUtilities.NetworkUtil;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.HashMap;
import RestaurantUtilities.*;
public class TransferDataThread implements Runnable {
    private final String clientType;
    private final Thread thr;
    private final NetworkUtil networkUtil;

    //connected
    public static volatile ObservableList<String> connectedCustomerListItems;
    public static volatile ObservableList<String> connectedRestaurantListItems;
    public static volatile HashMap<String, NetworkUtil> customerMap;
    public static volatile HashMap<String, NetworkUtil> restaurantMap;


    public TransferDataThread(NetworkUtil networkUtil) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        this.clientType = "customer";
        thr.start();
    }

    public TransferDataThread(NetworkUtil networkUtil, String clientType) {
        this.networkUtil = networkUtil;
        this.thr = new Thread(this);
        this.clientType = clientType;
        System.out.println(clientType);
        thr.start();
    }


    @Override
    public void run() {
        try {
            while (true) {
                Object o = networkUtil.read();
                if(o instanceof String){
                    String s = (String) o;
                    System.out.println(s);
                    if(s.contains("customer close")){
                        String[] arr = s.split(": ");
                        String customerName = arr[1];
                        Platform.runLater(() -> {
                            connectedCustomerListItems.remove(customerName);
                        });
                        customerMap.remove(customerName);
                    }
                    else if(s.contains("restaurant close")){
                        String[] arr = s.split(": ");
                        String restaurantName = arr[1];
                        Platform.runLater(() -> {
                            connectedRestaurantListItems.remove(restaurantName);
                        });
                        restaurantMap.remove(restaurantName);
                        networkUtil.closeConnection();
                        break;
                    }
                }
                else if(o instanceof Order){
                    Order order = (Order) o;
                    System.out.println("me heere");
                    System.out.println(order.getInfo());
                    System.out.println(clientType);
                    if(clientType.equals("customer")) {
                        String restaurantName = order.getRestaurantName();
                        System.out.println(restaurantName);
                        //if he is connected
                        if (restaurantMap.containsKey(restaurantName)) {
                            System.out.println("Restaurant is connected");
                            NetworkUtil restaurantNetworkUtil = restaurantMap.get(restaurantName);
                            restaurantNetworkUtil.write(order);
                        }
                    }else{
                        String customerName = order.getCustomerName();
                        System.out.println(customerName);
                        System.out.println();
                        //if he is connected
                        System.out.println(customerMap);
                        if (customerMap.containsKey(customerName)) {
                            System.out.println("order delivered");
                            NetworkUtil customerNetworkUtil = customerMap.get(customerName);
                            customerNetworkUtil.write(order);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                networkUtil.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



