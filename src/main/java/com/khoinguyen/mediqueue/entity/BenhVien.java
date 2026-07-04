package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "benh_vien")
public class BenhVien {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_benh_vien")
    private Integer maBenhVien;

    @Column(name = "ten_benh_vien", nullable = false)
    private String tenBenhVien;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "email")
    private String email;

    @Column(name = "vi_do")
    private double viDo;

    @Column(name = "kinh_do")
    private double kinhDo;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    // =================================================================
    // GETTER VÀ SETTER (Tên hàm vẫn giữ nguyên vì chữ cái đầu biến tự động viết hoa)
    // =================================================================
    
    public Integer getMaBenhVien() { return maBenhVien; }
    public void setMaBenhVien(Integer maBenhVien) { this.maBenhVien = maBenhVien; }

    public String getTenBenhVien() { return tenBenhVien; }
    public void setTenBenhVien(String tenBenhVien) { this.tenBenhVien = tenBenhVien; }
    
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getViDo() { return viDo; }
    public void setViDo(double viDo) { this.viDo = viDo; }

    public double getKinhDo() { return kinhDo; }
    public void setKinhDo(double kinhDo) { this.kinhDo = kinhDo; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
}