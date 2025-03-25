<%@page import="Model.users.UsersDTO"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset='utf-8'>
        <meta http-equiv='X-UA-Compatible' content='IE=edge'>
        <title>Restaurant Page</title>
        <meta name='viewport' content='width=device-width, initial-scale=1'>
        <!--<link rel="stylesheet" href="/ReviewFood/CSS/header.css">-->
        <link rel="stylesheet" href="/ReviewFood/CSS/footer.css">
        <script src="/ReviewFood/JS/reviewrestaurant.js"></script>
        <link rel="stylesheet" href="/ReviewFood/CSS/restaurant_profile.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">


    </head>

    <body>

        <%--<jsp:include page="header.jsp" />--%>


        <div class="profile-container">
            <div class="between">
                <div class="profile-container-left">
                    <img src="<%= request.getContextPath()%>/Avatar Restaurant/Avatar(${restaurant.restaurantId}).jpg" alt=${restaurant.restaurantName}>
                </div>
                <div class="profile-container-right" style="display: block">
                    <div class="breadcrumb">
                        <div>
                            <span>
                                <a href="${pageContext.request.contextPath}/HomeController">Home</a>
                                >
                            </span>
                        </div>
                        <div>
                            <span>
                                <a href="#">${restaurant.restaurantName}></a>
                            </span>
                        </div>
                        <div>
                            <span>
                                <a href="#">${restaurant.address}></a>
                            </span>
                        </div>
                    </div>

                    <h1>${restaurant.restaurantName}</h1>
                    <div class="margin-top">
                        <div class="point-summary-container">
                            <div class="point-summary">
                                <div class="point-avg"><%= session.getAttribute("totalAverage")%></div>
                                <div style="gap: 50px;">
                                    <div class="mark">
                                        <div class="green-text">
                                            <span><%= session.getAttribute("avgQualityRating")%></span>
                                        </div>
                                        <div class="status">Quality</div>
                                    </div>
                                    <div class="mark">
                                        <div class="green-text">
                                            <span><%= session.getAttribute("avgPriceRating")%></span>
                                        </div>
                                        <div class="status">Price</div>
                                    </div>
                                    <div class="mark">
                                        <div class="green-text">
                                            <span><%= session.getAttribute("avgServiceRating")%></span>
                                        </div>
                                        <div class="status">Serving</div>
                                    </div>
                                    <div class="mark">
                                        <div class="green-text">
                                            <span><%= session.getAttribute("avgPositionRating")%></span>
                                        </div>
                                        <div class="status">Position</div>
                                    </div>
                                    <div class="mark">
                                        <div class="green-text">
                                            <span><%= session.getAttribute("avgSpaceRating")%></span>
                                        </div>
                                        <div class="status">Space</div>
                                    </div>
                                </div>
                                <div class="mark">
                                    <div>404</div>
                                    <div class="status">Comment</div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="margin-top">
                        <div class="address-restaurant">
                            <div class="address">
                                <div>
                                    <span><i class="fa-solid fa-location-arrow"></i></span>
                                    <span>${sessionScope.restaurant.address}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="margin-top">
                        <div class="status-restaurant">
                            <div>
                                <span><i class="fa-regular fa-clock"></i></span>
                                <span class="green-text">Openning</span>
                            </div>
                            <div>
                                <span>09:00 - 22:00</span>
                                <span><i class="fa-solid fa-circle-exclamation"></i></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="option">
            <!-- Phone Link -->
            <a href="#" id="phone-link">
                <i class="fa-solid fa-phone"></i>
                Phone
            </a>

            <!-- Phone Popup -->
            <div id="phone-popup" class="phone-popup">
                <div class="phone-popup-content">
                    <span class="close-phone-popup">&times;</span>
                    <p>Contact us at: <strong><%= session.getAttribute("ownerPhone")%></strong></p>
                </div>
            </div>
            <a href="#" class="review-food-btn"  onclick="document.getElementById('review-dialog').style.display = 'block'">
                <i class="fa-solid fa-comment"></i>
                Comment
            </a>
        </div>

        <div>
            <h2 class="menu">Menu</h2>
        </div>

        <div class="menu-container">
            <%
                List<Model.restaurantMenu.RestaurantMenuDTO> menuList = (List<Model.restaurantMenu.RestaurantMenuDTO>) session.getAttribute("menuList");
                if (menuList == null || menuList.isEmpty()) {
            %>
            <p>No menu items found for this restaurant.</p>
            <%
            } else {
            %>
            <div class="menu-container-left" style="gap: 45px">
                <div class="column" style="gap: 105px">
                    <% for (int i = 0; i < menuList.size(); i++) {
                            Model.restaurantMenu.RestaurantMenuDTO menu = menuList.get(i);
                            if (i == menuList.size() / 2) { // Start second column halfway through the list
                    %>
                </div>
                <div class="column" style="gap: 105px">
                    <% }%>
                    <div style="flex-direction: column;">
                        <img style="width: 100px; height: 100px" src="<%= request.getContextPath()%>/MenuDish/<%= menu.getPicture()%>" alt="<%= menu.getDishName()%>">
                        <div>
                            <span><%= menu.getDishName()%></span>
                        </div>
                    </div>
                    <% } %>
                </div>
            </div>

            <div class="menu-container-bottom">
                <button>View more</button>
            </div>
            <%
                }
            %>
        </div>


        <div class="review-container">
            <div class="review-list">
                <%
                    // L?y danh sách bình lu?n và ?i?m ?ánh giá trung bình t? session
                    List<Object[]> commentList = (List<Object[]>) session.getAttribute("commentList");

                    if (commentList != null && !commentList.isEmpty()) {
                        for (Object[] review : commentList) {
                %>
                <div class="review-list-container-top">

                    <img class="img" src="<%= review[6]%>" alt="User Avatar">


                    <div class="info">
                        <div>
                            <a class="name" href="#">
                                <%= review[0]%> <%= review[1]%> 
                            </a>
                            <div class="time">
                                <i class="fa-solid fa-calendar-days"></i>
                                <span><%= review[2]%> <i class="fa-regular fa-clock"></i> 9:30</span> 
                            </div>
                        </div>
                    </div>

                    <div class="review-point">
                        <span class="point"><%= review[5] != null ? review[5] : "N/A"%></span>
                    </div>
                </div>

                <div class="divider"></div>

                <div class="title">
                    <%= review[3]%> 
                </div>
                <div class="comments">
                    <span><%= review[4]%></span> 
                    <div>
                        <img src="<%= review[6]%>" alt="Comment Image">
                    </div>
                </div>

                <%
                    }
                } else {
                %>
                <p>Không có bình luận nào.</p>
                <%
                    }
                %>
            </div>



            <div class="rating-container">
                <div class="rating-box">
                    <div class="summary">
                        <div class="font-avg">4.5</div>
                        <div style="display: flex; gap: 10px;">
                            <div>
                                <i class="fa-solid fa-circle"></i>
                                <i class="fa-solid fa-circle"></i>
                                <i class="fa-solid fa-circle"></i>
                                <i class="fa-solid fa-circle"></i>
                                <i class="fa-solid fa-circle-half-stroke"></i>
                            </div>
                            <span>3,346 reviews</span>
                        </div>
                    </div>
                    <div class="rating-number">
                        <div class="evaluate">
                            <div class="font-text">Excellent</div>
                            <div class="all">
                                <div class="point-green" style="width: 100%;">1</div>
                            </div>
                            <div class="font-text1">2,570</div>
                        </div>
                        <div class="evaluate">
                            <div class="font-text">Good</div>
                            <div class="all">
                                <div class="point-green" style="width: 70%;;">1</div>
                            </div>
                            <div class="font-text1">397</div>
                        </div>
                        <div class="evaluate">
                            <div class="font-text">Average</div>
                            <div class="all">
                                <div class="point-green" style="width: 50%;;">1</div>
                            </div>
                            <div class="font-text1">231</div>
                        </div>
                        <div class="evaluate">
                            <div class="font-text">Poor</div>
                            <div class="all">
                                <div class="point-green" style="width: 30%;;">1</div>
                            </div>
                            <div class="font-text1">95</div>
                        </div>
                        <div class="evaluate">
                            <div class="font-text">Terrible</div>
                            <div class="all">
                                <div class="point-green" style="width: 10%;;">1</div>
                            </div>
                            <div class="font-text1">53</div>
                        </div>
                    </div>
                </div>
                <!-- Comment Button Section -->
                <div class="comment">
                    <button class="review-food-btn" onclick="document.getElementById('review-dialog').style.display = 'block'">
                        <i class="fa-solid fa-comment"></i>
                        Comment
                    </button>
                </div>

                <!-- Review Dialog -->
                <div id="review-dialog" class="dialog" style="display: none;">
                    <div class="dialog-content">
                        <span class="close" onclick="document.getElementById('review-dialog').style.display = 'none'">&times;</span>
                        <h2>Write a comment:</h2>
                        <form action="${pageContext.request.contextPath}/RestaurantController" method="post">
                            <input type="hidden" name="action" value="submitReview">
                            <%
                                // Ki?m tra n?u userSession t?n t?i và có userId
                                UsersDTO userSession = (UsersDTO) session.getAttribute("userSession");
                                if (userSession != null) {
                            %>
                            <input type="hidden" name="userId" value="<%= userSession.getUserId()%>">
                            <%
                            } else {
                            %>
                            <p>Vui lòng ??ng nh?p ?? g?i ?ánh giá.</p>
                            <%
                                }
                            %>
                            <input type="hidden" name="restaurantId" value="${restaurant.restaurantId}"> <!-- Restaurant ID -->

                            <div class="container-review">

                                <div class="container-left">
                                    <img src="<%= request.getContextPath()%>/Avatar Restaurant/Avatar(${restaurant.restaurantId}).jpg" 
                                         alt="Food Image" class="food-image">
                                    <div class="restaurant-info">
                                        <div class="info1">
                                            <div class="rating">
                                                <span>7.1</span>
                                            </div>
                                            <div class="info2">
                                                <div>${restaurant.restaurantName}</div>
                                                <div><span>${restaurant.address}</span></div>
                                            </div>
                                        </div>
                                        <div class="container-left-bottom">
                                            <div class="comment-review">404 Comments</div>
                                            <div class="rated">
                                                <div class="rated1"><div>164</div><span>Excellent</span></div>
                                                <div class="rated1"><div>37</div><span>Good</span></div>
                                                <div class="rated1"><div>239</div><span>Average</span></div>
                                                <div class="rated1"><div>2</div><span>Terrible</span></div>
                                            </div>
                                            <div class="rating-box1">
                                                <div class="rating-number">
                                                    <div class="evaluate">
                                                        <div class="font-text">Position</div>
                                                        <div class="all"><div class="point-green" style="width: 100%;">1</div></div>
                                                        <div class="font-text1">2,570</div>
                                                    </div>
                                                    <div class="evaluate">
                                                        <div class="font-text">Price</div>
                                                        <div class="all"><div class="point-green" style="width: 70%;">1</div></div>
                                                        <div class="font-text1">397</div>
                                                    </div>
                                                    <div class="evaluate">
                                                        <div class="font-text">Quality</div>
                                                        <div class="all"><div class="point-green" style="width: 50%;">1</div></div>
                                                        <div class="font-text1">231</div>
                                                    </div>
                                                    <div class="evaluate">
                                                        <div class="font-text">Serving</div>
                                                        <div class="all"><div class="point-green" style="width: 30%;">1</div></div>
                                                        <div class="font-text1">95</div>
                                                    </div>
                                                    <div class="evaluate">
                                                        <div class="font-text">Space</div>
                                                        <div class="all"><div class="point-green" style="width: 10%;">1</div></div>
                                                        <div class="font-text1">53</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Picture Section -->
                                <div class="container-right" id="picture-section">
                                    <div>
                                        <input type="text" name="title" placeholder="Enter a title, for example: 'The food here is great'"
                                               class="title-input" required>
                                        <textarea name="content" placeholder="Enter comment content" class="comment-input" required></textarea>
                                    </div>
                                    <div class="album1">
                                        <input type="text" name="picture" class="upload-placeholder1" placeholder="Copy link of picture">
                                    </div>
                                </div>

                                <!-- Review Section -->
                                <div class="container-right" id="review-section" style="display: none;">
                                    <h2>Rating</h2>
                                    <div class="ratings">
                                        <div class="rating-item">
                                            <label for="rating-quality">Quality</label>
                                            <input type="range" id="rating-quality" name="qualityRating" min="0" max="10" value="5" oninput="updateRatingValue('quality', this.value)">
                                            <span id="rating-value-quality">5</span>
                                        </div>
                                        <div class="rating-item">
                                            <label for="rating-price">Price</label>
                                            <input type="range" id="rating-price" name="priceRating" min="0" max="10" value="5" oninput="updateRatingValue('price', this.value)">
                                            <span id="rating-value-price">5</span>
                                        </div>
                                        <div class="rating-item">
                                            <label for="rating-service">Serving</label>
                                            <input type="range" id="rating-service" name="serviceRating" min="0" max="10" value="5" oninput="updateRatingValue('service', this.value)">
                                            <span id="rating-value-service">5</span>
                                        </div>
                                        <div class="rating-item">
                                            <label for="rating-position">Position</label>
                                            <input type="range" id="rating-position" name="positionRating" min="0" max="10" value="5" oninput="updateRatingValue('position', this.value)">
                                            <span id="rating-value-position">5</span>
                                        </div>
                                        <div class="rating-item">
                                            <label for="rating-space">Space</label>
                                            <input type="range" id="rating-space" name="spaceRating" min="0" max="10" value="5" oninput="updateRatingValue('space', this.value)">
                                            <span id="rating-value-space">5</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Action Buttons -->
                            <div class="actions1">
                                <div id="picture-tab" class="choose" onclick="showPictureSection()">Picture</div>
                                <div id="review-tab" class="notchoose" onclick="showReviewSection()">Review</div>
                                <button type="submit" class="submit-btn">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>




            </div>
        </div>
        <footer>
            <div class="footer-container">
                <div class="footer-section about">
                    <h3>About Us</h3>
                    <p>We are a tech company focused on delivering the best software solutions.</p>
                </div>
                <div class="footer-section links">
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="#">Home</a></li>
                        <li><a href="#">Services</a></li>
                        <li><a href="#">Contact</a></li>
                        <li><a href="#">Blog</a></li>
                    </ul>
                </div>
                <div class="footer-section contact">
                    <h3>Contact Us</h3>
                    <p>Email: info@company.com</p>
                    <p>Phone: +123 456 789</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2024 Your Company | All rights reserved.</p>
            </div>
        </footer>
    </body>

</html>