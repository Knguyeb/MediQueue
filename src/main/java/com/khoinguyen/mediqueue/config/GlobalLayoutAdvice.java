package com.khoinguyen.mediqueue.config; // Thay đổi theo package của bạn

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalLayoutAdvice {

    // Hàm này sẽ tự động chạy trước MỌI Controller
    @ModelAttribute
    public void addLayoutToModel(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String quyen = (String) session.getAttribute("QuyenHan");

        // Giao diện mặc định (dành cho Khách hoặc Bệnh nhân)
        String layoutName = "layout/_layout"; 

        // Kiểm tra quyền và đổi tên Layout
        if (quyen != null) {
            if (quyen.equals("ADMIN")) {
                layoutName = "layout/_layoutAdmin";
            } else if (quyen.equals("BAC_SI") || quyen.equals("QUAN_LY")) {
                layoutName = "layout/_layoutQLBS";
            }
        }

        // Bơm biến "dynamicLayout" vào Model để HTML xài
        model.addAttribute("dynamicLayout", layoutName);
    }
}