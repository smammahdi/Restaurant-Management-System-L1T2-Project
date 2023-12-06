package RestaurantUtilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.HashMap;

public class Order implements Serializable{
    private int id;

    public String status;

    private HashMap<Food,Integer> foodQuantityMap;

    private String restaurant;
    private String customerName;

    public Order(int id, String customerName){
        this.id = id;
        this.restaurant="";
        this.customerName = customerName;
        this.foodQuantityMap = new HashMap<>();
        this.status = "pending";
    }

    public Order(Order order){
        this.id = order.getId();
        this.restaurant = order.getRestaurantName();
        this.customerName = order.getCustomerName();
        this.foodQuantityMap = new HashMap<>();
        this.status = order.status;
        for(Food food : order.foodQuantityMap.keySet()){
            this.foodQuantityMap.put(food, order.foodQuantityMap.get(food));
        }
    }

    public int getId(){
        return this.id;
    }

    public String getCustomerName(){
        return this.customerName;
    }

    //add food to order
    public void addFood(Food food){
        //if already exists, add quantity
        //extract restaurant from food
        this.restaurant = food.getRestaurantName();
        if(this.foodQuantityMap.containsKey(food)){
            int oldQuantity = this.foodQuantityMap.get(food);
            this.foodQuantityMap.put(food, oldQuantity + 1);
        }else{
            this.foodQuantityMap.put(food, 1);
        }
    }

    //remove food from order
    public void removeFood(Food food){
        if(this.foodQuantityMap.containsKey(food)){
            int oldQuantity = this.foodQuantityMap.get(food);
            if(oldQuantity == 1){
                this.foodQuantityMap.remove(food);
            }else{
                this.foodQuantityMap.put(food, oldQuantity - 1);
            }
        }
    }

    public void setStatus(String status){
        this.status = status;
    }

    //get total price of order
    public double getTotalPrice(){
        double totalPrice = 0;
        for(Food food : this.foodQuantityMap.keySet()){
            totalPrice += food.getPrice() * this.foodQuantityMap.get(food);
        }
        return totalPrice;
    }

    //restaurant name
    public String getRestaurantName(){
        return this.restaurant;
    }

    public HashMap<Food,Integer> getFoodQuantityMap(){
        return this.foodQuantityMap;
    }

    public ObservableList<String> getOrderList(){
        ObservableList<String> orderList = FXCollections.observableArrayList();
        for(Food food : this.foodQuantityMap.keySet()){
            orderList.add(food.getName() + " x" + this.foodQuantityMap.get(food));
        }
        return orderList;
    }
    public String getInfo(){
        String info = "";
        //just the customer name, retaurant,total price and status
        info += "Customer: " + this.customerName + "\n";
        info += "Restaurant: " + this.restaurant + "\n";
        info += "Total Price: " + this.getTotalPrice() + "\n";
        info += "Status: " + this.status + "\n\n";
        return info;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Order){
            Order order = (Order) o;
            if(order.getId() == this.id && order.getCustomerName().equals(this.customerName)){
                return true;
            }
        }
        return false;
    }
}
