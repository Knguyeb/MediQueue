/**
 * Xử lý popup xác nhận đăng xuất sử dụng SweetAlert2
 * Giao diện được tùy chỉnh để đồng bộ với Dark Theme của hệ thống
 */
function confirmLogoutSwal(logoutUrl) {
    Swal.fire({
        title: 'Xác nhận đăng xuất?',
        text: "Bạn có chắc chắn muốn rời khỏi hệ thống?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ff4d4d',   
        cancelButtonColor: '#3f3f46',    
        confirmButtonText: '<i class="fas fa-sign-out-alt me-2"></i> Đăng xuất',
        cancelButtonText: 'Hủy',
        background: '#27272a',           
        color: '#e4e4e7',                
        backdrop: `rgba(0, 0, 0, 0.6)`,  
        heightAuto: false,               // <--- QUAN TRỌNG: Sửa lỗi đẩy Header / dãn Layout
        customClass: {
            popup: 'border border-secondary', 
        }
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: 'Đang đăng xuất...',
                text: 'Vui lòng chờ trong giây lát.',
                background: '#27272a',
                color: '#e4e4e7',
                allowOutsideClick: false,
                showConfirmButton: false,
                heightAuto: false,       // <--- QUAN TRỌNG: Thêm ở cả popup Loading
                didOpen: () => {
                    Swal.showLoading();
                }
            });
            
            // Chuyển hướng đến @GetMapping("/logout") trong AuthController.java
            window.location.href = logoutUrl;
        }
    });
}