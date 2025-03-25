document.addEventListener('DOMContentLoaded', function() {
    initializeModals(); // Khởi tạo các modal ngay khi trang sẵn sàng
    initializeDeleteAccountModalEvents(); // Khởi tạo sự kiện cho modal Delete Account

    // Đóng modal khi nhấp ra ngoài
    window.onclick = function(event) {
        var modals = [
            "deleteModal", "addAdminModal",
        ];
        modals.forEach(function(modalId) {
            var modal = document.getElementById(modalId);
            if (modal && event.target == modal) {
                modal.style.display = 'none';
            }
        });
    };
});

// Khởi tạo tất cả modal cần thiết
function initializeModals() {
    initializeAddAdminInfoModalEvents();
}

// Khởi tạo các sự kiện cho modal "Delete Account"
function initializeDeleteAccountModalEvents() {
    var deleteModal = document.getElementById("deleteModal");

    if (!deleteModal) {
        console.error("Modal element 'deleteModal' not found!");
        return;
    }

    document.querySelectorAll('.btn-delete-account').forEach(button => {
        button.addEventListener('click', function() {
            deleteModal.style.display = 'block';
        });
    });

    deleteModal.querySelector('.btn-cancel').addEventListener('click', function() {
        deleteModal.style.display = 'none';
    });
}

// Khởi tạo sự kiện cho modal "Add Admin"
function initializeAddAdminInfoModalEvents() {
    var modal = document.getElementById("addAdminModal");
    if (!modal) return;

    document.querySelectorAll('.btn-add-admin').forEach(button => {
        button.addEventListener('click', function() {
            modal.style.display = 'block';
        });
    });

    modal.querySelector('.btn-cancel-add').addEventListener('click', function() {
        modal.style.display = 'none';
    });
}

// Hàm chuyển đổi hiển thị menu items
function toggleMenuItems() {
    var menuItems = document.getElementById("menuItems");
    var arrow = document.getElementById("arrow");

    if (menuItems.style.display === "block") {
        menuItems.style.display = "none";
    } else {
        menuItems.style.display = "block";
    }

    if (arrow.classList.contains('rotate-down')) {
        arrow.classList.remove('rotate-down');
    } else {
        arrow.classList.add('rotate-down');
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

// Khi trang load lần đầu, đảm bảo menu luôn hiển thị
window.onload = function() {
    var menuItems = document.getElementById("menuItems");
    var arrow = document.getElementById("arrow");

    menuItems.style.display = "block";
    arrow.classList.remove('rotate-down');
};

// Hàm cuộn đến phần được chỉ định
function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        const topOffset = 20;
        const elementPosition = section.getBoundingClientRect().top;
        const offsetPosition = elementPosition + window.pageYOffset - topOffset;

        if (typeof jQuery !== 'undefined') {
            $('html, body').animate({
                scrollTop: offsetPosition
            }, 500);
        } else {
            window.scrollTo({
                top: offsetPosition,
                behavior: 'smooth'
            });
        }
    }
}
