package Controller;

import Model.Owner.OwnerDAO;
import Model.Owner.OwnerDTO;
import Model.restaurant.RestaurantDAO;
import Model.restaurant.RestaurantDTO;
import Model.restaurantMenu.RestaurantMenuDAO;
import Model.restaurantMenu.RestaurantMenuDTO;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OwnerController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Kiểm tra session của owner
        HttpSession session = request.getSession(false);
        OwnerDTO ownerSession = (OwnerDTO) session.getAttribute("ownerSession");

        // Nếu session không tồn tại hoặc không có owner trong session, chuyển hướng về login
        if (session == null || ownerSession == null) {
            response.sendRedirect(request.getContextPath() + "/View/Home_GUI/Login.jsp");
            return;
        }

        // Xử lý các hành động trong Controller
        String action = request.getParameter("action");
        OwnerDAO ownerDAO = new OwnerDAO();
        RestaurantDAO restaurantDAO = new RestaurantDAO();
        RestaurantMenuDAO restaurantMenuDAO = new RestaurantMenuDAO();

        if (action == null || action.equals("showProfile")) {
            OwnerDTO ownerInfo = ownerDAO.load(ownerSession.getOwnerName(), ownerSession.getPassword());
            request.setAttribute("ownerInfo", ownerInfo);
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        } 
        
        else if (action.equals("updateOwnerInfo")) {
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
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
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
                    RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                    rd.forward(request, response);
                    return;
                }
            }

            OwnerDTO updateOwner = new OwnerDTO();
            updateOwner.setOwnerId(ownerSession.getOwnerId());
            updateOwner.setFirstName(firstName);
            updateOwner.setLastName(lastName);
            updateOwner.setSex(sex);
            updateOwner.setDateOfBirth(sqlDateOfBirth);
            updateOwner.setEmail(email);
            updateOwner.setPhone(phone);
            updateOwner.setAddress(address);

            boolean isUpdated = ownerDAO.update(updateOwner);
            if (isUpdated) {
                session.setAttribute("ownerSession", updateOwner);
                request.setAttribute("ownerInfo", updateOwner);
                request.setAttribute("editInfoMessage", "Owner information updated successfully.");
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("editInfoError", "Update failed. Please try again.");
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
            }
        } 
        
        else if (action.equals("changePassword")) {
            String currentPassword = request.getParameter("current-password");
            String newPassword = request.getParameter("new-password");
            String confirmPassword = request.getParameter("confirm-password");

            if (!ownerDAO.checkPassword(ownerSession.getOwnerId(), currentPassword)) {
                request.setAttribute("passwordChangeError", "Current password is incorrect.");
                request.setAttribute("ownerInfo", ownerSession);
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                request.setAttribute("passwordChangeError", "New password and confirm password do not match.");
                request.setAttribute("ownerInfo", ownerSession);
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
                return;
            }

            boolean isPasswordUpdated = ownerDAO.updatePassword(ownerSession.getOwnerId(), newPassword);
            if (isPasswordUpdated) {
                request.setAttribute("passwordChangeMessage", "Password changed successfully.");
                request.setAttribute("ownerInfo", ownerSession);
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("passwordChangeError", "Cannot change password. Please try again.");
                request.setAttribute("ownerInfo", ownerSession);
                RequestDispatcher rd = request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp");
                rd.forward(request, response);
            }
        } 
        
        else if (action.equals("deleteAccount")) {
            int ownerId = Integer.parseInt(request.getParameter("ownerId"));

            boolean isDeleted = ownerDAO.deleteOwner(ownerId);

            if (isDeleted) {
                if (session != null) {
                    session.invalidate();
                }
                request.setAttribute("message", "Account deleted successfully."); // Chuyển thông báo qua request
                RequestDispatcher rd = request.getRequestDispatcher("/View/Home_GUI/Login.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("error", "Failed to delete account. Please try again.");
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
            }
        } 
        
        else if (action.equals("showProfile")) {
            // Đặt showManager là false để hiển thị content và ẩn manager-area
            request.setAttribute("showManager", false);

            // Điều hướng về trang owner_setting.jsp
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        } 
        
        else if (action.equals("showRestaurants")) {

            // Lấy danh sách nhà hàng theo ownerId
            List<RestaurantDTO> restaurantList = restaurantDAO.loadListOfRestaurantByOwnerID(ownerSession.getOwnerId());

            // Kiểm tra và in ra log các thông tin của danh sách nhà hàng
            if (restaurantList == null) {
                System.out.println("restaurantList bị null.");
                session.setAttribute("message", "Không thể tải danh sách nhà hàng. Vui lòng thử lại sau.");
            } else {
                System.out.println("Danh sách nhà hàng của ownerId = " + ownerSession.getOwnerId() + ":");

                // Đặt danh sách nhà hàng vào session để truyền sang giao diện
                session.setAttribute("restaurants", restaurantList);
            }

            // Đặt cờ showManager để điều khiển hiển thị phần manager-area trên giao diện
            request.setAttribute("showManager", true);

            // Điều hướng về owner_setting.jsp để đảm bảo manager-area có thể hiển thị
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);

        } 
        
        else if ("createRestaurant".equals(action)) {
            String name = request.getParameter("restaurantName");
            String address = request.getParameter("restaurantAddress");
            String avatar = request.getParameter("restaurantAvatar");

            // Đặt URL mặc định nếu avatar null hoặc rỗng
            if (avatar == null || avatar.isEmpty()) {
                avatar = "/ReviewFood/View/images/j97.jpg"; // Hình ảnh mặc định nếu không có
            }

            int ownerId = ownerSession.getOwnerId(); // Lấy ownerId từ session

            // Tạo một đối tượng nhà hàng mới
            RestaurantDTO restaurant = new RestaurantDTO();
            restaurant.setRestaurantName(name);
            restaurant.setAddress(address);
            restaurant.setPicture(avatar); // Luôn gán giá trị avatar đã được kiểm tra ở trên
            restaurant.setOwnerId(ownerId);

            // Thêm nhà hàng vào cơ sở dữ liệu
            boolean isCreated = restaurantDAO.createRestaurant(restaurant);

            if (isCreated) {
                // Nếu thêm thành công, tải lại danh sách nhà hàng cho owner này
                List<RestaurantDTO> restaurantList = restaurantDAO.loadListOfRestaurantByOwnerID(ownerId);
                session.setAttribute("restaurants", restaurantList); // Cập nhật danh sách nhà hàng trong session
                request.setAttribute("message", "Restaurant added successfully.");
                System.out.println("Restaurant added successfully");
            } else {
                request.setAttribute("error", "Failed to add restaurant.");
                System.out.println("Failed to add restaurant");
            }

            // Điều hướng về trang owner_setting.jsp
            request.setAttribute("showManager", true); // Đảm bảo hiển thị phần quản lý nhà hàng
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        }
       
        else if ("editRestaurant".equals(action)) {
            String restaurantId = request.getParameter("restaurantId");
            try {
                int id = Integer.parseInt(restaurantId);
                RestaurantDTO restaurantInfo = restaurantDAO.getRestaurantById(id);

                if (restaurantInfo != null) {
                    request.setAttribute("showManager", true);
                    request.setAttribute("restaurantInfo", restaurantInfo); // Set restaurant info to request
                    request.setAttribute("editMode", true); // Set modal display flag
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Restaurant not found.");
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid restaurant ID.");
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
            }
        }
        
        else if ("updateRestaurant".equals(action)) {
            int restaurantId = Integer.parseInt(request.getParameter("updateRestaurantId"));
            String name = request.getParameter("newRestaurantName");
            String address = request.getParameter("newRestaurantAddress");
            String avatar = request.getParameter("newRestaurantAvatar");
            int ownerId = ownerSession.getOwnerId(); // Lấy ownerId từ session

            RestaurantDTO restaurant = new RestaurantDTO(restaurantId, name, address, ownerId, avatar, 0);

            boolean isUpdated = restaurantDAO.updateRestaurant(restaurant);

            if (isUpdated) {
                request.setAttribute("message", "Restaurant updated successfully.");
                // Tải lại danh sách nhà hàng sau khi cập nhật
                List<RestaurantDTO> restaurantList = restaurantDAO.loadListOfRestaurantByOwnerID(ownerSession.getOwnerId());
                session.setAttribute("restaurants", restaurantList);
            } else {
                request.setAttribute("error", "Failed to update restaurant.");
            }
            request.setAttribute("showManager", true);
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);

        } 
        
        else if ("showDeleteRestaurant".equals(action)) {
            String restaurantId = request.getParameter("restaurantId");
            try {
                int id = Integer.parseInt(restaurantId);
                RestaurantDTO restaurantInfo = restaurantDAO.getRestaurantById(id);

                if (restaurantInfo != null) {
                    request.setAttribute("showManager", true);
                    request.setAttribute("restaurantInfo", restaurantInfo); // Gửi thông tin nhà hàng vào request
                    request.setAttribute("deleteMode", true); // Kích hoạt chế độ hiển thị modal xóa
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Restaurant not found.");
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid restaurant ID.");
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
            }
        }
        
        else if ("deleteRestaurant".equals(action)) {
            try {
                int restaurantId = Integer.parseInt(request.getParameter("deleteRestaurantId"));
                boolean isDeleted = restaurantDAO.deleteRestaurant(restaurantId);
                if (isDeleted) {
                    request.setAttribute("message", "Restaurant deleted successfully.");
                    System.out.println("Success");
                    // Tải lại danh sách nhà hàng của owner sau khi xóa
                    List<RestaurantDTO> restaurantList = restaurantDAO.loadListOfRestaurantByOwnerID(ownerSession.getOwnerId());
                    session.setAttribute("restaurants", restaurantList); // Cập nhật lại danh sách nhà hàng trong session
                } else {
                    request.setAttribute("error", "Failed to delete restaurant.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid restaurant ID format.");
            } catch (Exception e) {
                request.setAttribute("error", "An error occurred while deleting the restaurant.");
                e.printStackTrace();
            }

            // Điều hướng về owner_setting.jsp
            request.setAttribute("showManager", true);
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        }
       
        else if ("searchRestaurant".equals(action)) {
            String keyword = request.getParameter("keyword");
            int ownerId = 0;
            ownerId = Integer.parseInt(request.getParameter("ownerId"));
            try {
                // Sử dụng DAO để tìm kiếm các nhà hàng dựa trên từ khóa
                List<RestaurantDTO> restaurants = restaurantDAO.searchRestaurants(keyword,ownerId);

                // Đặt kết quả tìm kiếm vào request attribute và chuyển tiếp đến trang kết quả
                session.setAttribute("restaurants", restaurants);
                request.setAttribute("showManager", true);
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);

            } catch (Exception e) {
                log("Error at SearchController: " + e.toString());
            }
        }
        
        else if (action.equals("showRestaurantMenu")) {

            // Lấy restaurantId từ request để xác định nhà hàng cần hiển thị menu
            String restaurantIdStr = request.getParameter("restaurantId");

            if (restaurantIdStr == null || restaurantIdStr.isEmpty()) {
                System.out.println("restaurantId không được cung cấp.");
                request.setAttribute("message", "Không thể tải menu. Vui lòng chọn một nhà hàng hợp lệ.");
            } else {
                try {
                    int restaurantId = Integer.parseInt(restaurantIdStr);

                    // Lấy danh sách menu của nhà hàng dựa trên restaurantId
                    List<RestaurantMenuDTO> menuList = restaurantMenuDAO.loadMenuByRestaurantID(restaurantId);

                    // Lấy thông tin nhà hàng
                    RestaurantDTO restaurantInfo = restaurantDAO.getRestaurantById(restaurantId);

                    // Đặt thông tin nhà hàng vào session bất kể menuList có dữ liệu hay không
                    session.setAttribute("restaurantInfoToAdd", restaurantInfo);

                    if (menuList == null || menuList.isEmpty()) {
                        System.out.println("menuList bị null hoặc rỗng cho restaurantId = " + restaurantId);
                        request.setAttribute("message", "Không thể tải menu của nhà hàng. Vui lòng thử lại sau.");
                    } else {
                        // Đặt danh sách menu vào session nếu có dữ liệu
                        session.setAttribute("menuList", menuList);
                        System.out.println("Đã tải thông tin menu và nhà hàng.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi định dạng restaurantId: " + restaurantIdStr);
                    request.setAttribute("message", "restaurantId không hợp lệ.");
                }
            }

            // Đặt cờ showMenu để điều khiển hiển thị phần menu-area trên giao diện
            request.setAttribute("showMenu", true);
            request.setAttribute("showManager", true);

            // Điều hướng về owner_setting.jsp để đảm bảo menu-area có thể hiển thị
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        }
   
        else if ("showAddProductMode".equals(action)) {
            // Lấy restaurantInfoToAdd từ session để xác định nhà hàng cần thêm sản phẩm
            RestaurantDTO restaurantInfo = (RestaurantDTO) session.getAttribute("restaurantInfoToAdd");

            if (restaurantInfo != null) {
                // Thiết lập các thuộc tính để hiển thị form thêm sản phẩm
                request.setAttribute("showManager", true);
                request.setAttribute("showMenu", true);
                request.setAttribute("restaurantInfo", restaurantInfo); // Gửi thông tin nhà hàng vào request để dùng trong JSP
                request.setAttribute("addProductMode", true); // Kích hoạt chế độ hiển thị modal thêm sản phẩm
            } else {
                // Thông báo lỗi nếu không có thông tin nhà hàng trong session
                request.setAttribute("error", "Restaurant information not found. Please select a valid restaurant.");
                System.out.println("Thông tin nhà hàng không tồn tại trong session.");
            }
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        } 
        
        else if ("addProductMenu".equals(action)) {
            // Lấy thông tin về sản phẩm từ form
            String dishName = request.getParameter("productName");
            String category = request.getParameter("productCategory");
            String cuisine = request.getParameter("productCuisine");
            String picture = request.getParameter("productAvatar");

            // Đặt URL mặc định nếu picture null hoặc rỗng
            if (picture == null || picture.isEmpty()) {
                picture = "/ReviewFood/View/images/j97.jpg"; // Hình ảnh mặc định nếu không có
            }

            // Lấy restaurantInfo từ session để xác định nhà hàng cần thêm sản phẩm
            RestaurantDTO restaurantInfo = (RestaurantDTO) session.getAttribute("restaurantInfoToAdd");
            int ownerId = ownerSession.getOwnerId(); // Lấy ownerId từ session

            if (restaurantInfo != null) {
                int restaurantId = restaurantInfo.getRestaurantId();

                // Tạo một đối tượng menu mới
                RestaurantMenuDTO menu = new RestaurantMenuDTO();
                menu.setDishName(dishName);
                menu.setCategory(category);
                menu.setCuisine(cuisine);
                menu.setPicture(picture);
                menu.setRestaurantId(restaurantId);
                menu.setOwnerId(ownerId);

                // Thêm mục menu vào cơ sở dữ liệu
                boolean isCreated = restaurantMenuDAO.createMenu(menu);

                if (isCreated) {
                    // Nếu thêm thành công, tải lại danh sách menu cho nhà hàng này
                    List<RestaurantMenuDTO> menuList = restaurantMenuDAO.loadMenuByRestaurantID(restaurantId);
                    session.setAttribute("menuList", menuList); // Cập nhật danh sách menu trong session
                    request.setAttribute("message", "Menu item added successfully.");
                    System.out.println("Menu item added successfully");
                } else {
                    request.setAttribute("error", "Failed to add menu item.");
                    System.out.println("Failed to add menu item");
                }
            } else {
                request.setAttribute("error", "Restaurant information not found. Please select a valid restaurant.");
                System.out.println("Không tìm thấy thông tin nhà hàng trong session.");
            }

            // Điều hướng về trang menu quản lý
            request.setAttribute("showManager", true);
            request.setAttribute("showMenu", true); // Đảm bảo hiển thị phần quản lý menu
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        }
       
        else if ("editProductMenu".equals(action)) {
            String menuId = request.getParameter("menuId");
            try {
                int id = Integer.parseInt(menuId);
                RestaurantMenuDTO menuInfo = restaurantMenuDAO.getMenuById(id); // Fetch menu info by ID

                if (menuInfo != null) {
                    request.setAttribute("showManager", true);
                    request.setAttribute("showMenu", true);
                    request.setAttribute("menuInfo", menuInfo); // Set menu info to request
                    request.setAttribute("editProductMode", true); // Set modal display flag for edit mode
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Menu item not found.");
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid menu ID.");
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
            }
        }
        
        else if ("updateProductMenu".equals(action)) {
            int menuId = Integer.parseInt(request.getParameter("updateMenuId"));
            String dishName = request.getParameter("newProductName");
            String picture = request.getParameter("newProductAvatar");
            String category = request.getParameter("newProductCategory");
            String cuisine = request.getParameter("newProductCuisine");
            int restaurantId = Integer.parseInt(request.getParameter("restaurantId")); // Assume restaurantId is passed as a parameter
            
            if (picture == null || picture.isEmpty()) {
                picture = "/ReviewFood/View/images/j97.jpg"; // Hình ảnh mặc định nếu không có
            }

            RestaurantMenuDTO menu = new RestaurantMenuDTO();
            menu.setMenuId(menuId);
            menu.setDishName(dishName);
            menu.setPicture(picture);
            menu.setCategory(category);
            menu.setCuisine(cuisine);
            menu.setRestaurantId(restaurantId);

            boolean isUpdated = restaurantMenuDAO.updateMenu(menu);

            if (isUpdated) {
                request.setAttribute("message", "Menu item updated successfully.");

                // Reload the menu list for the restaurant after updating
                List<RestaurantMenuDTO> menuList = restaurantMenuDAO.loadMenuByRestaurantID(restaurantId);
                session.setAttribute("menuList", menuList);
            } else {
                request.setAttribute("error", "Failed to update menu item.");
            }

            request.setAttribute("showManager", true);
            request.setAttribute("showMenu", true);
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
        }
        
        else if ("showDeleteProductMode".equals(action)) {
            String menuId = request.getParameter("menuId");
            try {
                int id = Integer.parseInt(menuId);
                RestaurantMenuDTO menuInfo = restaurantMenuDAO.getMenuById(id); // Fetch menu info by ID

                if (menuInfo != null) {
                    request.setAttribute("showManager", true);
                    request.setAttribute("showMenu", true);
                    request.setAttribute("menuInfo", menuInfo); // Send menu info to request
                    request.setAttribute("deleteProductMode", true); // Activate delete modal mode
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "Menu item not found.");
                    request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid menu ID.");
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
            }
        }
 
        else if (action.equals("deleteProduct")) {
            try {
                int menuId = Integer.parseInt(request.getParameter("deleteMenuId"));
                boolean isDeleted = restaurantMenuDAO.deleteMenu(menuId); // Call deleteMenu method to delete the menu item

                if (isDeleted) {
                    request.setAttribute("message", "Menu item deleted successfully.");
                    System.out.println("Success");

                    // Reload the menu list for the restaurant after deletion
                    int restaurantId = Integer.parseInt(request.getParameter("restaurantId")); // Assume restaurantId is passed
                    List<RestaurantMenuDTO> menuList = restaurantMenuDAO.loadMenuByRestaurantID(restaurantId);
                    session.setAttribute("menuList", menuList); // Update menu list in session
                } else {
                    request.setAttribute("error", "Failed to delete menu item.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid menu ID format.");
            } catch (Exception e) {
                request.setAttribute("error", "An error occurred while deleting the menu item.");
                e.printStackTrace();
            }

// Forward to restaurant_menu.jsp
            request.setAttribute("showManager", true);
            request.setAttribute("showMenu", true);
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);

        }
        
        else if ("searchMenu".equals(action)) {
            String keyword = request.getParameter("keyword");

            int ownerId = 0;
            int restaurantId = 0;
            ownerId = Integer.parseInt(request.getParameter("ownerId"));
            restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
            System.out.println("Id nha hang la " + restaurantId);
            try {
                // Sử dụng DAO để tìm kiếm các nhà hàng dựa trên từ khóa
                List<RestaurantMenuDTO> menu = restaurantMenuDAO.searchMenu(keyword, ownerId, restaurantId);

                // Đặt kết quả tìm kiếm vào request attribute và chuyển tiếp đến trang kết quả
                session.setAttribute("menuList", menu);
                request.setAttribute("showManager", true);
                request.setAttribute("showMenu", true);
                request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);

            } catch (Exception e) {
                log("Error at SearchController: " + e.toString());
            }
        }
        
        else if ("removeSession".equals(action)) {
            // Xóa menuList khỏi session
            session.removeAttribute("menuList");
            session.removeAttribute("restaurantInfoToAdd");

            // Đặt một thông báo nếu cần thiết (tùy chọn)
            request.setAttribute("message", "Menu list has been cleared from session.");

            // Điều hướng về trang owner_setting.jsp
            request.setAttribute("showManager", true); // Giữ trạng thái hiển thị phần quản lý
            request.getRequestDispatcher("/View/Owner_GUI/owner_setting.jsp").forward(request, response);
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
