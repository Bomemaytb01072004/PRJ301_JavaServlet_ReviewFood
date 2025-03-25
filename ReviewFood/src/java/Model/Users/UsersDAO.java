package Model.users;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class UsersDAO {

    public UsersDTO login(String username, String password) {
        UsersDTO user = null;
        String sql = "SELECT userId, userName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId "
                + "FROM users WHERE userName = ? AND password = ?";

        try (Connection con = DBUtils.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public UsersDTO load(String username, String password) {
        UsersDTO user = null;
        String sql = "SELECT userId, userName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId "
                + "FROM users WHERE userName = ? AND password = ?";

        try (Connection con = DBUtils.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public boolean register(UsersDTO user) {
        String sql = "INSERT INTO users (userId, userName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Gọi hàm getNextUserId() để lấy ID mới
            int newUserId = getNextUserId();
            pstmt.setInt(1, newUserId); // Thiết lập userId tự động
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getFirstName());
            pstmt.setString(5, user.getLastName());
            pstmt.setString(6, user.getSex());
            pstmt.setDate(7, new java.sql.Date(user.getDateOfBirth().getTime()));
            pstmt.setString(8, user.getPhone());
            pstmt.setString(9, user.getEmail());
            pstmt.setString(10, user.getAddress());
            pstmt.setString(11, user.getPicture());
            pstmt.setInt(12, user.getRoleId());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(UsersDTO user) {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, sex = ?, dateOfBirth = ?, phone = ?, email = ?, address = ? "
                + "WHERE userId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số cập nhật cho người dùng
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getSex());
            pstmt.setDate(4, new java.sql.Date(user.getDateOfBirth().getTime()));
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, user.getAddress());
            pstmt.setInt(8, user.getUserId()); // Đặt điều kiện WHERE với userId

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if the current password is correct
    public boolean checkPassword(int userId, String password) {
        String sql = "SELECT password FROM users WHERE userId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                return dbPassword.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update the password
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE userId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        String sql = "{CALL DeleteUserAndRelatedData(?)}"; // Gọi thủ tục SQL

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId); // Thiết lập tham số userId
            pstmt.executeUpdate();

            // Kiểm tra lại xem user đã bị xóa chưa
            String checkSql = "SELECT COUNT(*) FROM users WHERE userId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // Nếu không có bản ghi nào thì trả về true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public UsersDTO getUserById(int userId) {
        UsersDTO user = null;
        String sql = "SELECT * FROM users WHERE userId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setInt(1, userId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    user = new UsersDTO();
                    user.setUserId(rs.getInt("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setPassword(rs.getString("password"));
                    user.setFirstName(rs.getString("firstName"));
                    user.setLastName(rs.getString("lastName"));
                    user.setSex(rs.getString("sex"));
                    user.setDateOfBirth(rs.getDate("dateOfBirth"));
                    user.setPhone(rs.getString("phone"));
                    user.setEmail(rs.getString("email"));
                    user.setAddress(rs.getString("address"));
                    user.setPicture(rs.getString("picture"));
                    user.setRoleId(rs.getInt("roleId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<UsersDTO> getAllUsers() {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement psmt = conn.prepareStatement(sql); ResultSet rs = psmt.executeQuery()) {

            while (rs.next()) {
                UsersDTO user = new UsersDTO();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                user.setSex(rs.getString("sex"));
                user.setDateOfBirth(rs.getDate("dateOfBirth"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setPicture(rs.getString("picture"));
                user.setRoleId(rs.getInt("roleId"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    private UsersDTO mapResultSetToUser(ResultSet rs) throws SQLException {
        UsersDTO user = new UsersDTO();
        user.setUserId(rs.getInt("userId"));
        user.setUserName(rs.getString("userName"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setSex(rs.getString("sex"));
        user.setDateOfBirth(rs.getDate("dateOfBirth"));
        user.setPhone(rs.getString("phone"));
        user.setEmail(rs.getString("email"));
        user.setAddress(rs.getString("address"));
        user.setPicture(rs.getString("picture"));
        user.setRoleId(rs.getInt("roleId"));
        return user;
    }

    public int getNextUserId() {
        String sql = "SELECT MAX(userId) FROM users";
        int nextId = 1; // ID mặc định nếu bảng chưa có dữ liệu

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (!rs.wasNull()) { // Kiểm tra nếu bảng đã có dữ liệu
                    nextId = maxId + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }

}
