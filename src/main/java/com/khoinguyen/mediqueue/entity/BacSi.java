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
@Table(name = "bac_si")
public class BacSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_bac_si")
    private Integer maBacSi;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;
    
    // Đã thêm trường Email theo yêu cầu
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh; // true = Nam, false = Nữ

    // =================================================================
    // CÁC MỐI QUAN HỆ (RELATIONSHIPS)
    // =================================================================

    // 1. Một Bác sĩ CÓ MỘT Tài khoản 
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ma_tai_khoan", referencedColumnName = "ma_tai_khoan")
    private TaiKhoan taiKhoan;

    // 2. Nhiều Bác sĩ thuộc về MỘT Bệnh viện
    @ManyToOne
    @JoinColumn(name = "ma_benh_vien", referencedColumnName = "ma_benh_vien")
    private BenhVien benhVien;

    // 3. Nhiều Bác sĩ thuộc về MỘT Chuyên khoa
    @ManyToOne
    @JoinColumn(name = "ma_danh_muc", referencedColumnName = "ma_danh_muc")
    private DanhMucChuyenKhoa chuyenKhoa;

    // 4. Nhiều Bác sĩ có thể ngồi MỘT Phòng khám (Hoặc 1 Bác sĩ - 1 Phòng khám tùy logic)
    @OneToOne
    @JoinColumn(name = "ma_phong_kham", referencedColumnName = "ma_phong_kham")
    private PhongKham phongKham;

    // =================================================================
    // GETTER VÀ SETTER
    // =================================================================

    public Integer getMaBacSi() { return maBacSi; }
    public void setMaBacSi(Integer maBacSi) { this.maBacSi = maBacSi; }

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

    public DanhMucChuyenKhoa getChuyenKhoa() { return chuyenKhoa; }
    public void setChuyenKhoa(DanhMucChuyenKhoa chuyenKhoa) { this.chuyenKhoa = chuyenKhoa; }

    public PhongKham getPhongKham() { return phongKham; }
    public void setPhongKham(PhongKham phongKham) { this.phongKham = phongKham; }
}