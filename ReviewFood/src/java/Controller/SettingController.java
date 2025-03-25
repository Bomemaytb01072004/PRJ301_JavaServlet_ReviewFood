package Controller;

import Model.Admin.AdminDTO;
import Model.users.UsersDTO;
import Model.Owner.OwnerDTO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SettingController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/View/Home_GUI/Login.jsp");
            return;
        }

        UsersDTO userSession = (UsersDTO) session.getAttribute("userSession");
        OwnerDTO ownerSession = (OwnerDTO) session.getAttribute("ownerSession");
        AdminDTO adminSession = (AdminDTO) session.getAttribute("adminSession");

        if (userSession != null && userSession.getRoleId() == 3) {
            response.sendRedirect(request.getContextPath() + "/UsersController");
        } else if (ownerSession != null && ownerSession.getRoleId() == 2) {
            response.sendRedirect(request.getContextPath() + "/OwnerController");
        } else if (adminSession != null && adminSession.getRoleId() == 1) {
            response.sendRedirect(request.getContextPath() + "/AdminController");
        } else {
            response.sendRedirect(request.getContextPath() + "/View/Home_GUI/Login.jsp");
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
}
