<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/ReviewFood/CSS/search.css">
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






            <div class="content reveal">
                <div class="grid-container">
                    <%
                        List<Model.restaurant.RestaurantDTO> restaurantList = (List<Model.restaurant.RestaurantDTO>) request.getAttribute("restaurantList");
                        List<Model.restaurantMenu.RestaurantMenuDTO> menuList = (List<Model.restaurantMenu.RestaurantMenuDTO>) request.getAttribute("menuList");
                        Map<Integer, Model.restaurant.RestaurantDTO> restaurantMap = new HashMap<>();
                        if (restaurantList != null) {
                            for (Model.restaurant.RestaurantDTO restaurant : restaurantList) {
                                restaurantMap.put(restaurant.getRestaurantId(), restaurant);
                            }
                        }

                        if (menuList != null) {
                            for (Model.restaurantMenu.RestaurantMenuDTO menu : menuList) {
                                Model.restaurant.RestaurantDTO correspondingRestaurant = restaurantMap.get(menu.getRestaurantId());
                    %>
                    <div class="food-item">
                        <img src="<%= request.getContextPath()%>/MenuDish/<%= menu.getPicture()%>" alt="<%= menu.getDishName()%>">
                        <h2>
                            <a href="HomeController?action=viewRestaurant&restaurantId=<%= correspondingRestaurant != null ? correspondingRestaurant.getRestaurantId() : ""%>" class="restaurant-link">
                                <%= (correspondingRestaurant != null) ? correspondingRestaurant.getRestaurantName() : "Unknown Name"%>
                            </a>
                        </h2>
                        <p><%= (correspondingRestaurant != null) ? correspondingRestaurant.getAddress() : "Unknown Address"%></p>
                        <h3><%= menu.getDishName()%></h3>
                    </div>
                    <%
                            }
                        } else {
                            out.println("<p>No menu items found.</p>");
                        }
                    %>
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