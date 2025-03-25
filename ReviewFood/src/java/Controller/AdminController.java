package Controller;

import Model.Admin.AdminDAO;
import Model.Admin.AdminDTO;
import Model.Owner.OwnerDAO;
import Model.Owner.OwnerDTO;
import Model.restaurant.RestaurantDAO;
import Model.review.ReviewDAO;
import Model.review.ReviewDTO;
import Model.users.UsersDAO;
import Model.users.UsersDTO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        AdminDTO admin = (AdminDTO) session.getAttribute("adminSession");

        if (session == null || admin == null || admin.getRoleId() != 1) {
            response.sendRedirect(request.getContextPath() + "/View/Home_GUI/Login.jsp");
            return;
        }

        String action = request.getParameter("action");
        AdminDAO adminDAO = new AdminDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        UsersDAO usersDAO = new UsersDAO();
        ReviewDAO reviewDAO = new ReviewDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();

        if (action == null || action.equals("showProfile")) {
            // Sử dụng hàm load để lấy thông tin chi tiết của admin từ adminDAO
            AdminDTO adminInfo = adminDAO.load(admin.getAdminName(), admin.getPassword());
            request.setAttribute("adminInfo", adminInfo);
            request.setAttribute("showProfile", true);
            // Điều hướng đến trang cài đặt của admin
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        } else if (action.equals("updateAdminInfo")) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String sex = request.getParameter("sex");
            String email = request.getParameter("email");
            String confirmEmail = request.getParameter("confirmEmail");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            String day = request.getParameter("birthDay");
            String month = request.getParameter("birthMonth");
            String year = request.getParameter("birthYear");

            if (!email.equals(confirmEmail)) {
                request.setAttribute("editInfoError", "Email and confirmation email do not match.");
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
                return;
            }   

            java.sql.Date sqlDateOfBirth = null;
            if (day != null && month != null && year != null) {
                try {
                    String birthDateString = year + "-" + month + "-" + day;
                    java.util.Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateString);
                    sqlDateOfBirth = new java.sql.Date(birthDate.getTime());
                } catch (ParseException e) {
                    request.setAttribute("editInfoError", "Invalid date format.");
                    request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
                    return;
                }
            }

            AdminDTO updateAdmin = new AdminDTO();
            updateAdmin.setAdminId(admin.getAdminId());
            updateAdmin.setFirstName(firstName);
            updateAdmin.setLastName(lastName);
            updateAdmin.setSex(sex);
            updateAdmin.setDateOfBirth(sqlDateOfBirth);
            updateAdmin.setEmail(email);
            updateAdmin.setPhone(phone);
            updateAdmin.setAddress(address);

            boolean isUpdated = adminDAO.update(updateAdmin);
            if (isUpdated) {
                session.setAttribute("adminUser", updateAdmin);
                request.setAttribute("adminInfo", updateAdmin);
                request.setAttribute("showProfile", true);
                request.setAttribute("editInfoMessage", "Admin information updated successfully.");
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            } else {
                request.setAttribute("editInfoError", "Update failed. Please try again.");
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            }
        } else if (action.equals("changePassword")) {
            String currentPassword = request.getParameter("current-password");
            String newPassword = request.getParameter("new-password");
            String confirmPassword = request.getParameter("confirm-password");

            if (!adminDAO.checkPassword(admin.getAdminId(), currentPassword)) {
                request.setAttribute("passwordChangeError", "Current password is incorrect.");
                request.setAttribute("adminInfo", admin);
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("passwordChangeError", "New password and confirm password do not match.");
                request.setAttribute("adminInfo", admin);
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
                return;
            }

            boolean isPasswordUpdated = adminDAO.updatePassword(admin.getAdminId(), newPassword);
            if (isPasswordUpdated) {
                request.setAttribute("passwordChangeMessage", "Password changed successfully.");
                request.setAttribute("adminInfo", admin);
                request.setAttribute("showProfile", true);
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            } else {
                request.setAttribute("passwordChangeError", "Cannot change password. Please try again.");
                request.setAttribute("adminInfo", admin);
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            }
        } else if (action.equals("deleteAccount")) {
            int adminId = Integer.parseInt(request.getParameter("adminId"));

            boolean isDeleted = adminDAO.deleteAdmin(adminId);

            if (isDeleted) {
                if (session != null) {
                    session.invalidate();
                }
                request.setAttribute("message", "Account deleted successfully.");
                request.getRequestDispatcher("/View/Home_GUI/Login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to delete account. Please try again.");
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            }
        } else if ("showProfile".equals(action)) {

            request.setAttribute("showProfile", true);
            request.setAttribute("showManager", false);
            request.setAttribute("showModerate", false);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);

        } else if ("showManager".equals(action)) {

            // Lấy danh sách Users và Owners và đặt vào session
            List<UsersDTO> userList = usersDAO.getAllUsers();
            List<OwnerDTO> ownerList = ownerDAO.getAllOwners();
            session.setAttribute("users", userList);
            session.setAttribute("owners", ownerList);

            // Đặt thuộc tính hiển thị cho Manager
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", true);
            request.setAttribute("showModerate", false);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);

        } else if ("showModerate".equals(action)) {

            // Đặt thuộc tính hiển thị cho Moderate
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", false);
            request.setAttribute("showModerate", true);

            List<ReviewDTO> pendingReviews = reviewDAO.getPendingReviews();

            // Đặt danh sách review chờ duyệt vào request attribute để sử dụng trong JSP
            session.setAttribute("pendingReviews", pendingReviews);

            // Chuyển tiếp tới trang JSP admin_setting.jsp với dữ liệu review chờ duyệt
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        } else if ("viewUserDetail".equals(action)) {

            // Xử lý hiển thị chi tiết User
            int userId = Integer.parseInt(request.getParameter("userId"));
            UsersDTO user = usersDAO.getUserById(userId);
            request.setAttribute("showDetail", user);
            request.setAttribute("showProfile", false);
            request.setAttribute("isUser", true);
            request.setAttribute("showManager", true);
            request.setAttribute("showModerate", false);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);

        } else if ("viewOwnerDetail".equals(action)) {

            // Xử lý hiển thị chi tiết Owner
            int ownerId = Integer.parseInt(request.getParameter("ownerId"));
            OwnerDTO owner = ownerDAO.getOwnerById(ownerId);
            request.setAttribute("showDetail", owner);
            request.setAttribute("isUser", false);
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", true);
            request.setAttribute("showModerate", false);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);

        } else if ("deleteUser".equals(action)) {

            // Xử lý xóa User
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean isDeleted = usersDAO.deleteUser(userId);

            if (isDeleted) {
                // Làm mới danh sách sau khi xóa
                List<UsersDTO> userList = usersDAO.getAllUsers();
                session.setAttribute("users", userList);
                request.setAttribute("showProfile", false);
                request.setAttribute("showManager", true);
                request.setAttribute("showModerate", false);
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to delete user.");
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            }

        } else if ("deleteOwner".equals(action)) {

            // Xử lý xóa Owner
            int ownerId = Integer.parseInt(request.getParameter("ownerId"));
            boolean isDeleted = ownerDAO.deleteOwner(ownerId);

            if (isDeleted) {
                // Làm mới danh sách sau khi xóa
                List<OwnerDTO> ownerList = ownerDAO.getAllOwners();
                session.setAttribute("owners", ownerList);
                request.setAttribute("showProfile", false);
                request.setAttribute("showManager", true);
                request.setAttribute("showModerate", false);
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to delete owner.");
                request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
            }
        } else if ("searchAccount".equals(action)) {
            String keyword = request.getParameter("keyword");

            // Gọi DAO để tìm kiếm User và Owner
            List<UsersDTO> userList = adminDAO.searchUsers(keyword);
            List<OwnerDTO> ownerList = adminDAO.searchOwners(keyword);

            // Đưa danh sách vào session hoặc request để hiển thị trong JSP
            session.setAttribute("users", userList);
            session.setAttribute("owners", ownerList);
            request.setAttribute("showManager", true);

            // Forward đến trang admin_setting.jsp hoặc trang quản lý tương ứng
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        } else if ("viewReviewDetail".equals(action)) {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));

            // Giả sử có ReviewDAO với phương thức getReviewDetail(int reviewId)
            ReviewDTO review = reviewDAO.getReviewDetail(reviewId);

            // Đặt thuộc tính để JSP sử dụng
            request.setAttribute("reviewDetail", review);
            request.setAttribute("showReviewModal", true); // Để hiển thị modal
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", false);
            request.setAttribute("showModerate", true);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        } else if ("acceptReview".equals(action)) {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));

            // Cập nhật trạng thái review thành "A" (Accepted)
            boolean isAccepted = reviewDAO.acceptReview(reviewId);

            // Tính lại point cho nhà hàng sau khi chấp nhận review
            boolean pointCalculated = restaurantDAO.calculatePoint(restaurantId);

            // Thiết lập thông báo và chuyển đến trang quản lý đánh giá
            if (isAccepted && pointCalculated) {
                request.setAttribute("message", "Review đã được chấp nhận thành công và điểm của nhà hàng đã được cập nhật.");
            } else if (isAccepted) {
                request.setAttribute("message", "Review đã được chấp nhận thành công, nhưng không thể cập nhật điểm của nhà hàng.");
            } else {
                request.setAttribute("error", "Không thể chấp nhận review.");
            }

            // Làm mới danh sách review đang chờ
            List<ReviewDTO> pendingReviews = reviewDAO.getPendingReviews();
            session.setAttribute("pendingReviews", pendingReviews);

            // Thiết lập các thuộc tính hiển thị trang điều hướng
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", false);
            request.setAttribute("showModerate", true);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        } else if ("rejectReview".equals(action)) {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));

            // Delete the review from the database using the DAO
            boolean isRejected = reviewDAO.rejectReview(reviewId);

            // Set appropriate message and forward to the moderation page
            if (isRejected) {
                request.setAttribute("message", "Review rejected successfully.");
            } else {
                request.setAttribute("error", "Failed to reject the review.");
            }

            // Refresh the list of pending reviews for the moderation page
            List<ReviewDTO> pendingReviews = reviewDAO.getPendingReviews();
            session.setAttribute("pendingReviews", pendingReviews);
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", false);
            request.setAttribute("showModerate", true);
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        } else if ("searchReviews".equals(action)) {
            String searchKeyword = request.getParameter("searchKeyword");

            // Call DAO method to search reviews by user's full name
            List<ReviewDTO> filteredReviews = reviewDAO.searchReviewsByFullName(searchKeyword);

            // Set the filtered reviews in session scope to display in JSP
            session.setAttribute("pendingReviews", filteredReviews);

            // Keep other attributes to display Moderate section properly
            request.setAttribute("showProfile", false);
            request.setAttribute("showManager", false);
            request.setAttribute("showModerate", true);

            // Forward back to the JSP page
            request.getRequestDispatcher("/View/Admin_GUI/admin_setting.jsp").forward(request, response);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
