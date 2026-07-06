package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tai_khoan")
    private Integer maTaiKhoan;

    @Column(name = "ten_dang_nhap", nullable = false, unique = true, length = 100)
    private String tenDangNhap; // Có thể là Số điện thoại hoặc Email

    @Column(name = "mat_khau", length = 255, nullable = false)
    private String matKhau;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro", nullable = false, length = 20)
    private VaiTro vaiTro;

    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true = Đang hoạt động, false = Bị khóa

    // =================================================================
    // GETTER VÀ SETTER
    // =================================================================

    public Integer getMaTaiKhoan() { return maTaiKhoan; }
    public void setMaTaiKhoan(Integer maTaiKhoan) { this.maTaiKhoan = maTaiKhoan; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public VaiTro getVaiTro() { return vaiTro; }
    public void setVaiTro(VaiTro vaiTro) { this.vaiTro = vaiTro; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }
}