/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.restaurant;

import java.sql.CallableStatement;
import java.sql.Connection;
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
public class RestaurantDAO {

    public RestaurantDTO getRestaurantById(int restaurantId) {
        String sql = "SELECT restaurantId, restaurantName, address, ownerId, picture FROM restaurant WHERE restaurantId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, restaurantId); // Thiết lập giá trị của tham số truy vấn

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // Nếu có kết quả
                    RestaurantDTO restaurant = new RestaurantDTO();
                    restaurant.setRestaurantId(rs.getInt("restaurantId"));
                    restaurant.setRestaurantName(rs.getString("restaurantName"));
                    restaurant.setAddress(rs.getString("address"));
                    restaurant.setOwnerId(rs.getInt("ownerId"));
                    restaurant.setPicture(rs.getString("picture"));

                    return restaurant; // Trả về đối tượng RestaurantDTO
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy nhà hàng hoặc có lỗi
    }

    public List<RestaurantDTO> loadListOfRestaurantByOwnerID(int ownerId) {
        List<RestaurantDTO> restaurants = new ArrayList<>();
        String sql = "SELECT restaurantId, restaurantName, address, ownerId, picture FROM restaurant WHERE ownerId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                RestaurantDTO restaurant = new RestaurantDTO(
                        rs.getInt("restaurantId"),
                        rs.getString("restaurantName"),
                        rs.getString("address"),
                        rs.getInt("ownerId"),
                        rs.getString("picture"),
                        0
                );
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public boolean createRestaurant(RestaurantDTO restaurant) {
        String sql = "INSERT INTO restaurant (restaurantId, restaurantName, address, ownerId, picture) VALUES (?, ?, ?, ?, ?)";

        // Lấy ID tiếp theo cho nhà hàng
        int newRestaurantId = getNextRestaurantId();
        if (newRestaurantId <= 0) {
            System.err.println("Failed to generate a valid new restaurant ID.");
            return false;
        }

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newRestaurantId);
            pstmt.setString(2, restaurant.getRestaurantName());
            pstmt.setString(3, restaurant.getAddress());
            pstmt.setInt(4, restaurant.getOwnerId());
            pstmt.setString(5, restaurant.getPicture());

