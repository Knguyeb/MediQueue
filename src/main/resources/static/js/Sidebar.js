// Hàm mở/đóng Sidebar
function toggleSidebar() {
    var sidebar = document.getElementById('adminSidebar');
    var overlay = document.getElementById('sidebarOverlay');
    
    // Bật/tắt class 'show'
    sidebar.classList.toggle('show');
    overlay.classList.toggle('show');
}

// Hàm đóng Sidebar khi click ra ngoài vùng tối (overlay)
function closeSidebar() {
    var sidebar = document.getElementById('adminSidebar');
    var overlay = document.getElementById('sidebarOverlay');
    
    // Gỡ class 'show'
    sidebar.classList.remove('show');
    overlay.classList.remove('show');
}