document.addEventListener("DOMContentLoaded", function () {
    const errorEl = document.getElementById("errorMessage");
    const errorMessage = errorEl?.value?.trim();

    // Cấu hình chung cho Dark Mode
    const darkModeConfig = {
        background: '#1e1e1e', // Nền đen nhám
        color: '#ffffff',      // Chữ trắng
        backdrop: `rgba(0,0,0,0.4)` // Làm tối vùng bên ngoài popup
    };

    if (errorMessage && errorMessage !== "null" && errorMessage !== "" && errorMessage !== "undefined") {
        Swal.fire({
            ...darkModeConfig,
            icon: 'error',
            title: 'Lỗi',
            text: errorMessage,
            confirmButtonText: "Đóng",
            confirmButtonColor: '#ffc107' // Màu vàng cho chuẩn Luxury
        });
    }
});