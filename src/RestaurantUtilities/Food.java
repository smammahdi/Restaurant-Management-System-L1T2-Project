package RestaurantUtilities;

import java.io.Serializable;

public class Food implements Serializable {
    public int id;
    public String category;
    public String name;
    public double price;
    public String restaurantName;

    // Constructor
    public Food(int id,String category,String name,double price,String restaurantName){
        this.id=id;
        this.category=category;
        this.name=name;
        this.price=price;
        this.restaurantName=restaurantName;
    }

    // Getters and Setters
    public int getId(){
        return this.id;
    }
    
    public String getCategory(){
        return this.category;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public String getRestaurantName(){
        return this.restaurantName;
    }


    // Special methods and stuff
    public String getInfo(){
        String info = id + "," + category + "," + name + "," + price;
        return info;
    }

    public void print(){
        System.out.println("\tName: " + name);
        System.out.println("\tCategory: " + category);
        System.out.println("\tPrice: " + price);
        System.out.println("\tRestaurant: " + restaurantName);
        System.out.println();
    }

    public String printFX(){
        String info = "Name: " + name + "\n" + "Category: " + category + "\n" + "Price: " + price + "\n" + "Restaurant: " + restaurantName + "\n";
        return info;
    }

    public Boolean equals(Food food){
        return this.id==food.getId() && this.name.equalsIgnoreCase(food.getName()) && this.category.equalsIgnoreCase(food.getCategory());
    }
}