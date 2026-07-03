function confirmDelete(id) {
    Swal.fire({
        title: 'Bạn có chắc muốn xóa?',
        text: "Dữ liệu sẽ không thể khôi phục!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc3545', // Màu đỏ sang trọng cho nút Xóa
        cancelButtonColor: '#6c757d',  // Màu xám trung tính cho nút Hủy
        confirmButtonText: 'Xóa ngay',
        cancelButtonText: 'Hủy bỏ',
        background: '#1e1e1e',         // Nền đen nhám đồng bộ hệ thống
        color: '#ffffff',              // Chữ trắng
        backdrop: `rgba(0,0,0,0.6)`    // Làm tối nền phía sau khi hiện popup
    }).then((result) => {
        if (result.isConfirmed) {
            // Thực thi lệnh xóa ngay lập tức khi xác nhận
            document.getElementById('deleteForm-' + id).submit();
        }
    });
}