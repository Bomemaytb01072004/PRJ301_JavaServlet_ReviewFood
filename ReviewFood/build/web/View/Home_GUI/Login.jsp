<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign in || Sign up Form</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="/ReviewFood/CSS/login.css">
    </head>

    <body>

        <div class="container" id="container">
            <%
                String message = (String) request.getAttribute("message"); 
                if (message != null) {
            %>
            <div class="alert alert-success">
                <%= message%>
            </div>
            <%
                }
            %>
            <!-- Sign Up Form -->
            <div class="form-container sign-up-container">
                <form action="${pageContext.request.contextPath}/LoginController?action=register" method="POST">
                    <h1>Create Account</h1>

                    <!-- First Name -->
                    <div class="infield">
                        <input type="text" placeholder="First Name" name="firstNameSignUp" required />
                        <label></label>
                    </div>

                    <!-- Last Name -->
                    <div class="infield">
                        <input type="text" placeholder="Last Name" name="lastNameSignUp" required />
                        <label></label>
                    </div>

                    <!-- Sex -->
                    <div class="infield">
                        <select name="sexSignUp" required>
                            <option value="" disabled selected>Sex</option>
                            <option value="M">Male</option>
                            <option value="F">Female</option>
                        </select>
                        <label></label>
                    </div>

                    <!-- Date of Birth -->
                    <div class="infield">
                        <input type="date" name="dateOfBirthSignUp" required />
                        <label></label>
                    </div>

                    <!-- Phone -->
                    <div class="infield">
                        <input type="text" placeholder="Phone" name="phoneSignUp" required />
                        <label></label>
                    </div>

                    <!-- Email -->
                    <div class="infield">
                        <input type="email" placeholder="Email" name="emailSignUp" required />
                        <label></label>
                    </div>

                    <!-- Address -->
                    <div class="infield">
                        <input type="text" placeholder="Address" name="addressSignUp" required />
                        <label></label>
                    </div>

                    <div class="infield">
                        <input type="text" placeholder="Username" name="usernameSignUp" required />
                        <label></label>
                    </div>

                    <!-- Password -->
                    <div class="infield">
                        <input type="password" placeholder="Password" name="passwordSignUp" required />
                        <label></label>
                    </div>

                    <!-- Role -->
                    <div class="infield">
                        <select name="roleSignUp" required>
                            <option value="" disabled selected>Role</option>
                            <option value="user">User</option>
                            <option value="owner">Owner</option>
                        </select>
                        <label></label>
                    </div>

                    <button>Sign Up</button>
                </form>
            </div>

            <!-- Sign In Form -->
            <div class="form-container sign-in-container">
                <form action="${pageContext.request.contextPath}/LoginController?action=login" method="POST">
                    <h1>Sign in</h1>
                    <div class="infield">
                        <input type="text" placeholder="Username" name="username" required />
                        <label></label>
                    </div>
                    <div class="infield">
                        <input type="password" placeholder="Password" name="password" required />
                        <label></label>
                    </div>
                    <div class="infield">
                        <select name="roleSignIn" required>
                            <option value="" disabled selected>Role</option>
                            <option value="user">User</option>
                            <option value="owner">Owner</option>
                        </select>
                        <label></label>
                    </div>

                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) {%>
                    <h3 class="error-message"><%= error%></h3>
                    <% }%>

                    <button>Sign In</button>
                </form>
            </div>

            <!-- Overlay Section -->
            <div class="overlay-container" id="overlayCon">
                <div class="overlay">
                    <div class="overlay-panel overlay-left">
                        <h1>Welcome Back!</h1>
                        <p>To keep connected with us please login with your personal info</p>
                        <button>Sign In</button>
                    </div>
                    <div class="overlay-panel overlay-right">
                        <h1>Hello, Friend!</h1>
                        <p>Enter your personal details and start journey with us welcome</p>
                        <button>Sign Up</button>
                    </div>
                </div>
                <button id="overlayBtn"></button>
            </div>
        </div>

        <script>
            const container = document.getElementById('container');
            const overlayCon = document.getElementById('overlayCon');
            const overlayBtn = document.getElementById('overlayBtn');

            overlayBtn.addEventListener('click', () => {
                container.classList.toggle('right-panel-active');
                overlayBtn.classList.remove('btnScaled');
                window.requestAnimationFrame(() => {
                    overlayBtn.classList.add('btnScaled');
                });
            });
        </script>

    </body>
</html>
