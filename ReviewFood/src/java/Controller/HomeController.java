package Controller;

import Model.Admin.AdminDTO;
import Model.Owner.OwnerDTO;
import Model.restaurant.RestaurantDAO;
import Model.restaurant.RestaurantDTO;
import Model.restaurantMenu.RestaurantMenuDAO;
import Model.restaurantMenu.RestaurantMenuDTO;
import Model.review.ReviewDAO;
import Model.users.UsersDTO;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HomeController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập header ngăn cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";
        HttpSession session = request.getSession(false);

        RestaurantDAO restaurantDAO = new RestaurantDAO();
        RestaurantMenuDAO menuDAO = new RestaurantMenuDAO();

        // Điều hướng đến trang đăng nhập hoặc xử lý đăng xuất
        if ("signIn".equals(action)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/View/Home_GUI/Login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if ("logout".equals(action)) {
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/View/Home_GUI/Home.jsp");
            return;
        }

        // Tìm kiếm nhà hàng hoặc món ăn
        if ("search".equals(action)) {
            List<RestaurantDTO> restaurantList = restaurantDAO.listRestaurant(keyword);
            List<RestaurantMenuDTO> menuList = menuDAO.listMenuFood(keyword);
            Collections.shuffle(menuList);

            request.setAttribute("menuList", menuList);
            request.setAttribute("restaurantList", restaurantList);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/View/Home_GUI/Search.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Xử lý session người dùng, lấy dữ liệu cho người dùng đăng nhập
        if (session != null) {
            UsersDTO user = (UsersDTO) session.getAttribute("userSession");
            OwnerDTO owner = (OwnerDTO) session.getAttribute("ownerSession");
            AdminDTO adminSession = (AdminDTO) session.getAttribute("adminSession");
            int roleId = 0;

            if (user != null) {
                roleId = user.getRoleId();
                request.setAttribute("displayName", user.getFirstName());
                request.setAttribute("isLoggedIn", true);
            } else if (owner != null) {
                roleId = owner.getRoleId();
                request.setAttribute("displayName", owner.getFirstName());
                request.setAttribute("isLoggedIn", true);
            } else if (adminSession != null) {
                request.setAttribute("displayName", adminSession.getFirstName());
                request.setAttribute("isLoggedIn", true);
            } else {
                request.setAttribute("isLoggedIn", false);
            }

            request.setAttribute("roleId", roleId);
        } else {
            request.setAttribute("isLoggedIn", false);
        }

        // Xem chi tiết nhà hàng
        if ("viewRestaurant".equals(action)) {
            String restaurantId = request.getParameter("restaurantId");

            if (restaurantId != null) {
                RestaurantDTO correspondingRestaurant = restaurantDAO.getRestaurantByIdHome(restaurantId);

                if (correspondingRestaurant != null) {
                    request.setAttribute("correspondingRestaurant", correspondingRestaurant);
                    request.getRequestDispatcher("/RestaurantController").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("error.jsp");
            return;
        }

        // Lấy danh sách đánh giá nhà hàng và món ăn
        ReviewDAO reviewDAO = new ReviewDAO();
        List<Object[]> reviewList = reviewDAO.getTopReviewsByTotalPoint();
        if (reviewList == null || reviewList.isEmpty()) {
            request.setAttribute("errorMessage", "No reviews restaurant found.");
        } else {
            request.setAttribute("reviewList", reviewList);
        }

        List<Object[]> reviewFoodList = menuDAO.getBestRatedDishes();
        if (reviewFoodList == null || reviewFoodList.isEmpty()) {
            request.setAttribute("errorMessage", "No reviews food found.");
        } else {
            request.setAttribute("reviewFoodList", reviewFoodList);
        }

        // Kiểm tra keyword và thiết lập mặc định nếu cần
        if ("null".equals(keyword) || keyword.isEmpty()) {
            keyword = null;
        }

        List<RestaurantDTO> restaurantList = restaurantDAO.listRestaurant(keyword);
        List<RestaurantMenuDTO> menuList = menuDAO.listMenuFood(keyword);
        Collections.shuffle(menuList);

        if (menuList == null || menuList.isEmpty() || restaurantList == null || restaurantList.isEmpty()) {
            request.setAttribute("errorMessage", "No menu items found.");
        } else {
            request.setAttribute("menuList", menuList);
            request.setAttribute("restaurantList", restaurantList);
        }

        // Phân trang và lọc dữ liệu
        String sort = request.getParameter("sort");
        String category = request.getParameter("category");
        String address = request.getParameter("address");
        int itemsPerPage = (category != null && !category.equals("all") || address != null && !address.equals("all")) ? 40 : 100;
        int page = 1;

        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<RestaurantDTO> restaurantListpagination = restaurantDAO.listRestaurant(keyword);
        boolean newestFirst = "newest".equals(sort);
        List<RestaurantMenuDTO> menuListpagination = menuDAO.listMenuFoodWithPagination(page, itemsPerPage, newestFirst, category, address);

        if (menuListpagination == null || menuListpagination.isEmpty() || restaurantListpagination == null || restaurantListpagination.isEmpty()) {
            request.setAttribute("errorMessage", "No menu items found.");
        } else {
            request.setAttribute("menuListpagination", menuListpagination);
            request.setAttribute("restaurantListpagination", restaurantListpagination);
            request.setAttribute("category", category);
            request.setAttribute("address", address);
            request.setAttribute("currentPage", page);
        }

        // Điều hướng đến trang Home
        RequestDispatcher dispatcher = request.getRequestDispatcher("/View/Home_GUI/Home.jsp");
        dispatcher.forward(request, response);
    }

    // Phương thức doGet và doPost để xử lý yêu cầu
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
        return "HomeController Servlet";
    }
}
