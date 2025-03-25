package Model.response;

import java.sql.Date;

public class ResponseDTO {
    private int responseId;
    private String content;
    private String picture;
    private Date responseDate;
    private int reviewId;
    private int ownerId;

    // Constructor không tham số
    public ResponseDTO() {}

    // Constructor có tham số
    public ResponseDTO(int responseId, String content, String picture, Date responseDate, int reviewId, int ownerId) {
        this.responseId = responseId;
        this.content = content;
        this.picture = picture;
        this.responseDate = responseDate;
        this.reviewId = reviewId;
        this.ownerId = ownerId;
    }

    // Getter và Setter cho tất cả các thuộc tính

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
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

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}

