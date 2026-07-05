class TableSearcher {
    constructor(options) {
        this.searchInput = document.getElementById(options.searchInputId);
        this.paginator = options.paginator; 

        if (!this.searchInput || !this.paginator) return;

        this.init();
    }

    removeVietnameseTones(str) {
        return str.normalize('NFD')
                  .replace(/[\u0300-\u036f]/g, '')
                  .replace(/đ/g, 'd').replace(/Đ/g, 'D');
    }

    init() {
        this.searchInput.addEventListener("input", (e) => {
            const rawKeyword = e.target.value.toLowerCase().trim();
            const keyword = this.removeVietnameseTones(rawKeyword);
            
            // Lấy dữ liệu gốc từ bên class Phân Trang
            const allRows = this.paginator.allRows;
            
            // Tiến hành lọc
            const filteredRows = allRows.filter(row => {
                // ĐÃ SỬA: Đổi innerText thành textContent để đọc được cả những dòng đang bị ẩn do phân trang
                const rowText = this.removeVietnameseTones(row.textContent.toLowerCase());
                return rowText.includes(keyword);
            });
            
            // Gửi dữ liệu đã lọc sang class Phân Trang để vẽ lại bảng
            this.paginator.updateData(filteredRows);
        });
    }

    // Hàm xóa tìm kiếm (gọi khi bấm nút "Hiển thị tất cả")
    clearSearch() {
        if (this.searchInput) {
            this.searchInput.value = "";
            this.paginator.updateData(this.paginator.allRows);
        }
    }
}