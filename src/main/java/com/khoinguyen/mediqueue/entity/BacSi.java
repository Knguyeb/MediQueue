package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bac_si")
public class BacSi extends TaiKhoan {
    @ManyToOne @JoinColumn(name = "ma_khoa") 
    private ChuyenKhoa ChuyenKhoa;
    
    @ManyToOne(optional = false) 
    @JoinColumn(name = "ma_benh_vien", nullable = false) 
    private BenhVien BenhVien;

    // Getter và Setter
    public ChuyenKhoa getChuyenKhoa() { return ChuyenKhoa; }
    public void setChuyenKhoa(ChuyenKhoa chuyenKhoa) { this.ChuyenKhoa = chuyenKhoa; }
    
    public BenhVien getBenhVien() { return BenhVien; }
    public void setBenhVien(BenhVien benhVien) { this.BenhVien = benhVien; }
}