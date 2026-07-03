package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phong_kham")
public class PhongKham {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MaPhong;
    private String TenPhong;
    @ManyToOne @JoinColumn(name = "ma_benh_vien") 
    private BenhVien BenhVien;
    
    // Getter và Setter
    public Long getMaPhong() { return MaPhong; }
    public void setMaPhong(Long maPhong) { this.MaPhong = maPhong; }

    public String getTenPhong() { return TenPhong; }
    public void setTenPhong(String tenPhong) { this.TenPhong = tenPhong; }
    
    public BenhVien getBenhVien() { return BenhVien; }
    public void setBenhVien(BenhVien benhVien) { this.BenhVien = benhVien; }
}
