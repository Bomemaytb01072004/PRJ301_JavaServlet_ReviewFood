// Background
let backgrounds = [
  "/ReviewFood/View/images/background2.jpg",
  "/ReviewFood/View/images/background1.jpg",
  "/ReviewFood/View/images/background3.jpg"
];

let searchBar = document.querySelector('.search-bar');
let currentBackgroundIndex = 0;

function changeBackground() {
  currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;
  searchBar.style.backgroundImage = `url('${backgrounds[currentBackgroundIndex]}')`;
}
changeBackground();
setInterval(changeBackground, 4000);


// Scrolling
function revealElements() {
    var elements = document.querySelectorAll('.reveal');
    var windowHeight = window.innerHeight;
  
    elements.forEach(function(element) {
      var position = element.getBoundingClientRect();
  
      if (position.top < windowHeight - 100) {
        element.classList.add('show');
      }
    });
  }
  
  window.addEventListener('DOMContentLoaded', revealElements);
  
  window.addEventListener('scroll', revealElements);
  
  

// Suggestion
const suggestions = [
    'Cơm gà xé', 'Cơm chiên tôm', 'Cơm hến  ', 'Cơm tấm', 'Cơm chiên cá mặn',
    'Cơm trộn rong biển thanh cua', 'Cơm gà Hội An', 'Cơm lam gà nướng', 'Indian Spices', 'BBQ Grill'
];

function showSuggestions() {
    const input = document.getElementById('search').value.toLowerCase();
    const suggestionList = document.getElementById('suggestions');
    suggestionList.innerHTML = '';

    if (input) {
        const filteredSuggestions = suggestions.filter(item =>
            item.toLowerCase().includes(input)
        );

        if (filteredSuggestions.length > 0) {
            filteredSuggestions.forEach(suggestion => {
                const li = document.createElement('li');
                li.textContent = suggestion;
                li.onclick = function () {
                    document.getElementById('search').value = suggestion;
                    suggestionList.style.display = 'none';
                };
                suggestionList.appendChild(li);
            });
            suggestionList.style.display = 'block';
        } else {
            suggestionList.style.display = 'none';
        }
    } else {
        suggestionList.style.display = 'none';
    }
}

// Slideshow-restaurants
let slideIndex = 0;
showSlides(slideIndex);

function plusSlides(n) {
    showSlides(slideIndex += n);
}

function showSlides(n) {
    let i;
    let slides = document.getElementsByClassName("slide");

    if (n >= slides.length) {
        slideIndex = 0;
    }
    if (n < 0) {
        slideIndex = slides.length - 1;
    }

    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
        slides[i].classList.remove("active");
    }

    slides[slideIndex].style.display = "block";
    slides[slideIndex].classList.add("active");
}


// Slideshow-food
let slideIndex2 = 0;
showSlides2(slideIndex2);

function plusSlides2(n) {
    showSlides2(slideIndex2 += n);
}

function showSlides2(n) {
    let i2;
    let slides2 = document.getElementsByClassName("food-slide");

    if (n >= slides2.length) {
        slideIndex2 = 0;
    }
    if (n < 0) {
        slideIndex2 = slides2.length - 1;
    }

    for (i = 0; i < slides2.length; i++) {
        slides2[i].style.display = "none";
        slides2[i].classList.remove("active");
    }

    slides2[slideIndex2].style.display = "block";
    slides2[slideIndex2].classList.add("active");
}






