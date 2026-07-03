class TablePaginator {
    constructor(options) {
        this.table = document.getElementById(options.tableId);
        this.paginationContainer = document.getElementById(options.paginationId);
        this.visibleCountLabel = document.getElementById(options.visibleCountId);
        this.noResultsDiv = document.getElementById(options.noResultsId);

        this.rowsPerPage = options.rowsPerPage || 10;
        this.currentPage = 1;

        if (!this.table) return;

        this.tbody = this.table.querySelector("tbody");
        // Lưu toàn bộ dữ liệu gốc
        this.allRows = Array.from(this.tbody.querySelectorAll("tr"));
        // Mảng chứa các dòng đang được kích hoạt (có thể đã bị bộ lọc tìm kiếm cắt bớt)
        this.activeRows = [...this.allRows]; 

        this.renderTable();
    }

    // Hàm mở rộng để nhận dữ liệu mới từ tính năng Tìm kiếm
    updateData(filteredRows) {
        this.activeRows = filteredRows;
        this.currentPage = 1; // Reset về trang 1 khi có dữ liệu mới
        this.renderTable();
    }

    renderTable() {
        // 1. Ẩn tất cả các dòng gốc
        this.allRows.forEach(row => row.style.display = "none");

        // 2. Tính toán để cắt mảng dữ liệu cho trang hiện tại
        const startIndex = (this.currentPage - 1) * this.rowsPerPage;
        const endIndex = startIndex + this.rowsPerPage;
        const rowsToShow = this.activeRows.slice(startIndex, endIndex);

        // 3. Hiển thị các dòng thuộc trang hiện tại
        rowsToShow.forEach(row => row.style.display = "");

        // 4. Cập nhật số đếm
        if (this.visibleCountLabel) this.visibleCountLabel.innerText = this.activeRows.length;

        // 5. Ẩn/Hiện thông báo "Không tìm thấy"
        if (this.activeRows.length === 0) {
            if (this.noResultsDiv) this.noResultsDiv.style.display = "block";
            this.table.style.display = "none";
        } else {
            if (this.noResultsDiv) this.noResultsDiv.style.display = "none";
            this.table.style.display = "table";
        }
        
        this.renderPagination();
    }

    renderPagination() {
        if (!this.paginationContainer) return;
        this.paginationContainer.innerHTML = "";
        const totalPages = Math.max(1, Math.ceil(this.activeRows.length / this.rowsPerPage));

        const createPageButton = (text, pageIndex, isActive = false, isDisabled = false) => {
            const li = document.createElement("li");
            li.className = `page-item ${isActive ? "active" : ""} ${isDisabled ? "disabled" : ""}`;
            li.innerHTML = `<a class="page-link" style="cursor: ${isDisabled ? "default" : "pointer"};">${text}</a>`;
            if (!isDisabled && pageIndex !== null) {
                li.addEventListener("click", () => {
                    this.currentPage = pageIndex;
                    this.renderTable();
                });
            }
            this.paginationContainer.appendChild(li);
        };

        createPageButton('<i class="bi bi-chevron-left"></i>', this.currentPage - 1, false, this.currentPage === 1);
        
        for (let i = 1; i <= totalPages; i++) {
            if (i === 1 || i === totalPages || (i >= this.currentPage - 1 && i <= this.currentPage + 1)) {
                createPageButton(i, i, i === this.currentPage);
            } else if (i === this.currentPage - 2 || i === this.currentPage + 2) {
                createPageButton("...", null, false, true);
            }
        }
        
        createPageButton('<i class="bi bi-chevron-right"></i>', this.currentPage + 1, false, this.currentPage === totalPages);
    }
}