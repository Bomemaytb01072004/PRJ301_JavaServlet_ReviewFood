<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard Settings</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">  
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>

        <link rel="stylesheet" href="/ReviewFood/CSS/owner_style.css">
        <link rel="stylesheet" href="/ReviewFood/CSS/owner_manager.css">
    </head>
    <body>

        <!-- Header Section -->
        <div class="header">
            <div class="headercontent container-fluid d-flex justify-content-between align-items-center">
                <div class="page-path">
                    <a href="${pageContext.request.contextPath}/HomeController" class="home-button" style="text-decoration: none">
                        <i class="fa fa-home"></i> Home
                    </a>
                    <span>/ Setting</span>
                </div>
                <div class="search-bar">
                    <form class="example" action="/action_page.php" style="margin:auto;max-width:300px">
                        <input type="text" placeholder="Search.." name="search2">
                        <button type="submit"><i class="btn-search fa fa-search"></i></button>
                    </form>
                </div>
                <div class="user-actions">
                    <span></span>
                    <span></span>
                    <span><i class="bell fa fa-bell" style="font-size:16px"></i></span>
                </div>
            </div>
        </div>

        <!-- Body Section -->
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar Column -->
                <div class="sidebar col-lg-3">
                    <div class="profile-menu">
                        <div class="profile-header">
                            <img src="/ReviewFood/View/images/j97.jpg" alt="Profile Picture" class="profile-image">
                            <br>
                            <div class="sidebar-usertitle">
                                <span class="profile-name">Owner Profile</span>

                                <button onclick="toggleMenuItems()" class="dropbtn">
                                    <i id="arrow" class="far fa-arrow-alt-circle-down"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Menu items section -->

                        <div class="menu-items" id="menuItems" style="display: block;" >
                            <!-- My Profile -->
                            <form action="${pageContext.request.contextPath}/OwnerController?action=showProfile" method="POST" style="display: inline;">
                                <div class="menu-item" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class="material-icons" style="font-size:24px">account_circle</i>
                                        <span> My Profile</span>
                                    </div>
                                </div>
                            </form>    

                            <!-- Restaurant -->
                            <form action="${pageContext.request.contextPath}/OwnerController?action=showRestaurants" method="POST" style="display: inline;">
                                <div class="menu-item" style="cursor: pointer;" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class="material-icons" style="font-size:24px; color:black">settings</i>
                                        <span>Restaurant</span>
                                    </div>
                                </div>
                            </form> 

                            <!-- Log out -->
                            <form action="${pageContext.request.contextPath}/LoginController?action=logout" method="POST" style="display: inline;">
                                <div class="menu-item" style="cursor: pointer;" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class="fas fa-sign-out-alt" style="font-size:24px;"></i>
                                        <span style="text-decoration: none; color: black;">Log out</span>
                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>                                              

                <!-- Settings Menu Column -->
                <div class="settings-menu col-lg-3" id="setting-menu-section" style="${showManager ? "display: none;" : "display: block;"}">
                    <ul>
                        <li onclick="scrollToSection('my-profile-section')"><i class='far fa-address-card' style='font-size:24px'></i> <span>My Profile</span></li>
                        <li onclick="scrollToSection('edit-profile-section')"><i class='fas fa-user-edit' style='font-size:24px'></i> <span>Edit Profile</span></li>
                        <li onclick="scrollToSection('change-password-section')"><i class="fa fa-lock" style="font-size:24px"></i> <span>Change Password</span></li>

                        <li class="btn-delete-account">
                            <i class='fas fa-trash-alt' style='font-size:24px'></i> <span>Delete Account</span>
                        </li>                    
                    </ul>
                </div>

                <!-- Delete Account Modal -->
                <div id="deleteModal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <span class="modal-icon">&#9888;</span>
                        </div>
                        <h2>Delete Project</h2>
                        <p>You?re going to delete your account. Are you sure?</p>


                        <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/OwnerController?action=deleteAccount">
                            <input type="hidden" name="ownerId" value="${sessionScope.ownerSession.ownerId}"> 
                            <div class="modal-actions">
                                <button type="button" class="btn btn-cancel">No, Keep It.</button>
                                <button type="submit" class="btn btn-confirm">Yes, Delete!</button>
                            </div>
                        </form>
                    </div>
                </div>


                <div class="content col-lg-6" id="content" style="${showManager ? "display: none;" : "display: block;"}">              

                    <div class="myprofile mb-4" id="my-profile-section">                   
                        <div class="card-body">
                            <div class="form-container">
                                <h2>Profile Information</h2>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="first-name">First Name</label>
                                        <span class="user-data">${requestScope.ownerInfo.firstName}</span> 
                                    </div>
                                    <div class="form-group">
                                        <label for="last-name">Last Name</label>
                                        <span class="user-data">${requestScope.ownerInfo.lastName}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="gender">I'm</label>
                                        <span class="user-data">
                                            ${requestScope.ownerInfo.sex == 'M' ? 'Male' : 'Female'}
                                        </span>
                                    </div>
                                    <div class="form-group">
                                        <label for="birth-date">Birth Date</label>
                                        <span class="user-data">${requestScope.ownerInfo.dateOfBirth}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <span class="user-data">${requestScope.ownerInfo.email}</span> 
                                    </div>
                                    <div class="form-group">
                                        <label for="phone-number">Phone Number</label>
                                        <span class="user-data">${requestScope.ownerInfo.phone}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="location">Location</label>
                                        <span class="user-data">${requestScope.ownerInfo.address}</span> 
                                    </div>
                                </div>
                            </div>
                        </div>                   
                    </div>


                    <div class="editprofile" id="edit-profile-section">
                        <div class="form-container">
                            <h2>Edit Profile</h2>
                            <form action="${pageContext.request.contextPath}/OwnerController?action=updateOwnerInfo" method="POST">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="first-name">First Name</label>
                                        <input type="text" id="first-name" name="firstName" value="" required="">
                                    </div>
                                    <div class="form-group">
                                        <label for="last-name">Last Name</label>
                                        <input type="text" id="last-name" name="lastName" value="" required="">
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="gender">Sex</label>
                                        <select id="gender" name="sex">
                                            <option value="M">Male</option>
                                            <option value="F">Female</option>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="birth-month">Birth Date</label>
                                        <div class="birth-date">
                                            <select id="birth-month" name="birthMonth">
                                                <!--JavaScript -->
                                            </select>
                                            <select id="birth-day" name="birthDay">
                                                <!--JavaScript -->
                                            </select>
                                            <select id="birth-year" name="birthYear">
                                                <!--JavaScript -->
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <input type="email" id="email" name="email" value="" required="">
                                    </div>
                                    <div class="form-group">
                                        <label for="confirm-email">Confirmation Email</label>
                                        <input type="email" id="confirm-email" name="confirmEmail" required="">
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="location">Your Location</label>
                                        <input type="text" id="location" name="address" value="" required="">
                                    </div>
                                    <div class="form-group">
                                        <label for="phone-number">Phone Number</label>
                                        <input type="text" id="phone-number" name="phone" pattern="0[0-9]{9,10}" value="" required="">
                                    </div>
                                </div>
                                <div class="form-row">
                                    <button type="submit" class="btn-info">Save Change</button>
                                </div>
                                <!-- Display Edit Info Messages -->
                                <% String editInfoMessage = (String) request.getAttribute("editInfoMessage"); %>
                                <% if (editInfoMessage != null) {%>
                                <div class="alert alert-success">
                                    <%= editInfoMessage%>
                                </div>
                                <% } %>

                                <% String editInfoError = (String) request.getAttribute("editInfoError"); %>
                                <% if (editInfoError != null) {%>
                                <div class="alert alert-danger">
                                    <%= editInfoError%>
                                </div>
                                <% }%>

                            </form>
                        </div>
                    </div>

                    <!-- Change Password Section -->
                    <div class="changepassword" id="change-password-section">
                        <h2>Change Password</h2>
                        <form action="<%= request.getContextPath()%>/OwnerController?action=changePassword" method="POST" class="form-container">
                            <div class="form-row">
                                <div class="form-group">
                                    <input type="password" id="current-password" name="current-password" placeholder=" " required>
                                    <label for="current-password">Current password</label>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <input type="password" id="new-password" name="new-password" placeholder=" " required>
                                    <label for="new-password">New password</label>
                                </div>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <input type="password" id="confirm-password" name="confirm-password" placeholder=" " required>
                                    <label for="confirm-password">Confirm new password</label>
                                </div>
                            </div>
                            <div class="password-requirements">
                                <h4>Password requirements</h4>
                                <ul>
                                    <li>One special character</li>
                                    <li>Min 6 characters</li>
                                    <li>One number (2 are recommended)</li>
                                    <li>Change it often</li>
                                </ul>
                            </div>
                            <div class="form-row">
                                <button type="submit" class="btn-info">Update Password</button>
                            </div>

                            <!-- Display Change Password Messages -->
                            <% String passwordChangeMessage = (String) request.getAttribute("passwordChangeMessage"); %>
                            <% if (passwordChangeMessage != null) {%>
                            <div class="alert alert-success">
                                <%= passwordChangeMessage%>
                            </div>
                            <% } %>

                            <% String passwordChangeError = (String) request.getAttribute("passwordChangeError"); %>
                            <% if (passwordChangeError != null) {%>
                            <div class="alert alert-danger">
                                <%= passwordChangeError%>
                            </div>
                            <% }%>

                        </form>
                    </div>

                </div>

                <div id="manager-area" class="col-lg-9" style="${requestScope.showManager ? "display: block;" : "display: none;"}">    
                    <div id="restaurant-manager-section" class="restaurant-manager-section">
                        <h2>Manage Restaurant</h2>
                        <div class="product-header">
                            <button class="btn btn-add-restaurant">Add Restaurant</button>
                            <div class="search-box">
                                <form action="${pageContext.request.contextPath}/OwnerController?action=searchRestaurant" method="POST">
                                    <input type="text" name="keyword" placeholder="Search Restaurants" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
                                    <input type="hidden" name="ownerId" value="${sessionScope.ownerSession.ownerId}">
                                    <button type="submit" class="btn btn-primary">Search</button>
                                </form>
                            </div>
                        </div>

                        <table class="product-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Image</th>
                                    <th>Restaurant Name</th>
                                    <th>Address</th>
                                    <th>View Menu</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%

                                    List<Model.restaurant.RestaurantDTO> restaurantList = (List<Model.restaurant.RestaurantDTO>) session.getAttribute("restaurants");
                                    if (restaurantList != null && !restaurantList.isEmpty()) {

                                        for (Model.restaurant.RestaurantDTO restaurant : restaurantList) {
                                %>
                                <tr>
                                    <td><%= restaurant.getRestaurantId()%></td>
                                    <td><img src="<%= request.getContextPath()%>/Avatar Restaurant/Avatar(<%= restaurant.getRestaurantId()%>).jpg" alt="image" width="50"></td>
                                    <td><%= restaurant.getRestaurantName()%></td>
                                    <td><%= restaurant.getAddress()%></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/OwnerController?action=showRestaurantMenu" method="POST" style="display:inline;">
                                            <input type="hidden" name="restaurantId" value="<%= restaurant.getRestaurantId()%>">
                                            <button class="btn btn-edit-menu"><i class="material-icons">restaurant_menu</i>Open Menu</button>
                                        </form>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/OwnerController?action=editRestaurant" method="POST" style="display:inline;">
                                            <input type="hidden" name="restaurantId" value="<%= restaurant.getRestaurantId()%>">
                                            <button type="submit" class="btn btn-edit-restaurant"><i class="material-icons">edit</i> Edit Info</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/OwnerController?action=showDeleteRestaurant" method="POST" style="display:inline;">
                                            <input type="hidden" name="restaurantId" value="<%= restaurant.getRestaurantId()%>">
                                            <button type="submit" class="btn btn-delete"><i class="fas fa-trash"></i> Delete
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="5">No restaurants available</td>
                                </tr>
                                <% }%>
                            </tbody>
                        </table>


                        <!-- Add Restaurant Modal -->
                        <div id="addRestaurantModal" class="modal-add-restaurant">
                            <div class="modal-add-restaurant-content">
                                <div class="modal-add-restaurant-header">
                                    <span class="modal-add-restaurant-icon">&#9432;</span>
                                </div>
                                <h2>Add New Restaurant</h2>
                                <form action="${pageContext.request.contextPath}/OwnerController?action=createRestaurant" id="createRestaurant" method="POST" class="form-container">
                                    <div class="modal-add-restaurant-form-group">
                                        <label for="add-restaurant-name">Restaurant Name</label>
                                        <input type="text" id="add-restaurant-name" name="restaurantName" placeholder="Enter restaurant name" required="">
                                    </div>
                                    <div class="modal-add-restaurant-form-group">
                                        <label for="add-restaurant-address">Address</label>
                                        <input type="text" id="add-restaurant-address" name="restaurantAddress" placeholder="Enter restaurant address" required>
                                    </div>
                                    <div class="modal-add-restaurant-form-group">
                                        <label for="add-restaurant-avatar">Avatar Restaurant</label>
                                        <input type="text" id="add-restaurant-avatar" name="restaurantAvatar" placeholder="Enter avatar URL" />
                                    </div>
                                    <div class="modal-add-restaurant-actions">
                                        <button type="button" class="btn btn-cancel-add">Close</button>
                                        <button type="submit" class="btn btn-confirm-save-restaurant-change">Add Restaurant</button>
                                    </div>
                                </form>
                            </div>
                        </div>


                        <div id="editRestaurantModal" class="modal-edit-restaurant" style="${requestScope.editMode ? 'display: block;' : 'display: none;'}">
                            <div class="modal-edit-restaurant-content">
                                <div class="modal-edit-restaurant-header">
                                    <span class="modal-edit-restaurant-icon">&#9432;</span>
                                </div>
                                <h2>Edit Restaurant Information</h2>
                                <form id="editRestaurantForm" action="${pageContext.request.contextPath}/OwnerController?action=updateRestaurant" method="POST" class="form-container">
                                    <input type="hidden" name="updateRestaurantId" value="${requestScope.restaurantInfo.restaurantId}">
                                    <div class="modal-edit-restaurant-form-group">
                                        <label for="edit-restaurant-name">Restaurant Name</label>
                                        <input type="text" id="edit-restaurant-name" name="newRestaurantName" value="${requestScope.restaurantInfo.restaurantName}" />
                                    </div>
                                    <div class="modal-edit-restaurant-form-group">
                                        <label for="edit-restaurant-address">Address</label>
                                        <input type="text" id="edit-restaurant-address" name="newRestaurantAddress" value="${requestScope.restaurantInfo.address}" />
                                    </div>
                                    <div class="modal-edit-restaurant-form-group">
                                        <label for="edit-restaurant-avatar">Avatar Restaurant</label>
                                        <input type="text" id="edit-restaurant-avatar" name="newRestaurantAvatar" value="${requestScope.restaurantInfo.picture}" />
                                    </div>
                                    <div class="modal-edit-restaurant-actions">
                                        <button type="button" class="btn btn-cancel-edit" onclick="document.getElementById('editRestaurantModal').style.display = 'none'">Close</button>
                                        <button type="submit" class="btn btn-confirm-save-restaurant-change">Save Changes</button>                                        
                                    </div>
                                </form>
                            </div>
                        </div>



                        <!-- Delete Restaurant Modal -->
                        <div id="deleteRestaurant" class="deleteRestaurant" style="${requestScope.deleteMode ? "display: block;" : "display: none;"}">
                            <div class="deleteRestaurant-content">
                                <div class="deleteRestaurant-header">
                                    <span class="deleteRestaurant-icon">&#9888;</span>
                                </div>
                                <h2>Delete Restaurant</h2>
                                <p>You're going to delete your restaurant. Are you sure?</p>

                                <form id="deleteRestaurant" method="POST" action="${pageContext.request.contextPath}/OwnerController?action=deleteRestaurant" class="form-container">
                                    <input type="hidden" name="deleteRestaurantId" value="${requestScope.restaurantInfo.restaurantId}">
                                    <div class="deleteRestaurant-actions">
                                        <button type="button" class="btn btn-cancel-delete" onclick="document.getElementById('deleteRestaurant').style.display = 'none'">No, Keep It.</button>
                                        <button type="submit" class="btn btn-confirm">Yes, Delete!</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Modal for Manage Menu Products -->
                        <div id="editMenuModal" class="modal-edit-menu" style="${requestScope.showMenu ? "display: block;" : "display: none;"}">
                            <div class="modal-edit-menu-content">
                                <div class="modal-edit-menu-header">
                                    <i class="material-icons gradient-icon" style="font-size: 40px;">restaurant_menu</i>
                                </div>
                                <h2>Manage <span>Products</span></h2>

                                <div class="product-header">
                                    <form action="${pageContext.request.contextPath}/OwnerController?action=showAddProductMode" method="POST" style="display:inline;">
                                        <input type="hidden" name="restaurantId" value="${sessionScope.restaurantInfoToAdd.restaurantId}">
                                        <button type="submit" id="addProductButton" class="btn btn-add-product">Add Product</button>
                                    </form>
                                    <div class="search-box">
                                        <form action="${pageContext.request.contextPath}/OwnerController?action=searchMenu" method="POST">
                                            <input type="text" id="searchProductsInput" name="keyword" placeholder="Search Products" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
                                            <input type="hidden" name="ownerId" value="${sessionScope.ownerSession.ownerId}">
                                            <input type="hidden" name="restaurantId" value="${sessionScope.restaurantInfoToAdd.restaurantId}">
                                            <button type="submit" id="searchProductsButton" class="btn btn-primary">Search</button>
                                        </form>
                                    </div>

                                </div>

                                <table id="editMenuTable" class="product-table">
                                    <thead>
                                        <tr>
                                            <th>Image</th>
                                            <th>Product Name</th>
                                            <th>Category</th>
                                            <th>Cuisine</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%

                                            List<Model.restaurantMenu.RestaurantMenuDTO> menuList = (List<Model.restaurantMenu.RestaurantMenuDTO>) session.getAttribute("menuList");
                                            if (menuList != null && !menuList.isEmpty()) {

                                                for (Model.restaurantMenu.RestaurantMenuDTO menu : menuList) {
                                        %>
                                        <tr>
                                            <td><img src="https://via.placeholder.com/50" alt="Special Indian Thali"></td>
                                            <td><%= menu.getDishName()%></td>
                                            <td><%= menu.getCategory()%></td>
                                            <td><%= menu.getCuisine()%></td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/OwnerController?action=editProductMenu" method="POST" style="display:inline;">
                                                    <input type="hidden" name="menuId" value="<%= menu.getMenuId()%>">
                                                    <button class="btn btn-edit-product">Edit</button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/OwnerController?action=showDeleteProductMode" method="POST" style="display:inline;">
                                                    <input type="hidden" name="menuId" value="<%= menu.getMenuId()%>">
                                                    <button class="btn btn-delete-product">Delete</button>
                                                </form>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <tr>
                                            <td colspan="5">No restaurants available</td>
                                        </tr>
                                        <% }%>
                                    </tbody>
                                </table>

                                <div class="modal-edit-menu-actions">
                                    <form action="${pageContext.request.contextPath}/OwnerController?action=removeSession" method="POST" style="display: inline;">
                                        <button type="submit" class="btn btn-cancel">Close</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Add Product Modal -->
                        <div id="addProductModal" class="modal-add-product" style="${requestScope.addProductMode ? 'display: block;' : 'display: none;'}">
                            <div class="modal-add-product-content">
                                <div class="modal-add-product-header">
                                    <i class="material-icons gradient-icon" style="font-size: 40px;">restaurant_menu</i>
                                </div>
                                <h2>Add New product</h2>
                                <form id="addProductForm" action="${pageContext.request.contextPath}/OwnerController?action=addProductMenu" method="POST" class="form-container">
                                    <input type="hidden" name="restaurantId" value="${sessionScope.restaurantInfoToAdd.restaurantId}">
                                    <div class="modal-add-product-form-group">
                                        <label for="add-product-name">Product Name</label>
                                        <input type="text" id="add-product-name" name="productName" placeholder="Enter product name" required=""/>
                                    </div>
                                    <div class="modal-add-product-form-group">
                                        <label for="add-product-avatar">Avatar product</label>
                                        <input type="text" id="add-product-avatar" name="productAvatar" placeholder="Enter avatar URL" />
                                    </div>
                                    <!-- Category -->
                                    <div class="modal-edit-product-form-group">
                                        <label for="edit-product-category">Category</label>
                                        <input type="text" id="edit-product-category" name="productCategory" value="" placeholder="Enter product category" required=""/>
                                    </div>

                                    <!-- Cuisine -->
                                    <div class="modal-edit-product-form-group">
                                        <label for="edit-product-cuisine">Cuisine</label>
                                        <input type="text" id="edit-product-cuisine" name="productCuisine" placeholder="Enter product cuisine" value="" required=""/>
                                    </div>
                                    <div class="modal-add-product-actions">
                                        <button type="button" class="btn btn-cancel-add" onclick="document.getElementById('addProductModal').style.display = 'none'">Close</button>
                                        <button type="submit" class="btn btn-confirm-save-product-change">Add product</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <!-- Edit Product Modal -->
                        <div id="editProductModal" class="modal-edit-product" style="${requestScope.editProductMode ? 'display: block;' : 'display: none;'}">
                            <div class="modal-edit-product-content">
                                <div class="modal-edit-product-header">
                                    <i class="material-icons gradient-icon" style="font-size: 40px;">restaurant_menu</i>
                                </div>
                                <h2>Edit Product Information</h2>
                                <form id="editProductForm" action="${pageContext.request.contextPath}/OwnerController?action=updateProductMenu" method="POST" class="form-container">
                                    <input type="hidden" name="updateMenuId" value="${requestScope.menuInfo.menuId}">
                                    <input type="hidden" name="restaurantId" value="${requestScope.menuInfo.restaurantId}">
                                    <!-- Product Name -->
                                    <div class="modal-edit-product-form-group">
                                        <label for="edit-product-name">Product Name</label>
                                        <input type="text" id="edit-product-name" name="newProductName" value="${requestScope.menuInfo.dishName}" required=""/>
                                    </div>

                                    <!-- Product Avatar -->
                                    <div class="modal-edit-product-form-group">
                                        <label for="edit-product-avatar">Avatar Product</label>
                                        <input type="text" id="edit-product-avatar" name="newProductAvatar" value="${requestScope.menuInfo.picture}" />
                                    </div>

                                    <!-- Category -->
                                    <div class="modal-edit-product-form-group">
                                        <label for="edit-product-category">Category</label>
                                        <input type="text" id="edit-product-category" name="newProductCategory" value="${requestScope.menuInfo.category}" required=""/>
                                    </div>

                                    <!-- Cuisine -->
                                    <div class="modal-edit-product-form-group">
                                        <label for="edit-product-cuisine">Cuisine</label>
                                        <input type="text" id="edit-product-cuisine" name="newProductCuisine" value="${requestScope.menuInfo.cuisine}" required=""/>
                                    </div>

                                    <!-- Action Buttons -->
                                    <div class="modal-edit-product-actions">
                                        <button type="button" class="btn btn-cancel-edit" onclick="document.getElementById('editProductModal').style.display = 'none'">Close</button>
                                        <button type="submit" class="btn btn-confirm-save-product-change">Save Changes</button>
                                    </div>
                                </form>
                            </div>
                        </div>


                        <!-- Delete Product Modal -->
                        <div id="deleteProduct" class="delete-product" style="${requestScope.deleteProductMode ? 'display: block;' : 'display: none;'}">
                            <div class="delete-product-content">
                                <div class="delete-product-header">
                                    <span class="delete-product-icon">&#9888;</span>
                                </div>
                                <h2>Delete product</h2>
                                <p>You are going to delete ${requestScope.menuInfo.dishName}. Are you sure?</p>

                                <form id="delete-productForm" method="POST" action="${pageContext.request.contextPath}/OwnerController?action=deleteProduct" class="form-container">
                                    <input type="hidden" name="deleteMenuId" value="${requestScope.menuInfo.menuId}">
                                    <input type="hidden" name="restaurantId" value="${requestScope.menuInfo.restaurantId}">
                                    <div class="delete-product-actions">
                                        <button type="button" class="btn btn-cancel-delete" onclick="document.getElementById('deleteProduct').style.display = 'none'">No, Keep It.</button>
                                        <button type="submit" class="btn btn-confirm">Yes, Delete!</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                </div>

                <!-- End of Main Content Column -->
            </div>
        </div>


        <script src="/ReviewFood/JS/owner_script.js" defer></script>
        <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>