            // Thực thi lệnh và trả về kết quả
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error executing insert for restaurant: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRestaurant(RestaurantDTO restaurant) {
        String sql = "UPDATE restaurant SET restaurantName = ?, address = ?, picture = ? WHERE restaurantId = ? AND ownerId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, restaurant.getRestaurantName());
            pstmt.setString(2, restaurant.getAddress());
            pstmt.setString(3, restaurant.getPicture());
            pstmt.setInt(4, restaurant.getRestaurantId());
            pstmt.setInt(5, restaurant.getOwnerId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteRestaurant(int restaurantId) {
        String sql = "{CALL DeleteRestaurantAndRelatedData(?)}"; // Gọi thủ tục xóa nhà hàng và dữ liệu liên quan

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Thiết lập tham số restaurantId cho thủ tục lưu trữ
            pstmt.setInt(1, restaurantId);
            pstmt.executeUpdate();

            // Kiểm tra lại để đảm bảo rằng nhà hàng đã bị xóa
            String checkSql = "SELECT COUNT(*) FROM restaurant WHERE restaurantId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, restaurantId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // Trả về true nếu không còn bản ghi nào với restaurantId đã cho
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra hoặc không xóa được
    }

    public List<RestaurantDTO> searchRestaurants(String keyword, int ownerId) throws SQLException {
        List<RestaurantDTO> restaurantList = new ArrayList<>();
        String sql = "SELECT restaurantId, restaurantName, address, ownerId, picture FROM restaurant WHERE restaurantName LIKE ? AND ownerId = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {
             
            // Thêm dấu '%' vào từ khóa để tìm kiếm toàn văn
            psmt.setString(1, "%" + keyword + "%");
            psmt.setInt(2, ownerId);
            
            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    int restaurantId = rs.getInt("restaurantID");
                    String restaurantName = rs.getString("restaurantName");
                    String address = rs.getString("address");
                    int ownerID = rs.getInt("ownerId");
                    String picture = rs.getString("picture");
                    RestaurantDTO restaurant = new RestaurantDTO(restaurantId, restaurantName, address, ownerID, picture, 0);
                    restaurantList.add(restaurant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantList;
    }

    public int getNextRestaurantId() {
        String sql = "SELECT MAX(restaurantId) FROM restaurant";
        int nextId = 1; // ID mặc định nếu bảng chưa có dữ liệu

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) { // Kiểm tra nếu bảng đã có dữ liệu
                    nextId = maxId + 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching max restaurant ID: " + e.getMessage());
            e.printStackTrace();
        }
        return nextId;
    }

    public boolean calculatePoint(int restaurantId) {
    String getAverageQuery = "SELECT AVG(totalPoint) as averagePoint FROM review WHERE restaurantId = ? AND status = 'A'";
    String updateRestaurantPointQuery = "UPDATE restaurant SET point = ? WHERE restaurantId = ?";
    
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement getAvgStmt = conn.prepareStatement(getAverageQuery);
         PreparedStatement updateStmt = conn.prepareStatement(updateRestaurantPointQuery)) {

        // Bước 1: Tính trung bình totalPoint cho các review đã được chấp nhận
        getAvgStmt.setInt(1, restaurantId);
        ResultSet rs = getAvgStmt.executeQuery();

        if (rs.next()) {
            double averagePoint = rs.getDouble("averagePoint");

            // Bước 2: Cập nhật lại điểm cho nhà hàng
            updateStmt.setDouble(1, averagePoint);
            updateStmt.setInt(2, restaurantId);

            int rowsUpdated = updateStmt.executeUpdate();
            return rowsUpdated > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<RestaurantDTO> listRestaurant(String keyword) {

        List<RestaurantDTO> list = new ArrayList<RestaurantDTO>();

        try {
            Connection con = DBUtils.getConnection();

            String sql = "SELECT DISTINCT r.restaurantId, r.restaurantName, m.dishName ,r.address, r.ownerId, r.picture "
                    + "FROM restaurant r "
                    + "LEFT JOIN restaurant_menu m ON r.restaurantId = m.restaurantId";

            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE r.restaurantName LIKE ? OR m.dishName LIKE ?";
            }

            PreparedStatement stmt = con.prepareStatement(sql);

            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");
            }

            ResultSet rs = stmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    int restaurantId = rs.getInt("restaurantId");
                    String restaurantName = rs.getString("restaurantName");
                    String address = rs.getString("address");
                    int ownerId = rs.getInt("ownerId");
                    String picture = rs.getString("picture");

                    RestaurantDTO restaurant = new RestaurantDTO();

                    restaurant.setRestaurantId(restaurantId);
                    restaurant.setRestaurantName(restaurantName);
                    restaurant.setAddress(address);
                    restaurant.setOwnerId(ownerId);
                    restaurant.setPicture(picture);

                    list.add(restaurant);
                }
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in servlet. Details:" + ex.getMessage());
            ex.printStackTrace();
        }
        return list;
    }

    public RestaurantDTO getRestaurantByIdHome(String restaurantId) {
        RestaurantDTO restaurant = null;

        try {

            Connection con = DBUtils.getConnection();
            String sql = "SELECT restaurantId, ownerId, restaurantName, address, picture FROM Restaurant WHERE restaurantId = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, restaurantId);

            ResultSet rs = stmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    restaurant = new RestaurantDTO();

                    restaurant.setRestaurantId(rs.getInt("restaurantId"));
                    restaurant.setRestaurantName(rs.getString("restaurantName"));
                    restaurant.setAddress(rs.getString("address"));
                    restaurant.setOwnerId(rs.getInt("ownerId"));
                    restaurant.setPicture(rs.getString("picture"));

                }
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in servlet. Details:" + ex.getMessage());
            ex.printStackTrace();

        }
        return restaurant;
    }

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public RestaurantDTO getOneRestaurant(int restaurantId) {
        String sql = "SELECT restaurantId, restaurantName, address, ownerId, picture FROM restaurant WHERE restaurantId = ? ";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            rs = ps.executeQuery();
            if (rs.next()) {
                // Lấy từng field từ ResultSet
                int id = rs.getInt("restaurantId");
                String name = rs.getString("restaurantName");
                String address = rs.getString("address");
                int ownerId = rs.getInt("ownerId");
                String picture = rs.getString("picture");

                // Tạo và trả về đối tượng RestaurantDTO
                return new RestaurantDTO(id, name, address, ownerId, picture, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public String getPhoneRestaurant(int restaurantId) {
        String sql = "SELECT o.phone FROM owner o JOIN restaurant r ON o.ownerId = r.ownerId WHERE r.restaurantId = ?";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            rs = ps.executeQuery();
            if (rs.next()) {
                // Lấy phone từ ResultSet
                return rs.getString("phone");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối, PreparedStatement và ResultSet để giải phóng tài nguyên
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public List<Object[]> getInfoUser(int restaurantId) {
        List<Object[]> userReviews = new ArrayList<>();
        String sql = "SELECT u.firstName, u.lastName, r.reviewDate, r.title, r.content, r.totalPoint, r.picture "
                + "FROM users u "
                + "JOIN review r ON u.userId = r.userId "
                + "JOIN restaurant rest ON r.restaurantId = rest.restaurantId "
                + "WHERE rest.restaurantId = ?";
        try {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, restaurantId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Object[] reviewData = new Object[7];
                reviewData[0] = rs.getString("firstName");
                reviewData[1] = rs.getString("lastName");
                reviewData[2] = rs.getDate("reviewDate");
                reviewData[3] = rs.getString("title");
                reviewData[4] = rs.getString("content");
                reviewData[5] = rs.getFloat("totalPoint");
                reviewData[6] = rs.getString("picture");

                userReviews.add(reviewData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return userReviews;
    }
}
