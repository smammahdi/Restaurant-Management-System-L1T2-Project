package RestaurantUtilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.HashMap;

public class RestaurantManager{
    private List<Restaurant> restaurantList;
    private Map<Integer, Restaurant> idToRestaurant;
    private Map<String,Integer> nameToId;
    private String RESTAURANT_OUT;
    private String FOOD_OUT;

    // Constructor
    public RestaurantManager(){
        restaurantList=new ArrayList<>();
        idToRestaurant=new HashMap<>();
        nameToId=new HashMap<>();
    }

    public RestaurantManager(List<Restaurant> restaurantList){
        this.restaurantList=restaurantList;
        idToRestaurant=new HashMap<>();
        nameToId=new HashMap<>();
        for(Restaurant restaurant : restaurantList){
            idToRestaurant.put(restaurant.getId(),restaurant);
            nameToId.put(restaurant.getName().toUpperCase(),restaurant.getId());
        }
    }


    // This loads the restaurants from current file
    public void loadRestaurants(final String INPUT_FILE_NAME) throws Exception{
        RESTAURANT_OUT=INPUT_FILE_NAME;
        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
        while (true) {
            String line = br.readLine();
            if(line==null)  break;
            String [] tokens = line.split(",(?!\\s)",-1);
            int id = Integer.parseInt(tokens[0]);
            String name = tokens[1], price = tokens[3], zipCode = tokens[4];
            double score = Double.parseDouble(tokens[2]);
            int categoryCount=tokens.length-5;
            if(tokens[tokens.length-1].equals(""))   categoryCount--;
            List<String> category = new ArrayList<>();
            for(int i=0;i<categoryCount;i++){
                category.add(tokens[5+i]);
            }
            Restaurant resturant = new Restaurant(id, name, score, price, zipCode, category);
            idToRestaurant.put(id,resturant);
            nameToId.put(name.toUpperCase(),id);                
            restaurantList.add(resturant);
        }
        br.close();
    }


    // This adds food items in respective restaurant
    public void loadFoodMenu(final String INPUT_FILE_NAME) throws Exception{
        FOOD_OUT=INPUT_FILE_NAME;
        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
        while (true){
            String line = br.readLine();
            if(line==null) break;
            String [] tokens = line.split(",(?!\\s)",-1);
            int id = Integer.parseInt(tokens[0]);
            String name = tokens[2],category = tokens[1];
            double price = Double.parseDouble(tokens[3]);
            Food food = new Food(id,category,name,price,idToRestaurant.get(id).getName());
            idToRestaurant.get(id).addFood(food);
        }
        br.close();
    }

