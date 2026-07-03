// 1. BIẾN TOÀN CỤC (Lưu trữ danh sách file ảnh chuẩn bị upload)
const dt = new DataTransfer();

// 2. HÀM XỬ LÝ KHI NGƯỜI DÙNG CHỌN FILE
function handleFileSelect(event) {
    const files = event.target.files;
    const container = document.getElementById("imageThumbnailsContainer");
    const noImageText = document.getElementById("noImageText");

    // Ẩn dòng chữ "Chưa có hình ảnh nào" nếu có file được chọn
    if (files.length > 0 && noImageText) {
        noImageText.style.display = 'none';
    }

    // Lặp qua từng file để xử lý
    Array.from(files).forEach(file => {
        // Chỉ chấp nhận file ảnh
        if (!file.type.startsWith('image/')) return;

        // Thêm file vào biến toàn cục dt
        dt.items.add(file);
        
        // Đọc file để hiển thị Preview
        const reader = new FileReader();
        reader.onload = function(e) {
            createThumbnail(e.target.result, container, file);
        };
        reader.readAsDataURL(file);
    });

    // Cập nhật lại danh sách file cho thẻ input ẩn
    document.getElementById('fileInput').files = dt.files;
}

// 3. HÀM TẠO KHUNG PREVIEW ẢNH (Kèm nút xóa)
function createThumbnail(src, container, fileObj) {
    const imgWrapper = document.createElement("div");
    imgWrapper.className = "thumbnail-wrapper fade-in-up";

    const deleteBtn = document.createElement("button");
    deleteBtn.className = "btn-delete-image";
    deleteBtn.innerHTML = '<i class="bi bi-x"></i>';
    deleteBtn.type = "button";
    
    // Sự kiện khi bấm nút (X) màu đỏ
    deleteBtn.onclick = function() {
        removeFile(fileObj); // Xóa khỏi danh sách chờ upload
        imgWrapper.remove(); // Xóa khỏi giao diện
        
        // Nếu xóa hết ảnh thì hiện lại dòng chữ "Chưa có hình ảnh nào"
        if (container.querySelectorAll('.thumbnail-wrapper').length === 0) {
            document.getElementById("noImageText").style.display = 'block';
        }
    };

    const img = document.createElement("img");
    img.src = src;
    img.className = "thumbnail-img";

    imgWrapper.appendChild(deleteBtn);
    imgWrapper.appendChild(img);
    container.appendChild(imgWrapper);
}

// 4. HÀM XÓA ẢNH KHỎI DANH SÁCH CHỜ UPLOAD (Giỏ hàng)
function removeFile(fileToRemove) {
    const newDt = new DataTransfer();
    const currentFiles = dt.files;
    
    // Lọc bỏ file cần xóa
    for (let i = 0; i < currentFiles.length; i++) {
        if (currentFiles[i] !== fileToRemove) {
            newDt.items.add(currentFiles[i]);
        }
    }
    
    // Cập nhật lại biến dt
    dt.items.clear();
    for (let i = 0; i < newDt.files.length; i++) {
        dt.items.add(newDt.files[i]);
    }
    
    // Gán lại cho thẻ input
    document.getElementById('fileInput').files = dt.files;
}