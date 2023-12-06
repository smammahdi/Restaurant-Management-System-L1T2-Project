package RestaurantUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {
    private int id;
    private String name;
    private double score;
    private String price;
    private String zipCode;
    private List<String> categories;
    private List<Food> foodMenu;

    // Constructor
    public Restaurant(int id,String name,double score,String price,String zipCode,List<String>categories){
        this.id=id;
        this.name=name;
        this.score=score;
        this.price=price;
        this.zipCode=zipCode;
        this.categories=new ArrayList<>();
        this.foodMenu = new ArrayList<>();
        for(int i=0;i<categories.size();i++){
            this.categories.add(categories.get(i));
        }
    }

    // Getters and Setters
    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getScore(){
        return this.score;
    }

    public String getPrice(){
        return this.price;
    }

    public String getZipCode(){
        return this.zipCode;
    }

    public List<String> getCategories(){
        return this.categories;
    }

    public List<Food> getFoodList(){
        return this.foodMenu;
    }


    // Special methods

    public void addFood(Food food){
        if(foodMenu==null){
            foodMenu=new ArrayList<>();
        }
        foodMenu.add(food);
    }

    public void print(){
        System.out.println("\tId : "+id);
        System.out.println("\tName : "+name);
        System.out.println("\tScore : "+score);
        System.out.println("\tPrice : "+price);
        System.out.println("\tZip Code : "+zipCode);
        System.out.print("\tCategories : "+categories.get(0));
        for(int i = 1;i<categories.size();i++){
            System.out.print(", "+categories.get(i));
        }
        System.out.println();
        System.out.println();

    }

    public String printFX(){
        String info = "Id : "+id + "\n" + "Name : "+name + "\n" + "Score : "+score + "\n" + "Price : "+price + "\n" + "Zip Code : "+zipCode + "\n" + "Categories : "+categories.get(0);
        for(int i = 1;i<categories.size();i++){
            info+= ", "+categories.get(i);
        }
        info+= "\n";
        return info;
    }

    public String getInfo(){
        String info = id + "," + name + "," + score + "," + price + "," + zipCode + "," + categories.get(0);
        for(int i = 1;i<categories.size();i++){
            info+= "," + categories.get(i); 
        }
        return info;
    }
}