package com.khoinguyen.mediqueue.entity;

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
@Table(name = "phong_kham")
public class PhongKham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_phong_kham")
    private Integer maPhongKham;

    @Column(name = "ten_phong_kham", nullable = false, length = 100)
    private String tenPhongKham; // VD: Phòng Khám Nội 1, Phòng Siêu Âm...

    @Column(name = "so_phong", length = 20)
    private String soPhong; // VD: P101, Tầng 2

    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true: Đang hoạt động, false: Tạm đóng

    @ManyToOne
    @JoinColumn(name = "ma_benh_vien", referencedColumnName = "ma_benh_vien")
    private BenhVien benhVien;

    @OneToOne(mappedBy = "phongKham")
    private BacSi bacSi;

    @ManyToOne
    @JoinColumn(name = "ma_danh_muc", referencedColumnName = "ma_danh_muc")
    private DanhMucChuyenKhoa chuyenKhoa;

    // =================================================================
    // GETTER VÀ SETTER
    // =================================================================
    public Integer getMaPhongKham() { return maPhongKham; }
    public void setMaPhongKham(Integer maPhongKham) { this.maPhongKham = maPhongKham; }

    public String getTenPhongKham() { return tenPhongKham; }
    public void setTenPhongKham(String tenPhongKham) { this.tenPhongKham = tenPhongKham; }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    public BenhVien getBenhVien() { return benhVien; }
    public void setBenhVien(BenhVien benhVien) { this.benhVien = benhVien; }

    public BacSi getBacSi() { return bacSi; }
    public void setBacSi(BacSi bacSi) { this.bacSi = bacSi; }

    public DanhMucChuyenKhoa getChuyenKhoa() { return chuyenKhoa; }
    public void setChuyenKhoa(DanhMucChuyenKhoa chuyenKhoa) { this.chuyenKhoa = chuyenKhoa; }
}