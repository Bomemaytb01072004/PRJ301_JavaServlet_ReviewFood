/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.restaurant;

public class RestaurantDTO {
    private int restaurantId;
    private String restaurantName;
    private String address;
    private int ownerId;
    private String picture; // Có thể là null
    private int point;

    // Constructor không tham số
    public RestaurantDTO() {}

    // Constructor có tham số
    public RestaurantDTO(int restaurantId, String restaurantName, String address, int ownerId, String picture, int point) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.ownerId = ownerId;
        this.picture = picture;
        this.point = point;
    }

    // Getter và Setter cho tất cả các thuộc tính

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
    
    
}
