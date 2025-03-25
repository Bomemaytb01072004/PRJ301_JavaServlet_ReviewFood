    <%@page import="Model.restaurantMenu.RestaurantMenuDTO"%>
<%@page import="java.lang.Integer"%>
    <%@page import="java.util.Map"%>
    <%@page import="java.util.HashMap"%>
    <%@page import="java.util.List"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="/ReviewFood/CSS/home.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
            <link rel="preconnect" href="https://fonts.googleapis.com">
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
            <link href="https://fonts.googleapis.com/css2?family=Pacifico&family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet">

            <title>Food Review</title>

        </head>

        <body>

            <div>


                <header>
                    <nav class="header-top row1">
                        <div class="logo">
                            <a href="<%= request.getContextPath()%>/HomeController">
                                <img src="/ReviewFood/View/images/logo.png" alt="Logo">
                            </a>
                        </div>




                        <div class="user-button">
                            <%
                                Boolean isLoggedIn = (Boolean) request.getAttribute("isLoggedIn");
                                String displayName = (String) request.getAttribute("displayName");

                                if (isLoggedIn != null && isLoggedIn) {
                            %>
                            <form action="<%= request.getContextPath()%>/SettingController" method="post" style="display:inline;">
                                <button type="submit" name="action" value="settings"><%= displayName%></button>
                            </form>

                            <form action="<%= request.getContextPath()%>/LoginController" method="post" style="display:inline;">
                                <button type="submit" name="action" value="logout">Logout</button>
                            </form>
                            <%
                            } else {
                            %>
                            <form action="<%= request.getContextPath()%>/HomeController" method="post">
                                <button type="submit" name="action" value="signIn">Sign in</button>
                            </form>
                            <%
                                }
                            %>
                        </div>



                    </nav>
                </header>


                <section class="search-bar">
                    <div class="overlay"></div>
                    <div class="search-container">
                        <h1>Find your perfect restaurant</h1>
                        <form action="/ReviewFood/HomeController" method="GET" class="form-search">
                            <input type="hidden" name="action" value="search">
                            <label for="search">
                                <input required autocomplete="off" placeholder="Search restaurants, foods ..." id="search"
                                       name="keyword" type="text" 
                                       value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>"
                                       onkeyup="showSuggestions()">
                                <div class="icon">
                                    <svg stroke-width="2" stroke="currentColor" viewBox="0 0 24 24" fill="none"
                                         xmlns="http://www.w3.org/2000/svg" class="swap-on">
                                    <path d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" stroke-linejoin="round"
                                          stroke-linecap="round"></path>
                                    </svg>
                                    <svg stroke-width="2" stroke="currentColor" viewBox="0 0 24 24" fill="none"
                                         xmlns="http://www.w3.org/2000/svg" class="swap-off">
                                    <path d="M10 19l-7-7m0 0l7-7m-7 7h18" stroke-linejoin="round" stroke-linecap="round">
                                    </path>
                                    </svg>
                                </div>
                                <button type="reset" class="close-btn" aria-label="Clear search">
                                    <svg viewBox="0 0 20 20" class="h-5 w-5" xmlns="http://www.w3.org/2000/svg">
                                    <path clip-rule="evenodd"
                                          d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                          fill-rule="evenodd"></path>
                                    </svg>
                                </button>
                            </label>
                            <ul id="suggestions" class="suggestions"></ul>
                        </form>
                    </div>
                </section>



                <div class="slideshow-title reveal" id="restaurants">
                    <div class="scrolling-content">
                        <span class="stars">⭐⭐⭐</span>
                        <span class="text">Best Rated Restaurants</span>
                        <span class="stars">⭐⭐⭐</span>
                    </div>
                </div>

                <div class="restaurant-slideshow-wrapper reveal">

                    <div class="prev-container">
                        <a class="prev" onclick="plusSlides(-1)"></a>
                    </div>

                    <div class="slideshow-container">
                        <%
                            // Lấy reviewList và restaurantList từ request
                            List<Object[]> reviewList = (List<Object[]>) request.getAttribute("reviewList");
                            List<Model.restaurant.RestaurantDTO> restaurantList = (List<Model.restaurant.RestaurantDTO>) request.getAttribute("restaurantList");

                            // Kiểm tra null hoặc rỗng
                            if (reviewList == null || reviewList.isEmpty()) {
                                out.println("<p>No reviews found.</p>");
                            } else if (restaurantList == null || restaurantList.isEmpty()) {
                                out.println("<p>No restaurants found.</p>");
                            } else {
                                int count = 0;

                                // Duyệt qua từng phần tử trong reviewList
                                for (int i = 0; i < reviewList.size(); i++) {
                                    Object[] reviewData = reviewList.get(i);
                                    int restaurantId = (int) reviewData[1];
                                    float totalPoint = ((float) reviewData[4] / 2) + 0.5f;

                                    // Tìm nhà hàng tương ứng
                                    Model.restaurant.RestaurantDTO restaurant = null;
                                    boolean found = false;
                                    for (Model.restaurant.RestaurantDTO rest : restaurantList) {
                                        if (rest.getRestaurantId() == restaurantId) {
                                            restaurant = rest;
                                            found = true;
                                            break;
                                        }
                                    }

                                    if (restaurant == null) {
                                        continue;
                                    }

                                    if (count % 8 == 0) {
                        %>
                        <div class="slide <%= count == 0 ? "active" : ""%>">
                            <div class="rating-grid">
                                <%
                                    }

                                %>
                                <div class="card">
                                    <a href="HomeController?action=viewRestaurant&restaurantId=<%= restaurant.getRestaurantId()%>"class="restaurant-link">
                                        <img src="<%= request.getContextPath()%>/Avatar Restaurant/Avatar(<%= restaurant.getRestaurantId()%>).jpg" alt="<%= restaurant.getRestaurantName()%>">
                                        <h3><%= restaurant.getRestaurantName()%></h3>
                                    </a>
                                    <p>Rating: 
                                        <%
                                            // Hiển thị số sao dựa trên totalPoint
                                            int stars = Math.round(totalPoint);
                                            for (int star = 1; star <= 5; star++) {
                                                if (star <= stars) {
                                        %>
                                        &#9733; 
                                        <%
                                        } else {
                                        %>
                                        &#9734;
                                        <%
                                                }
                                            }
                                        %>
                                    </p>
                                </div>

                                <%
                                    count++;
                                    if (count % 8 == 0 || i == reviewList.size() - 1) {
                                %>
                            </div>
                        </div>
                        <%
                                    }
                                }
                            }
                        %>
                    </div>

                    <div class="next-container">
                        <a class="next" onclick="plusSlides(1)"></a>
                    </div>
                </div>

                <div class="slideshow-title reveal" id="foods">
                    <div class="scrolling-content">
                        <span class="stars">⭐⭐⭐</span>
                        <span class="text">Best Rated Foods</span>
                        <span class="stars">⭐⭐⭐</span>
                    </div>
                </div>

                <div class="food-slideshow-container reveal">
                    <div class="prev-container">
                        <a class="prev" onclick="plusSlides2(-1)"></a>
                    </div>

                    <div class="food-container">
                        <%
                            List<RestaurantMenuDTO> menuList = (List<RestaurantMenuDTO>) request.getAttribute("menuList");
                            List<Object[]> reviewFoodList = (List<Object[]>) request.getAttribute("reviewFoodList");
                            int count = 0;
                            int itemsPerSlide = 8; // Số món ăn tối đa trên mỗi slide

                            if (reviewFoodList != null && !reviewFoodList.isEmpty()) {
                                for (int i = 0; i < reviewFoodList.size(); i++) {
                                    Object[] food = reviewFoodList.get(i);
                                    String dishName = (String) food[0];
                                    float totalPoint = (float) food[3];

                                    // Tìm món ăn tương ứng trong menuList
                                    RestaurantMenuDTO matchedMenu = null;
                                    for (RestaurantMenuDTO menu : menuList) {
                                        if (menu.getDishName().equals(dishName)) {
                                            matchedMenu = menu;
                                            break;
                                        }
                                    }

                                    if (matchedMenu == null) {
                                        continue; // Nếu không tìm thấy món ăn trong menuList, bỏ qua món ăn này
                                    }

                                    // Bắt đầu một slide mới sau mỗi 8 món ăn
                                    if (count % itemsPerSlide == 0) {
                        %>
                        <div class="food-slide <%= (count == 0) ? "active" : ""%>">
                            <div class="rating-grid">
                                <%
                                    }

                                %>
                                <div class="card">
                                    <a href="HomeController?action=viewRestaurant&restaurantId=<%= matchedMenu.getRestaurantId()%>" class="restaurant-link">
                                        <img src="<%= request.getContextPath()%>/MenuDish/<%= matchedMenu.getPicture()%>" alt="<%= matchedMenu.getDishName()%>">
                                        <h3><%= matchedMenu.getDishName()%></h3>
                                    </a>
                                    <p>Rating: 
                                        <%
                                            int stars = Math.round(totalPoint / 2); // Chuyển điểm đánh giá từ thang 10 sang 5
                                            for (int star = 1; star <= 5; star++) {
                                                if (star <= stars) {
                                        %>
                                        &#9733; <!-- Ngôi sao đầy -->
                                        <%
                                        } else {
                                        %>
                                        &#9734; <!-- Ngôi sao trống -->
                                        <%
                                                }
                                            }
                                        %>
                                    </p>
                                </div>
                                <%
                                    count++;

                                    // Đóng slide sau mỗi 8 món ăn hoặc khi kết thúc danh sách
                                    if (count % itemsPerSlide == 0 || i == reviewFoodList.size() - 1) {
                                %>
                            </div>
                        </div>
                        <%
                                    }
                                }
                            } else {
                                out.println("<p>No best rated foods found.</p>");
                            }
                        %>
                    </div>

                    <div class="next-container">
                        <a class="next" onclick="plusSlides2(1)"></a>
                    </div>
                </div>





                <div class="sidebar reveal">
                    <div class="discovery">
                        <img src="/ReviewFood/View/images/icon.png" alt="" style="height: 40px;">
                        <h2>Discovery</h2>
                    </div>
                    <ul>
                        <a href="#restaurants">
                            <li><label class="label-discovery">Best restaurant</label><i class="fas fa-chevron-right"></i></li>
                        </a>
                        <a href="#foods">
                            <li><label class="label-discovery">Best Foods</label><i class="fas fa-chevron-right"></i></li>
                        </a>
                    </ul>
                </div>

                <div class="top-filter reveal">
                    <div class="filter-start">
                        <label class="filter-label">
                            <a href="HomeController?sort=newest&page=1">NEWEST</a>
                        </label>
                    </div>


                    <form action="HomeController" method="GET">
                    <div class="filter-group danhmuc">
                        <label for="category"></label>
                        <select id="category" name="category" onchange="this.form.submit()">
                            <option value="all">- Category -</option>
                            <option value="Sang trọng">Luxurious</option>
                            <option value="buffet">Buffet</option>
                            <option value="Nhà hàng">Restaurant</option>
                            <option value="Ăn vặt/ Vỉa hè">Snacks</option>
                            <option value="Ăn chay">Vegan</option>
                            <option value="Cafe">Cafe/Dessert</option>
                            <option value="Quán ăn">Eatery</option>
                            <option value="Tiệm bánh">Bakery</option>
                            <option value="Đồ uống">Drink</option>
                            <option value="Giao cơm văn phòng">Office meal delivery</option>
                        </select>
                    </div>
                </form>



                    <form action="HomeController" method="GET">
                    <div class="filter-group quan">
                        <label for="address"></label>
                        <select id="address" name="address" onchange="this.form.submit()">
                            <option value="all">- District -</option>
                            <option value="Quận 1, HCM">District 1</option>
                            <option value="Quận 2, HCM">District 2</option>
                            <option value="Quận 3, HCM">District 3</option>
                            <option value="Quận 4, HCM">District 4</option>
                            <option value="Quận 5, HCM">District 5</option>
                            <option value="Quận 6, HCM">District 6</option>
                            <option value="Quận 7, HCM">District 7</option>
                            <option value="Quận 8, HCM">District 8</option>
                            <option value="Quận 9, HCM">District 9</option>
                            <option value="Quận 10, HCM">District 10</option>
                            <option value="Quận 11, HCM">District 11</option>
                            <option value="Quận 12, HCM">District 12</option>
                            <option value="Quận Bình Thạnh, HCM">District Binh Thanh</option>
                            <option value="Quận Tân Bình, HCM">District Tan Binh</option>
                            <option value="Quận Phú Nhuận, HCM">District Phu Nhuan</option>
                            <option value="Quận Tân Phú, HCM">District Tan Phu</option>
                            <option value="Quận Gò Vấp, HCM">District Go Vap</option>
                            <option value="Quận Bình Tân, HCM">District Binh Tan</option>
                            <option value="Thủ Đức, HCM">City Thu Duc</option>
                            <option value="Huyện Bình Chánh, HCM">District Binh Chanh</option>
                            <option value="Huyện Nhà Bè, HCM">District Nha Be</option>
                            <option value="Huyện Hóc Môn, HCM">District Hoc Mon</option>
                            <option value="Huyện Cần Giờ, HCM">District Can Gio</option>
                        </select>
                    </div>
                </form>



                </div>

                <div class="content reveal">
                    <div class="grid-container">
                        <%
                            List<RestaurantMenuDTO> menuListpagination = (List<RestaurantMenuDTO>) request.getAttribute("menuListpagination");

                            if (menuListpagination != null && !menuListpagination.isEmpty()) {
                                for (RestaurantMenuDTO menu : menuListpagination) {
                                    String restaurantAddress = "Unknown Address"; // Địa chỉ mặc định nếu không tìm thấy nhà hàng

                                    // Tìm nhà hàng tương ứng với menu.getRestaurantId()
                                    for (Model.restaurant.RestaurantDTO restaurant : restaurantList) {
                                        if (restaurant.getRestaurantId() == menu.getRestaurantId()) {
                                            restaurantAddress = restaurant.getAddress();
                                            break;
                                        }
                                    }
                        %>
                        <div class="food-item">
                            <a href="HomeController?action=viewRestaurant&restaurantId=<%= menu.getRestaurantId()%>" class="restaurant-link">
                                <img src="<%= request.getContextPath()%>/MenuDish/<%= menu.getPicture()%>" alt="<%= menu.getDishName()%>">
                                <h2><%= menu.getDishName()%></h2>

                            </a>
                            <h4><%= restaurantAddress%></h4>
                        </div>
                        <%
                                }
                            } else {
                                out.println("<p>No menu items found.</p>");
                            }
                        %>
                    </div>


                    <%
                        Integer currentPage = (Integer) request.getAttribute("currentPage");
                        if (currentPage == null) {
                            currentPage = 1; // Đặt giá trị mặc định nếu `currentPage` là null
                        }
                    %>
                    <div class="pagination">
                        <!-- Form phân trang Previous -->
                        <form action="HomeController" method="get">
                            <input type="hidden" name="page" value="<%= currentPage - 1%>">
                            <% if (request.getParameter("keyword") != null && !"null".equals(request.getParameter("keyword"))) {%>
                            <input type="hidden" name="keyword" value="<%= request.getParameter("keyword")%>">
                            <% }%>
                            <!-- Disable nút Previous nếu currentPage <= 1 -->
                            <button type="submit" <%= currentPage <= 1 ? "disabled" : ""%>>Previous</button>
                        </form>

                        <!-- Form phân trang Next -->
                        <form action="HomeController" method="get">
                            <input type="hidden" name="page" value="<%= currentPage + 1%>">
                            <% if (request.getParameter("keyword") != null && !"null".equals(request.getParameter("keyword"))) {%>
                            <input type="hidden" name="keyword" value="<%= request.getParameter("keyword")%>">
                            <% }%>
                            <button type="submit">Next</button>
                        </form>
                    </div>


                </div>



                <footer>
                    <div class="footer-content reveal">
                        <div class="footer-logo">
                            <img src="/ReviewFood/View/images/logo2.png" alt="Logo" style="border-radius: 50%;">
                        </div>
                        <div class="footer-links">
                            <a href="#">Privacy Policy</a> |
                            <a href="#">Terms of Service</a> |
                            <a href="#">Contact Us</a>
                        </div>
                        <div class="social-media">
                            <a href="#"><i class="fab fa-facebook-f"></i></a>
                            <a href="#"><i class="fab fa-twitter"></i></a>
                            <a href="#"><i class="fab fa-instagram"></i></a>
                        </div>
                    </div>
                    <div class="footer-bottom">
                        <p>&copy; 2024 F-Food. All Rights Reserved.</p>
                    </div>
                </footer>


                <script src="<%= request.getContextPath()%>/JS/home.js"></script>




        </body>

    </html>