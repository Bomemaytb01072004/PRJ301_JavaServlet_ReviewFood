/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.review;

import Model.users.UsersDTO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author Administrator
 */
public class ReviewDAO {

    public List<ReviewDTO> getPendingReviews() {
        List<ReviewDTO> reviews = new ArrayList<>();
        String sql = "SELECT r.reviewId, u.userId, u.userName, r.status, u.firstName, u.lastName, u.roleId "
                + "FROM review r "
                + "JOIN users u ON r.userId = u.userId "
                + "WHERE r.status = 'W'";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setReviewId(rs.getInt("reviewId"));
                review.setUserId(rs.getInt("userId")); // Thiết lập userId từ review

                // Thiết lập thông tin User
                UsersDTO user = new UsersDTO();
                user.setUserId(rs.getInt("userId")); // Đảm bảo đúng cột
                user.setUserName(rs.getString("userName"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setRoleId(rs.getInt("roleId"));

                review.setUser(user); // Gán đối tượng User vào ReviewDTO
                review.setStatus(rs.getString("status")); // Trạng thái review

                reviews.add(review); // Thêm vào danh sách reviews
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public ReviewDTO getReviewDetail(int reviewId) {
        ReviewDTO review = null;
        String sql = "SELECT r.reviewId, r.title, r.content, r.picture, r.reviewDate, r.status, "
                + "r.restaurantId, r.positionRating, r.priceRating, r.qualityRating, "
                + "r.serviceRating, r.spaceRating, r.totalPoint, "
                + "u.userId, u.userName, u.firstName, u.lastName, u.roleId "
                + "FROM review r "
                + "JOIN users u ON r.userId = u.userId "
                + "WHERE r.reviewId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reviewId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Initialize ReviewDTO object
                    review = new ReviewDTO();
                    review.setReviewId(rs.getInt("reviewId"));
                    review.setTitle(rs.getString("title"));
                    review.setContent(rs.getString("content"));
                    review.setPicture(rs.getString("picture"));
                    review.setReviewDate(rs.getDate("reviewDate"));
                    review.setStatus(rs.getString("status"));
                    review.setRestaurantId(rs.getInt("restaurantId"));
                    review.setPositionRating(rs.getInt("positionRating"));
                    review.setPriceRating(rs.getInt("priceRating"));
                    review.setQualityRating(rs.getInt("qualityRating"));
                    review.setServiceRating(rs.getInt("serviceRating"));
                    review.setSpaceRating(rs.getInt("spaceRating"));
                    review.setTotalPoint(rs.getFloat("totalPoint"));

                    // Set user information in UsersDTO
                    UsersDTO user = new UsersDTO();
                    user.setUserId(rs.getInt("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setRoleId(rs.getInt("roleId"));

                    // Assign UsersDTO to ReviewDTO
                    review.setUser(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return review;
    }

    public boolean acceptReview(int reviewId) {
        String sql = "UPDATE review SET status = 'A' WHERE reviewId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reviewId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean rejectReview(int reviewId) {
        String sql = "{CALL DeleteReviewAndRelatedData(?)}"; // Call the stored procedure

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareCall(sql)) {

            // Set the reviewId parameter for the stored procedure
            stmt.setInt(1, reviewId);

            // Execute the stored procedure
            stmt.execute(); // We use execute() to check for any exceptions instead of relying on affected rows

            // If no exception is thrown, we assume deletion was successful
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Return false if an exception occurs
    }

    public List<ReviewDTO> searchReviewsByFullName(String keyword) {
        List<ReviewDTO> reviews = new ArrayList<>();
        String sql = "SELECT r.reviewId, r.title, r.content, r.status, r.reviewDate, u.userId, u.firstName, u.lastName, u.roleId "
                + "FROM review r "
                + "JOIN users u ON r.userId = u.userId "
                + "WHERE r.status = 'W' "
                + "AND (u.firstName LIKE ? OR u.lastName LIKE ?)";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the search keyword with wildcards for partial matching
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReviewDTO review = new ReviewDTO();
                    review.setReviewId(rs.getInt("reviewId"));
                    review.setTitle(rs.getString("title"));
                    review.setContent(rs.getString("content"));
                    review.setStatus(rs.getString("status"));

                    // Create and set the user information in ReviewDTO
                    UsersDTO user = new UsersDTO();
                    user.setUserId(rs.getInt("userId"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setRoleId(rs.getInt("roleId"));

                    review.setUser(user);
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public List<ReviewDTO> getReviewById(int restaurantId) {

        List<ReviewDTO> list = new ArrayList<>();

        try {
            Connection con = DBUtils.getConnection();
            String sql = "SELECT reviewId, title, content, picture, reviewDate, status, userId, restaurantId, "
                    + "positionRating, priceRating, qualityRating, serviceRating, spaceRating, totalPoint "
                    + "FROM review WHERE restaurantId = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, restaurantId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int reviewId = rs.getInt("reviewId");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String picture = rs.getString("picture");
                Date reviewDate = rs.getDate("reviewDate");
                String status = rs.getString("status");
                int userId = rs.getInt("userId");
                int positionRating = rs.getInt("positionRating");
                int priceRating = rs.getInt("priceRating");
                int qualityRating = rs.getInt("qualityRating");
                int serviceRating = rs.getInt("serviceRating");
                int spaceRating = rs.getInt("spaceRating");
                float totalPoint = rs.getFloat("totalPoint");

                // Create a ReviewDTO object and set values
                ReviewDTO review = new ReviewDTO();
                review.setReviewId(reviewId);
                review.setTitle(title);
                review.setContent(content);
                review.setPicture(picture);
                review.setReviewDate(reviewDate);
                review.setStatus(status);
                review.setUserId(userId);
                review.setPositionRating(positionRating);
                review.setPriceRating(priceRating);
                review.setQualityRating(qualityRating);
                review.setServiceRating(serviceRating);
                review.setSpaceRating(spaceRating);
                review.setTotalPoint(totalPoint);

                // Add review to the list
                list.add(review);
            }

            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in ReviewDAO. Details: " + ex.getMessage());
            ex.printStackTrace();
        }

        return list;
    }

    public boolean saveReview(ReviewDTO review) {
        String sql = "INSERT INTO review (reviewId, title, content, picture, reviewDate, status, userId, restaurantId, "
                + "positionRating, priceRating, qualityRating, serviceRating, spaceRating, totalPoint) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Tính tổng điểm trung bình từ các đánh giá
        float totalPoint = (review.getPositionRating() + review.getPriceRating() + review.getQualityRating()
                + review.getServiceRating() + review.getSpaceRating()) / 5.0f;
        review.setTotalPoint(totalPoint);

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Lấy reviewId mới và thiết lập vào đối tượng review
            int reviewId = getNextReviewId();
            review.setReviewId(reviewId);

            // Thiết lập các tham số cho PreparedStatement
            stmt.setInt(1, reviewId);
            stmt.setString(2, review.getTitle());
            stmt.setString(3, review.getContent());
            stmt.setString(4, review.getPicture());
            stmt.setDate(5, new Date(System.currentTimeMillis())); // Ngày hiện tại làm reviewDate
            stmt.setString(6, review.getStatus());
            stmt.setInt(7, review.getUserId());
            stmt.setInt(8, review.getRestaurantId());
            stmt.setInt(9, review.getPositionRating());
            stmt.setInt(10, review.getPriceRating());
            stmt.setInt(11, review.getQualityRating());
            stmt.setInt(12, review.getServiceRating());
            stmt.setInt(13, review.getSpaceRating());
            stmt.setFloat(14, totalPoint);

            // Thực thi câu lệnh INSERT
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Object[]> getTopReviewsByTotalPoint() {
        List<Object[]> topReviews = new ArrayList<>();
        String sql = "SELECT TOP 16\n"
                + "    r.reviewId,\n"
                + "    r.restaurantId,\n"
                + "    rest.restaurantName,\n"
                + "    rest.picture AS restaurantPicture,\n"
                + "    r.totalPoint\n"
                + "FROM \n"
                + "    review r\n"
                + "JOIN \n"
                + "    restaurant rest ON r.restaurantId = rest.restaurantId\n"
                + "ORDER BY \n"
                + "    r.totalPoint DESC;";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] reviewData = new Object[5];
                reviewData[0] = rs.getInt("reviewId");
                reviewData[1] = rs.getInt("restaurantId");
                reviewData[2] = rs.getString("restaurantName");
                reviewData[3] = rs.getString("restaurantPicture");
                reviewData[4] = rs.getFloat("totalPoint");

                topReviews.add(reviewData);
            }

            System.out.println("Number of reviews retrieved: " + topReviews.size()); // Kiểm tra số lượng review
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topReviews;
    }

    public int getNextReviewId() {
        String sql = "SELECT MAX(reviewId) FROM review";
        int nextId = 1;

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) {
                    nextId = maxId + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }
    
    
}
