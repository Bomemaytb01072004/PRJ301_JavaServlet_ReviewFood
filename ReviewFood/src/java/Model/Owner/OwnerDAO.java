package Model.Owner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class OwnerDAO {

    public OwnerDTO login(String ownerName, String password) {
        OwnerDTO owner = null;
        String sql = "SELECT ownerId, ownerName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId "
                + "FROM owner WHERE ownerName = ? AND password = ?";

        try (Connection con = DBUtils.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ownerName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                owner = mapResultSetToOwner(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return owner;
    }

    public OwnerDTO load(String ownerName, String password) {
        OwnerDTO owner = null;
        String sql = "SELECT ownerId, ownerName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId "
                + "FROM owner WHERE ownerName = ? AND password = ?";

        try (Connection con = DBUtils.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ownerName);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                owner = mapResultSetToOwner(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return owner;
    }

    public boolean register(OwnerDTO owner) {
        String sql = "INSERT INTO owner (ownerId, ownerName, password, firstName, lastName, sex, dateOfBirth, phone, email, address, picture, roleId) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int newOwnerId = getNextOwnerId();
            pstmt.setInt(1, newOwnerId);
            pstmt.setString(2, owner.getOwnerName());
            pstmt.setString(3, owner.getPassword());
            pstmt.setString(4, owner.getFirstName());
            pstmt.setString(5, owner.getLastName());
            pstmt.setString(6, owner.getSex());
            pstmt.setDate(7, new java.sql.Date(owner.getDateOfBirth().getTime()));
            pstmt.setString(8, owner.getPhone());
            pstmt.setString(9, owner.getEmail());
            pstmt.setString(10, owner.getAddress());
            pstmt.setString(11, owner.getPicture());
            pstmt.setInt(12, owner.getRoleId());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(OwnerDTO owner) {
        String sql = "UPDATE owner SET firstName = ?, lastName = ?, sex = ?, dateOfBirth = ?, phone = ?, email = ?, address = ? "
                + "WHERE ownerId = ?";

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, owner.getFirstName());
            pstmt.setString(2, owner.getLastName());
            pstmt.setString(3, owner.getSex());
            pstmt.setDate(4, new java.sql.Date(owner.getDateOfBirth().getTime()));
            pstmt.setString(5, owner.getPhone());
            pstmt.setString(6, owner.getEmail());
            pstmt.setString(7, owner.getAddress());
            pstmt.setInt(8, owner.getOwnerId());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkPassword(int ownerId, String password) {
        String sql = "SELECT password FROM owner WHERE ownerId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
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

    public boolean updatePassword(int ownerId, String newPassword) {
        String sql = "UPDATE owner SET password = ? WHERE ownerId = ?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, ownerId);

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteOwner(int ownerId) {
        String sql = "{CALL DeleteOwnerAndRelatedData(?)}"; // Gọi thủ tục SQL

        try (Connection conn = DBUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ownerId); // Thiết lập tham số ownerId
            pstmt.executeUpdate();

            // Kiểm tra lại xem owner đã bị xóa chưa
            String checkSql = "SELECT COUNT(*) FROM owner WHERE ownerId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, ownerId);
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
    
    public OwnerDTO getOwnerById(int ownerId) {
        OwnerDTO owner = null;
        String sql = "SELECT * FROM owner WHERE ownerId = ?";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {
            
            psmt.setInt(1, ownerId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    owner = new OwnerDTO();
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }
    
    
        public List<OwnerDTO> getAllOwners() {
        List<OwnerDTO> ownerList = new ArrayList<>();
        String sql = "SELECT * FROM owner";

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery()) {
            
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


    private OwnerDTO mapResultSetToOwner(ResultSet rs) throws SQLException {
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
        return owner;
    }

    public int getNextOwnerId() {
        String sql = "SELECT MAX(ownerId) FROM owner";
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
