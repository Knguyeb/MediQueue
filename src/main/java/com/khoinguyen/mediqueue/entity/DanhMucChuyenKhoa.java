package com.khoinguyen.mediqueue.entity;

import java.util.List; // Nhớ import List

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "danh_muc_chuyen_khoa")
public class DanhMucChuyenKhoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_danh_muc")
    private Integer maDanhMuc;

    @Column(name = "ten_danh_muc", nullable = false, unique = true)
    private String tenDanhMuc; // Ví dụ: "Nội Tim Mạch", "Nhi Khoa"

    // =================================================================
    // CÁC MỐI QUAN HỆ (CHỈ DÙNG MAPPEDBY, KHÔNG DÙNG JOINCOLUMN Ở ĐÂY)
    // =================================================================
    
    // 1 Chuyên khoa có nhiều Phòng khám
    @OneToMany(mappedBy = "chuyenKhoa")
    private List<PhongKham> danhSachPhongKham;

    // 1 Chuyên khoa có nhiều Bác sĩ
    @OneToMany(mappedBy = "chuyenKhoa")
    private List<BacSi> danhSachBacSi;

    // =================================================================
    // GETTER VÀ SETTER
    // =================================================================

    public Integer getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(Integer maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }

    public List<PhongKham> getDanhSachPhongKham() { return danhSachPhongKham; }
    public void setDanhSachPhongKham(List<PhongKham> danhSachPhongKham) { this.danhSachPhongKham = danhSachPhongKham; }

    public List<BacSi> getDanhSachBacSi() { return danhSachBacSi; }
    public void setDanhSachBacSi(List<BacSi> danhSachBacSi) { this.danhSachBacSi = danhSachBacSi; }
}