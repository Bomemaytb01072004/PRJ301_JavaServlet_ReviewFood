/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.restaurantMenu;

public class RestaurantMenuDTO {
    private int menuId;
    private int restaurantId;
    private String dishName;
    private String picture; // Có thể là null
    private String category;
    private String cuisine;
    private int ownerId;

    // Constructor không tham số
    public RestaurantMenuDTO() {}

    // Constructor có tham số
    public RestaurantMenuDTO(int menuId, int restaurantId, String dishName, String picture, String category, String cuisine, int ownerId) {
        this.menuId = menuId;
        this.restaurantId = restaurantId;
        this.dishName = dishName;
        this.picture = picture;
        this.category = category;
        this.cuisine = cuisine;
        this.ownerId = ownerId;
    }

    // Getter và Setter cho tất cả các thuộc tính

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
