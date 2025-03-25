<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
        <link rel="stylesheet" href="/ReviewFood/CSS/user_style.css">
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
                    <span>Sign In</span>
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
                                <span class="profile-name">User Profile</span>
                                <!-- Dropdown Button -->
                                <button onclick="toggleMenuItems()" class="dropbtn">
                                    <i id="arrow" class="far fa-arrow-alt-circle-down"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Menu items section -->
                        <div class="menu-items" id="menuItems">
                            <div class="menu-item">
                                <div class="menu-text">
                                    <i class="material-icons" style="font-size:24px">account_circle</i><span> My Profile</span>
                                </div>
                            </div>
                            <div class="menu-item">
                                <div class="menu-text">
                                    <i class="material-icons" style="font-size:24px;color:black">settings</i><span>Setting</span>
                                </div>
                            </div>
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
                <div class="settings-menu col-lg-3">
                    <ul>
                        <li onclick="scrollToSection('profile-information')"><i class='far fa-address-card' style='font-size:24px'></i> <span>My Profile</span></li>
                        <li onclick="scrollToSection('edit-profile')"><i class='fas fa-user-edit' style='font-size:24px'></i> <span>Edit Profile</span></li>
                        <li onclick="scrollToSection('change-password')"><i class="fa fa-lock" style="font-size:24px"></i> <span>Change Password</span></li>

                        <li onclick="document.getElementById('deleteModal').style.display = 'block'">
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
                        <h2>Delete Account</h2>
                        <p>You are going to delete your account. Are you sure?</p>

                        <form id="deleteForm" method="POST" action="${pageContext.request.contextPath}/UsersController?action=deleteAccount">
                            <input type="hidden" name="userId" value="${userSession.userId}">
                            <div class="modal-actions">
                                <button type="button" class="btn btn-cancel" onclick="closeModal()">No, Keep It.</button>
                                <button type="submit" class="btn btn-confirm">Yes, Delete!</button>
                            </div>
                        </form>
                    </div>
                </div>




                <!-- Main Content Column (MyProfile, EditProfile, ChangePassword) -->
                <div class="content col-lg-6">

                    <!-- MyProfile Section (Display User Data) -->
                    <div class="myprofile mb-4" id="profile-information">                   
                        <div class="card-body">
                            <div class="form-container">
                                <h2>Profile Information</h2>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="first-name">First Name</label>
                                        <span class="user-data">${requestScope.userInfo.firstName}</span> 
                                    </div>
                                    <div class="form-group">
                                        <label for="last-name">Last Name</label>
                                        <span class="user-data">${requestScope.userInfo.lastName}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="gender">I'm</label>
                                        <span class="user-data">
                                            ${requestScope.userInfo.sex == 'M' ? 'Male' : 'Female'}
                                        </span>
                                    </div>
                                    <div class="form-group">
                                        <label for="birth-date">Birth Date</label>
                                        <span class="user-data">${requestScope.userInfo.dateOfBirth}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <span class="user-data">${requestScope.userInfo.email}</span> 
                                    </div>
                                    <div class="form-group">
                                        <label for="phone-number">Phone Number</label>
                                        <span class="user-data">${requestScope.userInfo.phone}</span> 
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="location">Location</label>
                                        <span class="user-data">${requestScope.userInfo.address}</span> 
                                    </div>
                                </div>
                            </div>
                        </div>                   
                    </div>


                    <!-- EditProfile Section -->
                    <div class="editprofile" id="edit-profile">
                        <div class="form-container">
                            <h2>Edit Profile</h2>
                            <form action="${pageContext.request.contextPath}/UsersController?action=updateUserInfo" method="POST">
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



                    <div class="changepassword" id="change-password">
                        <h2>Change Password</h2>
                        <form action="<%= request.getContextPath()%>/UsersController?action=changePassword" method="POST" class="form-container">
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

                            <!-- Display success or error messages -->
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



                    <!-- End of Main Content Column -->
                </div>
            </div>

            <script src="/ReviewFood/JS/user_script.js"></script>
            <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
