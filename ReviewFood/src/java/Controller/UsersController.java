/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Model.users.UsersDAO;
import Model.users.UsersDTO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class UsersController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Kiểm tra session người dùng
        HttpSession session = request.getSession(false);
        UsersDTO userSession = (UsersDTO) session.getAttribute("userSession"); // Ép kiểu về UsersDTO

        // Nếu session không tồn tại hoặc không có người dùng trong session, chuyển hướng về login
        if (session == null || userSession == null) {
            response.sendRedirect(request.getContextPath() + "/View/Home_GUI/Login.jsp");
            return;
        }

        // Xử lý các hành động trong Controller
        String action = request.getParameter("action");
        UsersDAO usersDAO = new UsersDAO();

        if (action == null || action.equals("showInfo")) {
            // Ví dụ về việc lấy danh sách người dùng, cần xác định rõ mục đích của hàm này
            UsersDTO userInfo = usersDAO.load(userSession.getUserName(), userSession.getPassword());
            request.setAttribute("userInfo", userInfo);
            request.getRequestDispatcher("/View/User_GUI/user_setting.jsp").forward(request, response);
        } 
        
        else if (action.equals("updateUserInfo")) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String sex = request.getParameter("sex");
            String email = request.getParameter("email");
            String confirmEmail = request.getParameter("confirmEmail");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            // Check for date of birth from dropdown fields
            String day = request.getParameter("birthDay");
            String month = request.getParameter("birthMonth");
            String year = request.getParameter("birthYear");

            // Validate email and confirm email
            if (!email.equals(confirmEmail)) {
                request.setAttribute("editInfoError", "Email and confirmation email do not match.");
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
                return;
            }

            // Convert birth date to java.sql.Date if available
            java.sql.Date sqlDateOfBirth = null;
            if (day != null && month != null && year != null) {
                try {
                    String birthDateString = year + "-" + month + "-" + day;
                    java.util.Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateString);
                    sqlDateOfBirth = new java.sql.Date(birthDate.getTime());
                } catch (ParseException e) {
                    request.setAttribute("editInfoError", "Invalid date format.");
                    RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                    rd.forward(request, response);
                    return;
                }
            }

            // Update user info
            UsersDTO updateUser = new UsersDTO();
            updateUser.setUserId(userSession.getUserId());
            updateUser.setFirstName(firstName);
            updateUser.setLastName(lastName);
            updateUser.setSex(sex);
            updateUser.setDateOfBirth(sqlDateOfBirth);
            updateUser.setEmail(email);
            updateUser.setPhone(phone);
            updateUser.setAddress(address);

            boolean isUpdated = usersDAO.update(updateUser);
            if (isUpdated) {
                session.setAttribute("userSession", updateUser);
                request.setAttribute("userInfo", updateUser);
                request.setAttribute("editInfoMessage", "User information updated successfully.");
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("editInfoError", "Update failed. Please try again.");
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
            }
        } 
        
        else if (action.equals("changePassword")) {
            // Xử lý đổi mật khẩu
            String currentPassword = request.getParameter("current-password");
            String newPassword = request.getParameter("new-password");
            String confirmPassword = request.getParameter("confirm-password");

            // Kiểm tra mật khẩu hiện tại có đúng không
            if (!usersDAO.checkPassword(userSession.getUserId(), currentPassword)) {
                request.setAttribute("passwordChangeError", "Current password is incorrect.");
                request.setAttribute("userInfo", userSession);  // Cập nhật lại userInfo
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
                return;
            }

            // Kiểm tra mật khẩu mới và xác nhận mật khẩu có khớp không
            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("passwordChangeError", "New password and confirm password do not match.");
                request.setAttribute("userInfo", userSession);  // Cập nhật lại userInfo
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
                return;
            }

            // Cập nhật mật khẩu
            boolean isPasswordUpdated = usersDAO.updatePassword(userSession.getUserId(), newPassword);
            if (isPasswordUpdated) {
                request.setAttribute("passwordChangeMessage", "Password changed successfully.");
                request.setAttribute("userInfo", userSession);  // Cập nhật lại userInfo
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("passwordChangeError", "Cannot change password. Please try again");
                request.setAttribute("userInfo", userSession);
                RequestDispatcher rd = request.getRequestDispatcher("/View/User_GUI/user_setting.jsp");
                rd.forward(request, response);
            }
        }
        
        else if (action.equals("deleteAccount")) {
            // Lấy userId từ request
            int userId = Integer.parseInt(request.getParameter("userId"));

            boolean isDeleted = usersDAO.deleteUser(userId);

            if (isDeleted) {
                // Hủy session sau khi xóa tài khoản và lưu thông báo
                if (session != null) {
                    session.invalidate();
                }
                request.setAttribute("message", "Account deleted successfully."); // Chuyển thông báo qua request
                RequestDispatcher rd = request.getRequestDispatcher("/View/Home_GUI/Login.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("error", "Failed to delete account. Please try again.");
                request.getRequestDispatcher("/View/User_GUI/user_setting.jsp").forward(request, response);
            }
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
