<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <title>header</title>
    <link rel="website icon" type="webp"
    href="images/logo.webp">
    <link rel="stylesheet" href="/ReviewFood/CSS/header.css">
</head>

<body>
    <header>
        <nav class="header-top row1">
            <!-- Logo -->
            <div class="logo ">
                <img src="images/logo.webp" alt="Logo">
                <h2>F-Food</h2>
            </div>

            <!-- Search bar -->

            <section class="Search-bar1 ">
                <div class="search-input1">
                    <input type="text" placeholder="Search">
                    <button><i class="fas fa-search"></i></button> <!-- Kính lúp -->
                </div>
            </section>

            <nav class="menu">
                <a href="#">Discover</a>
                <a href="#">Trips</a>
                <a href="#">Review</a>
                <a href="#">More</a>
            </nav>

            <!-- User button -->
            <div class="user-button">
                <div class="translate-button">
                    <i class="fas fa-globe"></i> <!-- Icon quả địa cầu -->
                    <span class="separator">|</span> <!-- Dấu gạch dọc -->
                    <span>USA</span> <!-- Văn bản USD -->
                </div>
                <button onclick="window.location.href='login.html';">Sign in</button>
                <button>Resigter</button>

            </div>
        </nav>

        <div class="header-bottom">
            <!-- Menu -->
            <div class="row1">
                <nav class="menu">
                    <a href="#">Hotels</a>
                    <a href="#">Things to Do</a>
                    <a href="#">Restaurants</a>
                    <a href="#">Flights</a>
                    <a href="#">Vacation Rentals</a>
                    <a href="#">Cruises</a>
                    <a href="#">Rental Cars</a>
                    <a href="#">Forum</a>
                </nav>
            </div>
        </div>
    </header>
</body>

</html>