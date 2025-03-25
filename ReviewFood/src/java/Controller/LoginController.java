package Controller;

import Model.Admin.AdminDTO;
import Model.Owner.OwnerDTO;
import Model.Owner.OwnerDAO;
import Model.users.UsersDAO;
import Model.users.UsersDTO;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        HttpSession session = null;

        UsersDAO userDao = new UsersDAO();
        OwnerDAO ownerDao = new OwnerDAO();

        switch (action == null ? "login" : action) {
            case "login":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String roleSignIn = request.getParameter("roleSignIn");

                session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                if ("user".equalsIgnoreCase(roleSignIn)) {
                    UsersDTO user = userDao.login(username, password);
                    if (user != null) {
                        if (user.getRoleId() == 1) { 
                            System.out.println("Admin Login");
                            AdminDTO admin = new AdminDTO();

                            // Thiết lập các thuộc tính của AdminDTO từ UsersDTO
                            admin.setAdminId(user.getUserId());
                            admin.setAdminName(user.getUserName());
                            admin.setPassword(user.getPassword()); // Lưu ý: có thể mã hóa nếu cần thiết
                            admin.setFirstName(user.getFirstName());
                            admin.setLastName(user.getLastName());
                            admin.setSex(user.getSex());
                            admin.setDateOfBirth(user.getDateOfBirth());
                            admin.setPhone(user.getPhone());
                            admin.setEmail(user.getEmail());
                            admin.setAddress(user.getAddress());
                            admin.setPicture(user.getPicture());
                            admin.setRoleId(user.getRoleId());

                            // Thiết lập session cho admin và điều hướng đến HomeController
                            session = request.getSession(true);
                            session.setAttribute("adminSession", admin);
                            response.sendRedirect(request.getContextPath() + "/HomeController");
                        } else {
                            // Nếu không phải admin, thiết lập session cho user thông thường
                            session = request.getSession(true);
                            session.setAttribute("userSession", user);
                            response.sendRedirect(request.getContextPath() + "/HomeController");
                        }
                    } else {
                        redirectToLoginWithError(request, response, "Username or password is incorrect for User.");
                    }
                } else if ("owner".equalsIgnoreCase(roleSignIn)) {
                    OwnerDTO owner = ownerDao.login(username, password);
                    if (owner != null) {
                        session = request.getSession(true);
                        session.setAttribute("ownerSession", owner);
                        response.sendRedirect(request.getContextPath() + "/HomeController");
                    } else {
                        redirectToLoginWithError(request, response, "Username or password is incorrect for Owner.");
                    }
                } else {
                    redirectToLoginWithError(request, response, "Please select a valid role.");
                }

                break;

            case "register":
                String usernameSignUp = request.getParameter("usernameSignUp");
                String passwordSignUp = request.getParameter("passwordSignUp");
                String firstNameSignUp = request.getParameter("firstNameSignUp");
                String lastNameSignUp = request.getParameter("lastNameSignUp");
                String sexSignUp = request.getParameter("sexSignUp");
                String dateOfBirthSignUp = request.getParameter("dateOfBirthSignUp");
                String phoneSignUp = request.getParameter("phoneSignUp");
                String emailSignUp = request.getParameter("emailSignUp");
                String addressSignUp = request.getParameter("addressSignUp");
                String roleSignUp = request.getParameter("roleSignUp");

                java.sql.Date sqlDateOfBirth = parseDateOfBirth(request, response, dateOfBirthSignUp);
                if (sqlDateOfBirth == null) {
                    return; // Kết thúc nếu ngày không hợp lệ
                }
                boolean isRegistered;
                if ("user".equalsIgnoreCase(roleSignUp)) {
                    UsersDTO newUser = createUserDTO(usernameSignUp, passwordSignUp, firstNameSignUp, lastNameSignUp, sexSignUp, sqlDateOfBirth, phoneSignUp, emailSignUp, addressSignUp, 3);
                    isRegistered = userDao.register(newUser);
                } else if ("owner".equalsIgnoreCase(roleSignUp)) {
                    OwnerDTO newOwner = createOwnerDTO(usernameSignUp, passwordSignUp, firstNameSignUp, lastNameSignUp, sexSignUp, sqlDateOfBirth, phoneSignUp, emailSignUp, addressSignUp, 2);
                    isRegistered = ownerDao.register(newOwner);
                } else {
                    redirectToLoginWithError(request, response, "Please select a valid role for registration.");
                    return;
                }

                if (isRegistered) {
                    invalidateSession(request);
                    request.setAttribute("message", "Create Account Successfully");
                    RequestDispatcher rd = request.getRequestDispatcher("/View/Home_GUI/Login.jsp");
                rd.forward(request, response);
                } else {
                    redirectToLoginWithError(request, response, "Registration failed. Please try again.");
                }
                break;

            case "logout":
                if (session != null) {
                    session.invalidate(); // Hủy session để đăng xuất người dùng
                }
                request.setAttribute("message", "Logout Successfully"); // Chuyển thông báo qua request
                RequestDispatcher rd = request.getRequestDispatcher("/View/Home_GUI/Login.jsp");
                rd.forward(request, response);
                break;

            default:
                redirectToLoginWithError(request, response, "Invalid action");
                break;
        }
    }

    private void redirectToLoginWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        RequestDispatcher rd = request.getRequestDispatcher("/View/Home_GUI/Login.jsp");
        rd.forward(request, response);
    }

    private java.sql.Date parseDateOfBirth(HttpServletRequest request, HttpServletResponse response, String dateOfBirthSignUp) throws ServletException, IOException {
        if (dateOfBirthSignUp == null || dateOfBirthSignUp.trim().isEmpty()) {
            redirectToLoginWithError(request, response, "Date of Birth is required.");
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = sdf.parse(dateOfBirthSignUp);
            return new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            redirectToLoginWithError(request, response, "Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }

    private UsersDTO createUserDTO(String username, String password, String firstName, String lastName, String sex, java.sql.Date dateOfBirth, String phone, String email, String address, int roleId) {
        UsersDTO user = new UsersDTO();
        user.setUserName(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setSex(sex);
        user.setDateOfBirth(dateOfBirth);
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);
        user.setRoleId(roleId);
        return user;
    }

    private OwnerDTO createOwnerDTO(String ownerName, String password, String firstName, String lastName, String sex, java.sql.Date dateOfBirth, String phone, String email, String address, int roleId) {
        OwnerDTO owner = new OwnerDTO();
        owner.setOwnerName(ownerName);
        owner.setPassword(password);
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setSex(sex);
        owner.setDateOfBirth(dateOfBirth);
        owner.setPhone(phone);
        owner.setEmail(email);
        owner.setAddress(address);
        owner.setRoleId(roleId);
        return owner;
    }

    private void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles user and owner login and registration";
    }
}
