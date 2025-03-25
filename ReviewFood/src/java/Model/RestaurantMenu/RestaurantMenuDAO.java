/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.restaurantMenu;

import Model.restaurant.RestaurantDTO;
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
public class RestaurantMenuDAO {

    public List<RestaurantMenuDTO> listMenuFood(String keyword) {

        List<RestaurantMenuDTO> list = new ArrayList<RestaurantMenuDTO>();

        try {

            Connection con = DBUtils.getConnection();
            String sql = "SELECT menuId, restaurantId, dishName, picture, category, cuisine, ownerId FROM restaurant_menu";
            if (keyword != null && !keyword.isEmpty()) {
                sql += " WHERE dishName LIKE ? OR restaurantId IN (SELECT restaurantId FROM restaurant WHERE restaurantName LIKE ?)";
            }

            PreparedStatement stmt = con.prepareStatement(sql);
            
            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(1, "%" + keyword + "%");
                stmt.setString(2, "%" + keyword + "%");
            }

            ResultSet rs = stmt.executeQuery();

            if (rs != null) {
                while (rs.next()) {
                    
                    int menuId = rs.getInt("menuId");
                    int restaurantId = rs.getInt("restaurantId");
                    String dishName = rs.getString("dishName");
                    String picture = rs.getString("picture");
                    String category = rs.getString("category");
                    String cuisine = rs.getString("cuisine");
                    int ownerId = rs.getInt("ownerId");

                    RestaurantMenuDTO menu = new RestaurantMenuDTO();
                    menu.setMenuId(menuId);
                    menu.setRestaurantId(restaurantId);
                    menu.setDishName(dishName);
                    menu.setPicture(picture);
                    menu.setCategory(category);
                    menu.setCuisine(cuisine);
                    menu.setOwnerId(ownerId);

                    list.add(menu);
                }
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in servlet. Details:" + ex.getMessage());
            ex.printStackTrace();

        }
        return list;
    }
    
    public List<RestaurantMenuDTO> getMenuByRestaurantId(int restaurantId) {

        List<RestaurantMenuDTO> list = new ArrayList<>();

        try {
            Connection con = DBUtils.getConnection();
            String sql = "SELECT menuId, dishName, picture FROM restaurant_menu WHERE restaurantId = ? ";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, restaurantId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int menuId = rs.getInt("menuId");
                String dishName = rs.getString("dishName");
                String picture = rs.getString("picture");

                RestaurantMenuDTO menu = new RestaurantMenuDTO();
                menu.setMenuId(menuId);
                menu.setDishName(dishName);
                menu.setPicture(picture);

                list.add(menu);
            }

            con.close();
        } catch (SQLException ex) {
            System.out.println("Error in RestaurantMenuDAO. Details: " + ex.getMessage());
            ex.printStackTrace();
        }

