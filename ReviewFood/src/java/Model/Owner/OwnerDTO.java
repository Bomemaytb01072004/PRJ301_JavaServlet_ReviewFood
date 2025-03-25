package Model.Owner;

import java.sql.Date;

public class OwnerDTO {
    private int ownerId;
    private String ownerName;
    private String password;
    private String firstName;
    private String lastName;
    private String sex;
    private Date dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private String picture; // Có thể là null
    private int roleId; // Khóa ngoại liên kết tới Roles

    // Constructor không tham số
    public OwnerDTO() {}

    // Constructor có tham số
    public OwnerDTO(int ownerId, String ownerName, String password, String firstName, String lastName, String sex,
                    Date dateOfBirth, String phone, String email, String address, String picture, int roleId) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.picture = picture;
        this.roleId = roleId;
    }

    // Getter và Setter cho tất cả các thuộc tính

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
