// Hàm chuyển đổi hiển thị menu items
function toggleMenuItems() {
    var menuItems = document.getElementById("menuItems");
    var arrow = document.getElementById("arrow");

    if (menuItems.style.display === "block") {
        menuItems.style.display = "none"; // Ẩn menu
    } else {
        menuItems.style.display = "block"; // Hiển thị menu
    }

    // Đổi hướng mũi tên khi bấm
    if (arrow.classList.contains('rotate-down')) {
        arrow.classList.remove('rotate-down');
    } else {
        arrow.classList.add('rotate-down');
    }
}

// Khi trang load lần đầu, đảm bảo menu luôn hiển thị
window.onload = function() {
    var menuItems = document.getElementById("menuItems");
    var arrow = document.getElementById("arrow");

    menuItems.style.display = "block"; // Hiển thị menu ban đầu
    arrow.classList.remove('rotate-down'); // Đặt lại mũi tên
};

// Get modal element
var modal = document.getElementById("deleteModal");

// Open modal function (replace with your own trigger)
function openModal() {
  modal.style.display = "block";
}

// Close modal function (can bind to buttons or background clicks)
function closeModal() {
  modal.style.display = "none";
}

// Example close modal on background click
window.onclick = function(event) {
    var modal = document.getElementById("deleteModal");
    if (event.target == modal) {
      closeModal();
    }
}

function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        const topOffset = 20; // Khoảng cách từ đầu trang
        const elementPosition = section.getBoundingClientRect().top; // Lấy vị trí của phần tử
        const offsetPosition = elementPosition + window.pageYOffset - topOffset; // Tính toán vị trí cuộn

        // Kiểm tra xem jQuery có được sử dụng không
        if (typeof jQuery !== 'undefined') {
            // Nếu có jQuery, sử dụng jQuery để cuộn
            $('html, body').animate({
                scrollTop: offsetPosition
            }, 500); // 500 milliseconds for the animation duration
        } else {
            // Nếu không có jQuery, sử dụng JavaScript thuần túy
            window.scrollTo({
                top: offsetPosition,
                behavior: 'smooth' // Hiệu ứng cuộn mượt mà
            });
        }
    }
}

// Lấy phần tử select cho ngày sinh và năm sinh
const birthDaySelect = document.getElementById("birth-day");
const birthYearSelect = document.getElementById("birth-year");

// Thêm các tùy chọn cho tháng
const birthMonthSelect = document.getElementById("birth-month");
const months = [
    "January", "February", "March", "April", "May", "June", 
    "July", "August", "September", "October", "November", "December"
];
months.forEach((month, index) => {
    const option = document.createElement("option");
    option.value = index + 1;
    option.textContent = month;
    birthMonthSelect.appendChild(option);
});

// Thêm các tùy chọn từ 1 đến 31 cho ngày sinh
for (let i = 1; i <= 31; i++) {
    const option = document.createElement("option");
    option.value = i;
    option.textContent = i;
    birthDaySelect.appendChild(option);
}

// Thêm các tùy chọn cho năm từ 1900 đến 2024
for (let i = 2024; i >= 1900; i--) {
    const option = document.createElement("option");
    option.value = i;
    option.textContent = i;
    birthYearSelect.appendChild(option);
}
