package com.khoinguyen.mediqueue.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "benh_nhan")
public class BenhNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_benh_nhan")
    private Integer maBenhNhan;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh; // true = Nam, false = Nữ

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "dia_chi", length = 255)
    private String diaChi;

    // =================================================================
    // CÁC MỐI QUAN HỆ (RELATIONSHIPS)
    // =================================================================

    // Một Bệnh nhân CÓ MỘT Tài khoản
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ma_tai_khoan", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan taiKhoan;

    // =================================================================
    // GETTER VÀ SETTER
    // =================================================================
    public Integer getMaBenhNhan() { return maBenhNhan; }
    public void setMaBenhNhan(Integer maBenhNhan) { this.maBenhNhan = maBenhNhan; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(Boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public TaiKhoan getTaiKhoan() { return taiKhoan; }
    public void setTaiKhoan(TaiKhoan taiKhoan) { this.taiKhoan = taiKhoan; }
}