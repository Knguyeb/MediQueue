package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chuyen_khoa")
public class ChuyenKhoa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MaKhoa;
    private String TenKhoa;

    // Getter và Setter
    public Long getMaKhoa() { return MaKhoa; }
    public void setMaKhoa(Long maKhoa) { this.MaKhoa = maKhoa; }
    
    public String getTenKhoa() { return TenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.TenKhoa = tenKhoa; }
}