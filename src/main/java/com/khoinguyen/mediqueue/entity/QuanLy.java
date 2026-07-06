package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quan_ly")
public class QuanLy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_quan_ly")
    private Integer maQuanLy;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh; // true = Nam, false = Nữ

    // =================================================================
    // CÁC MỐI QUAN HỆ (RELATIONSHIPS)
    // =================================================================

    // 1. Một Quản lý CÓ MỘT Tài khoản
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ma_tai_khoan", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan taiKhoan;

    // 2. Nhiều Quản lý có thể thuộc về MỘT Bệnh viện (hoặc 1 Quản lý - 1 Bệnh viện tùy logic của bạn, ở đây dùng N-1 cho linh hoạt)
    @ManyToOne
    @JoinColumn(name = "ma_benh_vien", referencedColumnName = "ma_benh_vien")
    private BenhVien benhVien;

    // =================================================================
    // GETTER VÀ SETTER
    // =================================================================
    public Integer getMaQuanLy() { return maQuanLy; }
    public void setMaQuanLy(Integer maQuanLy) { this.maQuanLy = maQuanLy; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(Boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }

    public BenhVien getBenhVien() { return benhVien; }
    public void setBenhVien(BenhVien benhVien) { this.benhVien = benhVien; }
}