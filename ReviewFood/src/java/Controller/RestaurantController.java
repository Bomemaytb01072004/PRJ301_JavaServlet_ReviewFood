package Controller;

import Model.restaurant.RestaurantDAO;
import Model.restaurant.RestaurantDTO;
import Model.restaurantMenu.RestaurantMenuDAO;
import Model.restaurantMenu.RestaurantMenuDTO;
import Model.review.ReviewDAO;
import Model.review.ReviewDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RestaurantController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String restaurantIdStr = request.getParameter("restaurantId");
        String action = request.getParameter("action");

        // Kiểm tra nếu action là submitReview
        if ("submitReview".equals(action)) {
            handleReviewSubmission(request, response, session);
            return;
        }

        // Kiểm tra nếu không có restaurantId, báo lỗi và dừng xử lý
        if (restaurantIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Restaurant ID is required.");
            return;
        }

        try {
            int restaurantId = Integer.parseInt(restaurantIdStr);

            // Lấy thông tin nhà hàng
            RestaurantDAO restaurantDAO = new RestaurantDAO();
            RestaurantDTO restaurant = restaurantDAO.getOneRestaurant(restaurantId);
            if (restaurant == null) {
                System.out.println("Restaurant object is null for restaurantId: " + restaurantId);
                session.setAttribute("errorMessage", "Restaurant not found.");
                forwardToErrorPage(request, response);
                return;
            }
            session.setAttribute("restaurant", restaurant);

            // Lấy danh sách menu
            RestaurantMenuDAO menuDAO = new RestaurantMenuDAO();
            List<RestaurantMenuDTO> menuList = menuDAO.getMenuByRestaurantId(restaurantId);
            if (menuList == null || menuList.isEmpty()) {
                System.out.println("No menu items found for restaurantId: " + restaurantId);
                session.setAttribute("errorMessage", "No menu items found.");
            } else {
                session.setAttribute("menuList", menuList);
                setCategoriesAndCuisines(menuList, session);
            }

            // Lấy danh sách review và tính điểm trung bình
            ReviewDAO reviewDAO = new ReviewDAO();
            List<ReviewDTO> reviewList = reviewDAO.getReviewById(restaurantId);
            session.setAttribute("commentList", reviewList != null ? reviewList : new ArrayList<>());
            setAverageRatings(reviewList, session, restaurantId);

            // Lấy số điện thoại của chủ nhà hàng
            String ownerPhone = restaurantDAO.getPhoneRestaurant(restaurantId);
            session.setAttribute("ownerPhone", ownerPhone != null ? ownerPhone : "Phone number not found.");

            // Lấy danh sách bình luận từ `getInfoUser` method
            List<Object[]> commentList = restaurantDAO.getInfoUser(restaurantId);
            if (commentList == null || commentList.isEmpty()) {
                System.out.println("No comments found for restaurantId: " + restaurantId);
                session.setAttribute("errorMessage", "No comments found.");
            }
            session.setAttribute("commentList", commentList);

            // Chuyển tiếp đến trang hồ sơ nhà hàng
            RequestDispatcher dispatcher = request.getRequestDispatcher("/View/Restaurant_GUI/restaurant_profile.jsp");
            dispatcher.forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid restaurant ID.");
        }
    }

    // Hàm xử lý việc nộp review
    private void handleReviewSubmission(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        try {
            String userIdStr = request.getParameter("userId");
            String restaurantIdStr = request.getParameter("restaurantId");

            if (userIdStr == null || userIdStr.isEmpty() || restaurantIdStr == null || restaurantIdStr.isEmpty()) {
                session.setAttribute("errorMessage", "Invalid User ID or Restaurant ID.");
                response.sendRedirect(request.getContextPath() + "/error.jsp");
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            int restaurantId = Integer.parseInt(restaurantIdStr);
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String picture = request.getParameter("picture");

            int positionRating = Integer.parseInt(request.getParameter("positionRating"));
            int priceRating = Integer.parseInt(request.getParameter("priceRating"));
            int qualityRating = Integer.parseInt(request.getParameter("qualityRating"));
            int serviceRating = Integer.parseInt(request.getParameter("serviceRating"));
            int spaceRating = Integer.parseInt(request.getParameter("spaceRating"));

            float totalPoint = (positionRating + priceRating + qualityRating + serviceRating + spaceRating) / 5.0f;

            ReviewDTO review = new ReviewDTO();
            review.setTitle(title);
            review.setContent(content);
            review.setPicture(picture);
            review.setStatus("W");
            review.setUserId(userId);
            review.setRestaurantId(restaurantId);
            review.setPositionRating(positionRating);
            review.setPriceRating(priceRating);
            review.setQualityRating(qualityRating);
            review.setServiceRating(serviceRating);
            review.setSpaceRating(spaceRating);
            review.setTotalPoint(totalPoint);

            ReviewDAO reviewDAO = new ReviewDAO();
            boolean isSaved = reviewDAO.saveReview(review);

            if (isSaved) {
                List<ReviewDTO> reviewList = reviewDAO.getReviewById(restaurantId);
                setAverageRatings(reviewList, session, restaurantId);
            }

            session.setAttribute("message", isSaved ? "Review submitted successfully." : "Error submitting review.");
            response.sendRedirect(request.getContextPath() + "/RestaurantController?restaurantId=" + restaurantId);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
        }
    }

    // Hàm thiết lập danh mục và món ăn vào session
    private void setCategoriesAndCuisines(List<RestaurantMenuDTO> menuList, HttpSession session) {
        Set<String> categories = new HashSet<>();
        Set<String> cuisines = new HashSet<>();

        for (RestaurantMenuDTO menu : menuList) {
            categories.add(menu.getCategory());
            cuisines.add(menu.getCuisine());
        }

        session.setAttribute("categories", new ArrayList<>(categories));
        session.setAttribute("cuisines", new ArrayList<>(cuisines));
    }

    // Hàm tính điểm đánh giá trung bình
    private void setAverageRatings(List<ReviewDTO> reviewList, HttpSession session, int restaurantId) {
        float totalPositionRating = 0, totalPriceRating = 0, totalQualityRating = 0;
        float totalServiceRating = 0, totalSpaceRating = 0;
        float totalAverage = 0;

        for (ReviewDTO review : reviewList) {
            totalPositionRating += review.getPositionRating();
            totalPriceRating += review.getPriceRating();
            totalQualityRating += review.getQualityRating();
            totalServiceRating += review.getServiceRating();
            totalSpaceRating += review.getSpaceRating();
        }

        int reviewCount = reviewList.size();
        if (reviewCount > 0) {
            float avgPositionRating = totalPositionRating / reviewCount;
            float avgPriceRating = totalPriceRating / reviewCount;
            float avgQualityRating = totalQualityRating / reviewCount;
            float avgServiceRating = totalServiceRating / reviewCount;
            float avgSpaceRating = totalSpaceRating / reviewCount;

            totalAverage = (avgPositionRating + avgPriceRating + avgQualityRating + avgServiceRating + avgSpaceRating) / 5.0f;

            session.setAttribute("avgPositionRating", String.format("%.1f", avgPositionRating));
            session.setAttribute("avgPriceRating", String.format("%.1f", avgPriceRating));
            session.setAttribute("avgQualityRating", String.format("%.1f", avgQualityRating));
            session.setAttribute("avgServiceRating", String.format("%.1f", avgServiceRating));
            session.setAttribute("avgSpaceRating", String.format("%.1f", avgSpaceRating));
            session.setAttribute("totalAverage", String.format("%.1f", totalAverage));
        } else {
            session.setAttribute("avgPositionRating", "0.0");
            session.setAttribute("avgPriceRating", "0.0");
            session.setAttribute("avgQualityRating", "0.0");
            session.setAttribute("avgServiceRating", "0.0");
            session.setAttribute("avgSpaceRating", "0.0");
            session.setAttribute("totalAverage", "0.0");
        }

        RestaurantDAO resDAO = new RestaurantDAO();
        if (resDAO.calculatePoint(restaurantId)) {
            RestaurantDTO restaurant = resDAO.getOneRestaurant(restaurantId);
            session.setAttribute("restaurantPoint", restaurant.getPoint());
        } else {
            session.setAttribute("restaurantPoint", "0.0");
        }
    }

    // Hàm phụ để chuyển hướng đến trang lỗi
    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
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
