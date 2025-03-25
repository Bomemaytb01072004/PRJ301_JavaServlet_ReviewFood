document.addEventListener('DOMContentLoaded', function () {
    const phoneLink = document.getElementById('phone-link');
    const phonePopup = document.getElementById('phone-popup');
    const closePhonePopup = document.querySelector('.close-phone-popup');

    // Hiển thị pop-up khi bấm vào link Phone
    phoneLink.addEventListener('click', function (event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định
        phonePopup.style.display = 'block'; // Hiển thị pop-up
    });

    // Đóng pop-up khi nhấn nút close
    closePhonePopup.addEventListener('click', function () {
        phonePopup.style.display = 'none';
    });

    // Đóng pop-up khi nhấn bên ngoài pop-up
    window.addEventListener('click', function (event) {
        if (event.target === phonePopup) {
            phonePopup.style.display = 'none';
        }
    });
});

function showPictureSection() {
    document.getElementById('picture-section').style.display = 'block';
    document.getElementById('review-section').style.display = 'none';
    document.getElementById('picture-tab').classList.add('choose');
    document.getElementById('review-tab').classList.remove('choose');
}

function showReviewSection() {
    document.getElementById('picture-section').style.display = 'none';
    document.getElementById('review-section').style.display = 'block';
    document.getElementById('review-tab').classList.add('choose');
    document.getElementById('picture-tab').classList.remove('choose');
}

function updateRatingValue(type, value) {
    document.getElementById(`rating-value-${type}`).innerText = value;
}