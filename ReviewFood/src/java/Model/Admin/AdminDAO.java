/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.Admin;

import Model.Owner.OwnerDTO;
import Model.users.UsersDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author admin
 */
public class AdminDAO {

    public AdminDTO load(String adminName, String password) {
        AdminDTO admin = null;
        String sql = "SELECT userId, userName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId "
                + "FROM users WHERE userName = ? AND password = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adminName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                admin = mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return admin;
    }

    public boolean update(AdminDTO admin) {
        String sql = "UPDATE users SET firstName = ?, lastName = ?, sex = ?, dateOfBirth = ?, phone = ?, email = ?, address = ? "
                + "WHERE userId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số cập nhật cho admin
            pstmt.setString(1, admin.getFirstName());
            pstmt.setString(2, admin.getLastName());
            pstmt.setString(3, admin.getSex());
            pstmt.setDate(4, new java.sql.Date(admin.getDateOfBirth().getTime()));
            pstmt.setString(5, admin.getPhone());
            pstmt.setString(6, admin.getEmail());
            pstmt.setString(7, admin.getAddress());
            pstmt.setInt(8, admin.getAdminId());

            // Kiểm tra dateOfBirth có null không trước khi set giá trị
            if (admin.getDateOfBirth() != null) {
                pstmt.setDate(4, new java.sql.Date(admin.getDateOfBirth().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            pstmt.setString(5, admin.getPhone());
            pstmt.setString(6, admin.getEmail());
            pstmt.setString(7, admin.getAddress());
            pstmt.setInt(8, admin.getAdminId()); // Đặt điều kiện WHERE với adminId

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0; // Trả về true nếu có ít nhất 1 dòng được cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// Check if the current password is correct
    public boolean checkPassword(int adminId, String password) {
        String sql = "SELECT password FROM users WHERE userId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adminId);
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
    public boolean updatePassword(int adminId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE userId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, adminId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// Delete admin and related data
    public boolean deleteAdmin(int adminId) {
        String sql = "{CALL DeleteUserAndRelatedData(?)}"; // Gọi thủ tục SQL để xóa admin và các dữ liệu liên quan

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adminId); // Thiết lập tham số adminId
            pstmt.executeUpdate();

            // Kiểm tra lại xem admin đã bị xóa chưa
            String checkSql = "SELECT COUNT(*) FROM users WHERE userId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, adminId);
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

    public List<UsersDTO> searchUsers(String keyword) {
        List<UsersDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE firstName LIKE ? OR lastName LIKE ? OR userName LIKE ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

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

    public List<OwnerDTO> searchOwners(String keyword) {
        List<OwnerDTO> ownerList = new ArrayList<>();
        String sql = "SELECT * FROM owner WHERE firstName LIKE ? OR lastName LIKE ? OR ownerName LIKE ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Thiết lập tham số tìm kiếm
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                OwnerDTO owner = new OwnerDTO();
                owner.setOwnerId(rs.getInt("ownerId"));
                owner.setOwnerName(rs.getString("ownerName"));
                owner.setPassword(rs.getString("password"));
                owner.setFirstName(rs.getString("firstName"));
                owner.setLastName(rs.getString("lastName"));
                owner.setSex(rs.getString("sex"));
                owner.setDateOfBirth(rs.getDate("dateOfBirth"));
                owner.setPhone(rs.getString("phone"));
                owner.setEmail(rs.getString("email"));
                owner.setAddress(rs.getString("address"));
                owner.setPicture(rs.getString("picture"));
                owner.setRoleId(rs.getInt("roleId"));

                ownerList.add(owner);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ownerList;
    }

    private AdminDTO mapResultSetToUser(ResultSet rs) throws SQLException {
        AdminDTO admin = new AdminDTO();
        admin.setAdminId(rs.getInt("userId"));
        admin.setAdminName(rs.getString("userName"));
        admin.setPassword(rs.getString("password"));
        admin.setFirstName(rs.getString("firstName"));
        admin.setLastName(rs.getString("lastName"));
        admin.setSex(rs.getString("sex"));
        admin.setDateOfBirth(rs.getDate("dateOfBirth"));
        admin.setPhone(rs.getString("phone"));
        admin.setEmail(rs.getString("email"));
        admin.setAddress(rs.getString("address"));
        admin.setPicture(rs.getString("picture"));
        admin.setRoleId(rs.getInt("roleId"));
        return admin;
    }
}