    // This saves the updated restaurant list in the same file the data was taken
    public void saveRestaurants()throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(RESTAURANT_OUT));
        for(Restaurant restaurant : restaurantList){
            bw.write(restaurant.getInfo());
            bw.write(System.lineSeparator());
        }
        bw.close();
    }

    // This saves the updated food menu in the same file the data was taken
    public void saveFoodMenu()throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(FOOD_OUT));
        for(Restaurant restaurant : restaurantList){
            for(Food food : restaurant.getFoodList()){
                bw.write(food.getInfo());
                bw.write(System.lineSeparator());
            }
        }
        bw.close();
    }


    // Search methods
    public List<Restaurant> searchRestaurantByName(String name){
        List<Restaurant> searchResult = new ArrayList<>();
        name=name.toUpperCase();
        for(var r : nameToId.entrySet()){
            if(r.getKey().contains(name)){
                searchResult.add(idToRestaurant.get(r.getValue()));
            }
        }
        return searchResult;
    }

    public List<Restaurant> searchRestaurantByScoreRange(double score1, double score2){
        List<Restaurant> searchResult = new ArrayList<>();
        if(score1>score2){
            double temp=score1;
            score1=score2;
            score2=temp;
        }
        for(Restaurant restaurant : restaurantList){
            if(restaurant.getScore()>=score1 && restaurant.getScore()<=score2){
                searchResult.add(restaurant);
            }
        }
        return searchResult;
    }

    public List<Restaurant> searchRestaurantByCategory(String category){
        List<Restaurant> searchResult = new ArrayList<>();
        for(Restaurant restaurant : restaurantList){
            for(String cat : restaurant.getCategories()){
                if(cat.toUpperCase().contains(category.toUpperCase())){
                    searchResult.add(restaurant);
                    break;
                }
            }
        }
        return searchResult;
    }

    public List<Restaurant> searchRestaurantByPrice(String price){
        List<Restaurant> searchResult = new ArrayList<>();
        for(Restaurant restaurant : restaurantList){
            if(restaurant.getPrice().equalsIgnoreCase(price)){
                searchResult.add(restaurant);
            }
        }
        return searchResult;
    }

    public List<Restaurant> searchRestaurantByZipCode(String zipCode){
        List<Restaurant> searchResult = new ArrayList<>();
        for(Restaurant restaurant : restaurantList){
            if(restaurant.getZipCode().toUpperCase().contains(zipCode.toUpperCase())){
                searchResult.add(restaurant);
            }
        }
        return searchResult;
    }

    public Map<String,List<String>> getDifferentCategoryWiseList(){
        Map<String,List<String>> categoryWiseList = new HashMap<>();
        for(Restaurant restaurant : restaurantList){
            for(String category : restaurant.getCategories()){
                if(categoryWiseList.containsKey(category)){
                    categoryWiseList.get(category).add(restaurant.getName());
                }
                else{
                    List<String> list = new ArrayList<>();
                    list.add(restaurant.getName());
                    categoryWiseList.put(category,list);
                }
            }
        }
        return categoryWiseList;
    }

    public ObservableList<String> getDifferentCategoryWiseListForFX(){
        List<String> categoryList = new ArrayList<>();
        for(Restaurant restaurant : restaurantList){
            String restaurantWithCategory = restaurant.getName() + " :";
            for(String category : restaurant.getCategories()){
                restaurantWithCategory+=" ("+category+") ";
            }
            categoryList.add(restaurantWithCategory);
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(categoryList);
        return observableList;
    }

    public List<Food> searchFoodByName(String foodName, String restaurantName){
        List<Food> searchResult = new ArrayList<>();
        if (restaurantName==null){
            restaurantName="";
        }
        for(var restaurant:restaurantList){
            if(restaurant.getName().toUpperCase().contains(restaurantName.toUpperCase())){
                for(Food food : restaurant.getFoodList()){
                    if(food.getName().toUpperCase().contains(foodName.toUpperCase())){
                        searchResult.add(food);
                    }
                }
            }
        }
        return searchResult;
    }

    public List<Food> searchFoodByCategory(String category, String restaurantName){
        List<Food> searchResult = new ArrayList<>();
        for(var restaurant : restaurantList){
            if(restaurant.getName().toUpperCase().contains(restaurantName.toUpperCase())){
                for(Food food : restaurant.getFoodList()){
                    if(food.getCategory().toUpperCase().contains(category.toUpperCase())){
                        searchResult.add(food);
                    }
                }
            }
        }
        return searchResult;
    }

    public List<Food> searchFoodByPriceRange(double price1, double price2, String restaurantName){
        List<Food> searchResult = new ArrayList<>();
        if(price1>price2){
            double temp=price1;
            price1=price2;
            price2=temp;
        }
        restaurantName=restaurantName.toUpperCase();
        for(var restaurant : restaurantList){
            if(restaurant.getName().toUpperCase().contains(restaurantName.toUpperCase())){
                for(Food food : restaurant.getFoodList()){
                    if(food.getPrice()>=price1 && food.getPrice()<=price2){
                        searchResult.add(food);
                    }
                }
            }
        }
        return searchResult;
    }

    public List<Food> costliestFoodsInTheRestaurant(String restaurantName){
        List<Food> searchResult = new ArrayList<>();
        for(var restaurant : restaurantList){
            if(restaurant.getName().toUpperCase().contains(restaurantName.toUpperCase())){
                double maxPrice=0;
                for(Food food : restaurant.getFoodList()){
                    if(food.getPrice()>maxPrice){
                        maxPrice=food.getPrice();
                    }
                }
                for(Food food : restaurant.getFoodList()){
                    if(food.getPrice()==maxPrice){
                        searchResult.add(food);
                    }
                }
            }
        }
        return searchResult;
    }

    public Map<String,Integer> getRestaurantWiseTotalFoodList(){
        Map<String,Integer> restaurantWiseTotalFoodList = new HashMap<>();
        for(Restaurant restaurant : restaurantList){
            restaurantWiseTotalFoodList.put(restaurant.getName(),restaurant.getFoodList().size());
        }
        return restaurantWiseTotalFoodList;
    }


    // Add methods
    public void addRestaurant(Restaurant restaurant){
        restaurantList.add(restaurant);
        idToRestaurant.put(restaurant.getId(),restaurant);
        nameToId.put(restaurant.getName().toUpperCase(),restaurant.getId());
    }

    public Boolean addFood(int id, String category, String name, Double price){
        Food food = new Food(id,category,name,price,idToRestaurant.get(id).getName());
        if(idToRestaurant.containsKey(food.getId())){
            Boolean foodExists = false;
            for(Food f : idToRestaurant.get(food.getId()).getFoodList()){
                if(f.equals(food)){
                    foodExists=true;
                    break;
                }
            }
            if(!foodExists){
                idToRestaurant.get(food.getId()).addFood(food);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public List<String>getRestaurantNames(){
        List<String> restaurantNames = new ArrayList<>();
        for(Restaurant restaurant : restaurantList){
            restaurantNames.add(restaurant.getName());
        }
        return restaurantNames;
    }


    // Special methods and stuffs
    public Boolean restaurantIdExists(int id){
        return idToRestaurant.containsKey(id);
    }

    public Boolean restaurantNameExists(String name){
        return nameToId.containsKey(name.toUpperCase());
    }

    public int getRestaurantId(String name){
        return nameToId.get(name.toUpperCase());
    }

    public List<Restaurant> getRestaurantList(){
        return restaurantList;
    }

    public List<Food> getFoodList(){
        List<Food> foodList = new ArrayList<>();
        for(Restaurant restaurant : restaurantList){
            foodList.addAll(restaurant.getFoodList());
        }
        return foodList;
    }

    public Restaurant getRestaurant(String name){
        return idToRestaurant.get(nameToId.get(name.toUpperCase()));
    }

}