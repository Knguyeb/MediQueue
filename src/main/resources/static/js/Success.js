document.addEventListener("DOMContentLoaded", function () {
    const successEl = document.getElementById("successMessage");
    const successMessage = successEl?.value?.trim();

    // Cấu hình chung cho Dark Mode
    const darkModeConfig = {
        background: '#1e1e1e', // Nền đen nhám
        color: '#ffffff',      // Chữ trắng
        backdrop: `rgba(0,0,0,0.4)` // Làm tối vùng bên ngoài popup
    };

    if (successMessage && successMessage !== "null" && successMessage !== "" && successMessage !== "undefined") {
        Swal.fire({
            ...darkModeConfig,
            icon: 'success',
            title: 'Thành công',
            text: successMessage,
            showConfirmButton: false,
            timer: 2000
        });
    }
});