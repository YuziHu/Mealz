package com.example.mealz.Models;

import java.util.List;

public class User {
    private final String uid;
    public String name;
    public String email;
    // List of Strings representing grocery ids
    public List<String> groceryList;
    // registration token for firebase message
    public String token;

    public User(String uid, String name, String email, List<String> groceryList, String token){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.groceryList = groceryList;
        this.token = token;
    }

    // get and set methods
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public List<String> getGroceryList(){
        return this.groceryList;
    }
    public String getToken(){ return this.token; }
    //
    public void setName(String newName){
        this.name = newName;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void updateGroceryList(List<String> groceryList){
        this.groceryList = groceryList;
    }
    public void setToken(String token){ this.token = token; }
}
