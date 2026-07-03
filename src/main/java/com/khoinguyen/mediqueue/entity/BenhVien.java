package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "benh_vien")
public class BenhVien {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MaBenhVien;
    private String TenBenhVien;
    private String DiaChi;
    private String SoDienThoai;
    private String Email;
    private double ViDo;
    private double KinhDo;
    private String HinhAnh;
    
    @OneToMany(mappedBy = "BenhVien", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BacSi> BacSis = new ArrayList<>();
    // Getter và Setter
    public Long getMaBenhVien() { return MaBenhVien; }
    public void setMaBenhVien(Long maBenhVien) { this.MaBenhVien = maBenhVien; }

    public String getTenBenhVien() { return TenBenhVien; }
    public void setTenBenhVien(String tenBenhVien) { this.TenBenhVien = tenBenhVien; }
    
    public String getDiaChi() { return DiaChi; }
    public void setDiaChi(String diaChi) { this.DiaChi = diaChi; }

    public String getSoDienThoai() { return SoDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.SoDienThoai = soDienThoai; }

    public String getEmail() { return Email; }
    public void setEmail(String email) { this.Email = email; }

    public double getViDo() { return ViDo; }
    public void setViDo(double viDo) { this.ViDo = viDo; }

    public double getKinhDo() { return KinhDo; }
    public void setKinhDo(double kinhDo) { this.KinhDo = kinhDo; }

    public String getHinhAnh() { return HinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.HinhAnh = hinhAnh; }
    
    public List<BacSi> getBacSis() { return BacSis; }
    public void setBacSis(List<BacSi> bacSis) { this.BacSis = bacSis; }
}