        return list;
    }
    
        public List<Object[]> getBestRatedDishes() {
        List<Object[]> bestRatedDishes = new ArrayList<>();
        
        String sql = "WITH BestRatedDishes AS ( " +
                     "    SELECT m.dishName, m.picture, r.restaurantName, rv.totalPoint, " +
                     "           ROW_NUMBER() OVER (PARTITION BY r.restaurantId ORDER BY rv.totalPoint DESC, m.menuId ASC) AS row_num " +
                     "    FROM restaurant_menu m " +
                     "    JOIN restaurant r ON m.restaurantId = r.restaurantId " +
                     "    JOIN review rv ON r.restaurantId = rv.restaurantId " +
                     ") " +
                     "SELECT dishName, picture, restaurantName, totalPoint " +
                     "FROM BestRatedDishes " +
                     "WHERE row_num = 1 " +
                     "ORDER BY totalPoint DESC;";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] dishData = new Object[4];
                dishData[0] = rs.getString("dishName");
                dishData[1] = rs.getString("picture");
                dishData[2] = rs.getString("restaurantName");
                dishData[3] = rs.getFloat("totalPoint");
                
                bestRatedDishes.add(dishData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bestRatedDishes;
    }
    
    public List<RestaurantMenuDTO> listMenuFoodWithPagination(int page, int itemsPerPage, boolean newestFirst, String category, String address) {
    List<RestaurantMenuDTO> list = new ArrayList<>();

    try {
        Connection con = DBUtils.getConnection();
        String sql = "SELECT m.menuId, m.restaurantId, m.dishName, m.picture, m.category, m.cuisine, m.ownerId " +
                     "FROM restaurant_menu m " +
                     "JOIN restaurant r ON m.restaurantId = r.restaurantId ";

        boolean hasCategory = (category != null && !category.equals("all"));
        boolean hasAddress = (address != null && !address.equals("all"));

        if (hasCategory || hasAddress) {
            sql += "WHERE ";
            if (hasCategory && hasAddress) {
                sql += "m.category = ? AND r.address = ? ";
            } else if (hasCategory) {
                sql += "m.category = ? ";
            } else if (hasAddress) {
                sql += "r.address = ? ";
            }
        }

        if (newestFirst) {
            sql += "ORDER BY m.menuId DESC ";
        } else {
            sql += "ORDER BY m.dishName ";
        }

        sql += "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        PreparedStatement stmt = con.prepareStatement(sql);
        int paramIndex = 1;

        if (hasCategory) {
            stmt.setString(paramIndex++, category);
        }
        if (hasAddress) {
            stmt.setString(paramIndex++, address);
        }

        int offset = (page - 1) * itemsPerPage;
        stmt.setInt(paramIndex++, offset);
        stmt.setInt(paramIndex, itemsPerPage);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            RestaurantMenuDTO menu = new RestaurantMenuDTO();
            menu.setMenuId(rs.getInt("menuId"));
            menu.setRestaurantId(rs.getInt("restaurantId"));
            menu.setDishName(rs.getString("dishName"));
            menu.setPicture(rs.getString("picture"));
            menu.setCategory(rs.getString("category"));
            menu.setCuisine(rs.getString("cuisine"));
            menu.setOwnerId(rs.getInt("ownerId"));

            list.add(menu);
        }

        con.close();
    } catch (SQLException e) {
        System.out.println("Error in servlet. Details:" + e.getMessage());
        e.printStackTrace();
    }

    System.out.println("Menu items retrieved for page " + page + ": " + list.size());
    return list;
}
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // Method to load menu items by restaurantId
    public List<RestaurantMenuDTO> loadMenuByRestaurantID(int restaurantId) {
        List<RestaurantMenuDTO> menuList = new ArrayList<>();
        String query = "SELECT menuId, restaurantId, dishName, picture, category, cuisine, ownerId " +
                       "FROM restaurant_menu WHERE restaurantId = ?";

        try (Connection connection = DBUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, restaurantId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                RestaurantMenuDTO menu = new RestaurantMenuDTO();
                menu.setMenuId(resultSet.getInt("menuId"));
                menu.setRestaurantId(resultSet.getInt("restaurantId"));
                menu.setDishName(resultSet.getString("dishName"));
                menu.setPicture(resultSet.getString("picture"));
                menu.setCategory(resultSet.getString("category"));
                menu.setCuisine(resultSet.getString("cuisine"));
                menu.setOwnerId(resultSet.getInt("ownerId"));
                
                menuList.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return menuList;
    }
    
    public RestaurantMenuDTO getMenuById(int menuId) {
    String sql = "SELECT menuId, restaurantId, dishName, picture, category, cuisine, ownerId FROM restaurant_menu WHERE menuId = ?";
    try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, menuId); // Set the value for the query parameter

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) { // If there is a result
                RestaurantMenuDTO menu = new RestaurantMenuDTO();
                menu.setMenuId(rs.getInt("menuId"));
                menu.setRestaurantId(rs.getInt("restaurantId"));
                menu.setDishName(rs.getString("dishName"));
                menu.setPicture(rs.getString("picture"));
                menu.setCategory(rs.getString("category"));
                menu.setCuisine(rs.getString("cuisine"));
                menu.setOwnerId(rs.getInt("ownerId"));

                return menu; // Return the RestaurantMenuDTO object
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null; // Return null if the menu is not found or there is an error
}

    public boolean createMenu(RestaurantMenuDTO menu) {
    String sql = "INSERT INTO restaurant_menu (menuId, restaurantId, dishName, picture, category, cuisine, ownerId) VALUES (?, ?, ?, ?, ?, ?, ?)";

    // Lấy ID tiếp theo cho menu
    int newMenuId = getNextMenuId();
    if (newMenuId <= 0) {
        System.err.println("Failed to generate a valid new menu ID.");
        return false;
    }

    try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, newMenuId);
        pstmt.setInt(2, menu.getRestaurantId());
        pstmt.setString(3, menu.getDishName());
        pstmt.setString(4, menu.getPicture());
        pstmt.setString(5, menu.getCategory());
        pstmt.setString(6, menu.getCuisine());
        pstmt.setInt(7, menu.getOwnerId());

        // Thực thi lệnh và trả về kết quả
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error executing insert for menu: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

    
    public boolean updateMenu(RestaurantMenuDTO menu) {
    String sql = "UPDATE restaurant_menu SET dishName = ?, picture = ?, category = ?, cuisine = ? WHERE menuId = ? AND restaurantId = ?";
    try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, menu.getDishName());
        pstmt.setString(2, menu.getPicture());
        pstmt.setString(3, menu.getCategory());
        pstmt.setString(4, menu.getCuisine());
        pstmt.setInt(5, menu.getMenuId());
        pstmt.setInt(6, menu.getRestaurantId());

        return pstmt.executeUpdate() > 0; // Return true if at least one row is updated
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Return false if the update fails
}
    
    public boolean deleteMenu(int menuId) {
    String sql = "{CALL DeleteRestaurantMenu(?)}"; // Call stored procedure to delete menu and related data

    try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // Set the menuId parameter for the stored procedure
        pstmt.setInt(1, menuId);
        pstmt.executeUpdate();

        // Verify that the menu item has been deleted
        String checkSql = "SELECT COUNT(*) FROM restaurant_menu WHERE menuId = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, menuId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0; // Return true if no records exist with the given menuId
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false; // Return false if an error occurs or the delete operation fails
}
    
    public List<RestaurantMenuDTO> searchMenu(String keyword, int ownerId, int restaurantId) {
        List<RestaurantMenuDTO> restaurantList = new ArrayList<>();
        String sql = "SELECT menuId, restaurantId, dishName, picture, category, cuisine, ownerId FROM restaurant_menu "
                + "WHERE dishName LIKE ? AND ownerId = ? AND restaurantId = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {
             
            // Thêm dấu '%' vào từ khóa để tìm kiếm toàn văn
            psmt.setString(1, "%" + keyword + "%");
            psmt.setInt(2, ownerId);
            psmt.setInt(3, restaurantId);

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    int menuId = rs.getInt("menuId");
                    int restaurantID = rs.getInt("restaurantId");
                    String dishName = rs.getString("dishName");
                    String picture = rs.getString("picture");
                    String category = rs.getString("category");
                    String cuisine = rs.getString("cuisine");
                    int ownerID = rs.getInt("ownerId");

                    RestaurantMenuDTO restaurant = new RestaurantMenuDTO(menuId, restaurantID, dishName, picture, category, cuisine, ownerID);
                    restaurantList.add(restaurant);
                    System.out.println("mon an" + restaurant.getDishName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurantList;
    }
    
    public int getNextMenuId() {
    String sql = "SELECT MAX(menuId) FROM restaurant_menu";
    int nextId = 1; // Default ID if the table is empty

    try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

        if (rs.next()) {
            int maxId = rs.getInt(1);
            if (!rs.wasNull()) { // Check if the table already has data
                nextId = maxId + 1;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error fetching max menu ID: " + e.getMessage());
        e.printStackTrace();
    }
    return nextId;
}
    
    

    
}
