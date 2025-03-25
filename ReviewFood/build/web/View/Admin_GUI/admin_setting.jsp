<%@page import="Model.review.ReviewDTO"%>
<%@page import="Model.Owner.OwnerDTO"%>
<%@page import="Model.users.UsersDTO"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard Settings</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">  
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>

        <link rel="stylesheet" href="/ReviewFood/CSS/admin_style.css">
        <link rel="stylesheet" href="/ReviewFood/CSS/admin_manager.css">
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
                                <span class="profile-name">Admin Profile</span>
                                <!-- Dropdown Button -->
                                <button onclick="toggleMenuItems()" class="dropbtn">
                                    <i id="arrow" class="far fa-arrow-alt-circle-down"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Menu items section -->
                        <div class="menu-items" id="menuItems" style="display: block;">

                            <!-- My Profile -->
                            <form action="${pageContext.request.contextPath}/AdminController?action=showProfile" method="POST" style="display: inline;">
                                <div class="menu-item" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class="material-icons" style="font-size:24px">account_circle</i>
                                        <span> My Profile</span>
                                    </div>
                                </div>
                            </form>

                            <!-- Account Management -->
                            <form action="${pageContext.request.contextPath}/AdminController?action=showManager" method="POST" style="display: inline;">
                                <div class="menu-item" style="cursor: pointer;" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class="material-icons" style="font-size:24px;color:black">settings</i>
                                        <span>Account</span>
                                    </div>
                                </div>
                            </form>

                            <!-- Moderate Content -->
                            <form action="${pageContext.request.contextPath}/AdminController?action=showModerate" method="POST" style="display: inline;">
                                <div class="menu-item" style="cursor: pointer;" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class='fas fa-tasks' style="font-size:24px;color: black;"></i>
                                        <span>Moderate</span>
                                    </div>
                                </div>
                            </form>

                            <!-- Log out -->
                            <form action="${pageContext.request.contextPath}/LoginController?action=logout" method="POST" style="display: inline;">
                                <div class="menu-item" style="cursor: pointer;" onclick="this.closest('form').submit();">
                                    <div class="menu-text">
                                        <i class='fas fa-sign-out-alt' style='font-size:24px'></i>
                                        <span>Log out</span>
                                    </div>
                                </div>
                            </form>

                        </div>

                    </div>
                </div>                                              

                <!-- Settings Menu Column -->
                <div class="settings-menu col-lg-3" id="setting-menu-section" style="${requestScope.showProfile ? "display: block;" : "display: none;"}">
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
                        <p>You are going to delete your account. Are you sure?</p>


                        <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/AdminController?action=deleteAccount">
                            <input type="hidden" name="adminId" value="${sessionScope.adminSession.adminId}"> 
                            <div class="modal-actions">
                                <button type="button" class="btn btn-cancel">No, Keep It.</button>
                                <button type="submit" class="btn btn-confirm">Yes, Delete!</button>
                            </div>
                        </form>
                    </div>
                </div>

                <!-- Main Content Column-->
                <div class="content col-lg-6" id="content" style="${requestScope.showProfile ? "display: block;" : "display: none;"}">               
                    <!-- MyProfile Section (Display User Data) -->
                    <div class="myprofile mb-4" id="my-profile-section">                   
                        <div class="card-body">
                            <div class="form-container">
                                <h2>Profile Information</h2>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="first-name">First Name</label>
                                        <span class="user-data">${requestScope.adminInfo.firstName}</span>
                                    </div>
                                    <div class="form-group">
                                        <label for="last-name">Last Name</label>
                                        <span class="user-data">${requestScope.adminInfo.lastName}</span>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="gender">I'm</label>
                                        <span class="user-data">
                                            ${requestScope.adminInfo.sex == 'M' ? 'Male' : 'Female'}
                                        </span>
                                    </div>
                                    <div class="form-group">
                                        <label for="birth-date">Birth Date</label>
                                        <span class="user-data">${requestScope.adminInfo.dateOfBirth}</span>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <span class="user-data">${requestScope.adminInfo.email}</span>
                                    </div>
                                    <div class="form-group">
                                        <label for="phone-number">Phone Number</label>
                                        <span class="user-data">${requestScope.adminInfo.phone}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="location">Location</label>
                                        <span class="user-data">${requestScope.adminInfo.address}</span> <!-- Äá»a chá» -->
                                    </div>
                                </div>
                            </div>
                        </div>                   

                    </div>    

                    <!-- EditProfile Section -->
                    <div class="editprofile" id="edit-profile-section">
                        <div class="form-container">
                            <h2>Edit Profile</h2>
                            <form action="${pageContext.request.contextPath}/AdminController?action=updateAdminInfo" method="POST">
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="first-name">First Name</label>
                                        <input type="text" id="first-name" name="firstName" value="" required="">
                                    </div>
                                    <div class="form-group">
                                        <label for="last-name">Last Name</label>
                                        <input type="text" name="lastName" id="last-name" required="">
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
                        <form action="<%= request.getContextPath()%>/AdminController?action=changePassword" method="POST" class="form-container">
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

                <!-- Manager Content Column -->
                <div id="manager-area" class="col-lg-9" style="${requestScope.showManager ? "display: block;" : "display: none;"}">
                    <!-- Admin Manager Section -->
                    <div id="admin-manager-section" class="admin-manager-section">
                        <h2>Manage Accounts</h2>
                        <div class="product-header">
                            <div class="search-box">
                                <form action="${pageContext.request.contextPath}/AdminController?action=searchAccount" method="POST">
                                    <input type="text" name="keyword" placeholder="Search Accounts" value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : ""%>">
                                    <button type="submit" class="btn btn-primary">Search</button>
                                </form>
                            </div>
                        </div>

                        <table class="product-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Username</th>
                                    <th>Password</th>
                                    <th>Full Name</th>
                                    <th>Role</th>
                                    <th>View Detail</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>

                                <%
                                    List<UsersDTO> userList = (List<UsersDTO>) session.getAttribute("users");
                                    if (userList != null && !userList.isEmpty()) {
                                        for (UsersDTO user : userList) {
                                %>
                                <tr>
                                    <td><%= user.getUserId()%></td>
                                    <td><%= user.getUserName()%></td>
                                    <td><%= user.getPassword()%></td>
                                    <td><%= user.getFirstName() + " " + user.getLastName()%></td>
                                    <td>User</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/AdminController?action=viewUserDetail" method="POST" style="display:inline;">
                                            <input type="hidden" name="userId" value="<%= user.getUserId()%>">
                                            <button type="submit" class="btn btn-edit-menu"><i class="material-icons">visibility</i> View Detail</button>
                                        </form>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/AdminController?action=deleteUser" method="POST" style="display:inline;">
                                            <input type="hidden" name="userId" value="<%= user.getUserId()%>">
                                            <button type="submit" class="btn btn-delete"><i class="fas fa-trash"></i> Delete</button>
                                        </form>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="6">No users available</td>
                                </tr>
                                <% } %>


                                <%
                                    List<OwnerDTO> ownerList = (List<OwnerDTO>) session.getAttribute("owners");
                                    if (ownerList != null && !ownerList.isEmpty()) {
                                        for (OwnerDTO owner : ownerList) {
                                %>
                                <tr>
                                    <td><%= owner.getOwnerId()%></td>
                                    <td><%= owner.getOwnerName()%></td>
                                    <td><%= owner.getPassword()%></td>
                                    <td><%= owner.getFirstName() + " " + owner.getLastName()%></td>
                                    <td>Owner</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/AdminController?action=viewOwnerDetail" method="POST" style="display:inline;">
                                            <input type="hidden" name="ownerId" value="<%= owner.getOwnerId()%>">
                                            <button type="submit" class="btn btn-edit-menu"><i class="material-icons">visibility</i> View Detail</button>
                                        </form>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/AdminController?action=deleteOwner" method="POST" style="display:inline;">
                                            <input type="hidden" name="ownerId" value="<%= owner.getOwnerId()%>">
                                            <button type="submit" class="btn btn-delete"><i class="fas fa-trash"></i> Delete</button>
                                        </form>
                                    </td>
                                </tr>
                                <%
                                    }
                                } else {
                                %>
                                <tr>
                                    <td colspan="6">No owners available</td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>



                        <%
                            // Lấy đối tượng showDetail từ requestScope, đây là User hoặc Owner
                            Object showDetail = request.getAttribute("showDetail");
                            Boolean isUser = (Boolean) request.getAttribute("isUser"); // xác định nếu là User hoặc Owner
                        %>

                        <!-- Modal for Profile Information -->
                        <div id="editMenuModal" class="modal-edit-menu" style="<%= showDetail != null ? "display: block;" : "display: none;"%>">
                            <div class="profile-section mb-4" id="profile-details">
                                <span class="close-btn" onclick="document.getElementById('editMenuModal').style.display = 'none'">&times;</span> <!-- Nút đóng -->

                                <div class="profile-card">
                                    <div class="profile-form-container">
                                        <h2>Profile Information</h2>

                                        <!-- First Name and Last Name -->
                                        <div class="profile-row">
                                            <div class="profile-group">
                                                <label>First Name</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getFirstName()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getFirstName() : "N/A")%>
                                                </span>
                                            </div>
                                            <div class="profile-group">
                                                <label>Last Name</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getLastName()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getLastName() : "N/A")%>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Gender and Birth Date -->
                                        <div class="profile-row">
                                            <div class="profile-group">
                                                <label>Gender</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getSex()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getSex() : "N/A")%>
                                                </span>
                                            </div>
                                            <div class="profile-group">
                                                <label>Birth Date</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getDateOfBirth()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getDateOfBirth() : "N/A")%>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Email and Phone Number -->
                                        <div class="profile-row">
                                            <div class="profile-group">
                                                <label>Email</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getEmail()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getEmail() : "N/A")%>
                                                </span>
                                            </div>
                                            <div class="profile-group">
                                                <label>Phone Number</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getPhone()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getPhone() : "N/A")%>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Location -->
                                        <div class="profile-row">
                                            <div class="profile-group">
                                                <label>Location</label>
                                                <span class="profile-data">
                                                    <%= (isUser != null && isUser && showDetail instanceof UsersDTO) ? ((UsersDTO) showDetail).getAddress()
                                                            : (showDetail instanceof OwnerDTO ? ((OwnerDTO) showDetail).getAddress() : "N/A")%>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>



                    </div>
                </div>

                <!-- Moderate Content Column -->
                <div id="moderate-area" class="col-lg-9" style="${requestScope.showModerate ? "display: block;" : "display: none;"}">
                    <!-- Admin Review Moderate Section -->
                    <div id="admin-moderate-review-section" class="admin-moderate-review-section">
                        <% if (Boolean.TRUE.equals(request.getAttribute("showModerate"))) {%>
                        <h2>Moderate Reviews</h2>
                        <div class="review-header-section">
                            <div class="search-box">
                                <form action="${pageContext.request.contextPath}/AdminController" method="POST">
                                    <input type="hidden" name="action" value="searchReviews">
                                    <input type="text" name="searchKeyword" placeholder="Search Reviews" value="<%= request.getParameter("searchKeyword") != null ? request.getParameter("searchKeyword") : ""%>">
                                    <button type="submit" class="btn btn-primary">Search</button>
                                </form>
                            </div>
                        </div>

                        <table class="review-moderate-table">
                            <thead>
                                <tr>
                                    <th>ID Review</th>
                                    <th>ID User</th>
                                    <th>Fullname</th>
                                    <th>Role</th>
                                    <th>View Detail</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<ReviewDTO> pendingReviews = (List<ReviewDTO>) session.getAttribute("pendingReviews");
                                    if (pendingReviews != null) {
                                        for (ReviewDTO review : pendingReviews) {
                                            UsersDTO user = review.getUser();
                                %>
                                <tr>
                                    <td><%= review.getReviewId()%></td>
                                    <td><%= user.getUserId()%></td>
                                    <td><%= user.getFirstName()%> <%= user.getLastName()%></td>
                                    <td><%= user.getRoleId()%></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/AdminController?action=viewReviewDetail" method="POST">
                                            <input type="hidden" name="reviewId" value="<%= review.getReviewId()%>">
                                            <button type="submit" class="btn btn-view-review">
                                                <i class="material-icons">menu</i> View Detail
                                            </button>
                                        </form>
                                    </td>
                                    <td>
                                        <!-- Accept Form -->
                                        <form action="<%= request.getContextPath()%>/AdminController" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="acceptReview">
                                            <input type="hidden" name="reviewId" value="<%= review.getReviewId()%>">
                                            <input type="hidden" name="restaurantId" value="<%= review.getRestaurantId()%>">
                                            <button type="submit" class="btn btn-edit-review">
                                                <i class="fa fa-check" style="font-size:24px"></i> Accept
                                            </button>
                                        </form>

                                        <!-- Reject Form -->
                                        <form action="<%= request.getContextPath()%>/AdminController" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="rejectReview">
                                            <input type="hidden" name="reviewId" value="<%= review.getReviewId()%>">
                                            <button type="submit" class="btn btn-reject-review">
                                                <i class="fas fa-trash"></i> Reject
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                        <% }%>

                        <!-- Review Details Modal -->
                        <% if (Boolean.TRUE.equals(request.getAttribute("showReviewModal"))) { %>
                        <div id="viewReviewModal" class="modal-view-review" style="display: block;">
                            <div class="review-section" id="review-details">
                                <span class="close-btn" onclick="document.getElementById('viewReviewModal').style.display = 'none'">&times;</span> <!-- Close button -->

                                <div class="review-card">
                                    <div class="review-form-container">
                                        <div class="review-header">
                                            <%
                                                ReviewDTO reviewDetail = (ReviewDTO) request.getAttribute("reviewDetail");
                                                UsersDTO user = (reviewDetail != null) ? reviewDetail.getUser() : null;
                                            %>
                                            <img src="<%= (user != null && user.getPicture() != null) ? user.getPicture() : "default-avatar.jpg"%>" alt="User Avatar" class="review-avatar">

                                            <div class="review-info">
                                                <h3><%= (user != null) ? user.getFirstName() + user.getLastName() : "Unknown User"%></h3> <!-- User's Full Name -->
                                                <p><%= (reviewDetail != null && reviewDetail.getReviewDate() != null) ? reviewDetail.getReviewDate().toString() : "No Date Available"%></p> <!-- Review Date -->
                                            </div>
                                            <span class="review-rating"><%= (reviewDetail != null) ? reviewDetail.getTotalPoint() : "N/A"%></span> <!-- Review Rating -->
                                        </div>

                                        <h2><%= (reviewDetail != null) ? reviewDetail.getTitle() : "No Title"%></h2> <!-- Review Title -->

                                        <p>
                                            <%= (reviewDetail != null) ? reviewDetail.getContent() : "No Content Available"%>
                                        </p> <!-- Review Content -->

                                        <div class="review-image">
                                            <img src="<%= (reviewDetail != null && reviewDetail.getPicture() != null) ? reviewDetail.getPicture() : "default-image.jpg"%>" alt="Review Image" class="review-img"> <!-- Review Image -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <% }%>


                    </div>
                </div>

                <!-- End of Main Content Column -->
            </div>
        </div>



        <script src="/ReviewFood/JS/admin_script.js"></script>
        <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
