package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "phieu_kham")
public class PhieuKham {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MaPhieu;
    
    @ManyToOne @JoinColumn(name = "ma_benh_nhan") private BenhNhan BenhNhan;
    @ManyToOne @JoinColumn(name = "ma_bac_si") private BacSi BacSi;
    @ManyToOne @JoinColumn(name = "ma_phong") private PhongKham PhongKham;
    
    private LocalDateTime ThoiGianHen;
    private String TrangThai; // "CHO_KHAM", "DANG_KHAM", "DA_XONG"

    // Getter và Setter
    public Long getMaPhieu() { return MaPhieu; }
    public void setMaPhieu(Long maPhieu) { this.MaPhieu = maPhieu; }

    public BenhNhan getBenhNhan() { return BenhNhan; }
    public void setBenhNhan(BenhNhan benhNhan) { this.BenhNhan = benhNhan; }

    public BacSi getBacSi() { return BacSi; }
    public void setBacSi(BacSi bacSi) { this.BacSi = bacSi; }

    public PhongKham getPhongKham() { return PhongKham; }
    public void setPhongKham(PhongKham phongKham) { this.PhongKham = phongKham; }

    public LocalDateTime getThoiGianHen() { return ThoiGianHen; }
    public void setThoiGianHen(LocalDateTime thoiGianHen) { this.ThoiGianHen = thoiGianHen; }
    
    public String getTrangThai() { return TrangThai; }
    public void setTrangThai(String trangThai) { this.TrangThai = trangThai; }
}
