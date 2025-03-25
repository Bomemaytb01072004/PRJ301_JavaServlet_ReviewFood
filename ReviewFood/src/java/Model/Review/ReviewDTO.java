package Model.review;

import Model.users.UsersDTO;
import java.sql.Date;

public class ReviewDTO {
    private int reviewId;
    private String title;
    private String content;
    private String picture;
    private Date reviewDate;
    private String status;
    private int userId;
    private int restaurantId;
    private int positionRating;
    private int priceRating;
    private int qualityRating;
    private int serviceRating;
    private int spaceRating;
    private float totalPoint;
    private UsersDTO user;

    // Constructors
    public ReviewDTO() {}

    public ReviewDTO(int reviewId, String title, String content, String picture, Date reviewDate, 
                     String status, int userId, int restaurantId, int positionRating, 
                     int priceRating, int qualityRating, int serviceRating, int spaceRating, float totalPoint) {
        this.reviewId = reviewId;
        this.title = title;
        this.content = content;
        this.picture = picture;
        this.reviewDate = reviewDate;
        this.status = status;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.positionRating = positionRating;
        this.priceRating = priceRating;
        this.qualityRating = qualityRating;
        this.serviceRating = serviceRating;
        this.spaceRating = spaceRating;
        this.totalPoint = totalPoint;
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getPositionRating() {
        return positionRating;
    }

    public void setPositionRating(int positionRating) {
        this.positionRating = positionRating;
    }

    public int getPriceRating() {
        return priceRating;
    }

    public void setPriceRating(int priceRating) {
        this.priceRating = priceRating;
    }

    public int getQualityRating() {
        return qualityRating;
    }

    public void setQualityRating(int qualityRating) {
        this.qualityRating = qualityRating;
    }

    public int getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    public int getSpaceRating() {
        return spaceRating;
    }

    public void setSpaceRating(int spaceRating) {
        this.spaceRating = spaceRating;
    }

    public float getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(float totalPoint) {
        this.totalPoint = totalPoint;
    }
    
    public UsersDTO getUser() {
        return user;
    }

    public void setUser(UsersDTO user) {
        this.user = user;
    }
    
}

