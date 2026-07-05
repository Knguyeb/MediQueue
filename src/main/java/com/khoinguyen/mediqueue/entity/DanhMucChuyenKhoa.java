package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    // GETTER VÀ SETTER
    // =================================================================

    public Integer getMaDanhMuc() { return maDanhMuc; }
    public void setMaDanhMuc(Integer maDanhMuc) { this.maDanhMuc = maDanhMuc; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }
}