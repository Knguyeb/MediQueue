package com.khoinguyen.mediqueue.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "tai_khoan")
@Inheritance(strategy = InheritanceType.JOINED) // Kỹ thuật tạo bảng Cha-Con chuyên nghiệp
public abstract class TaiKhoan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    
    @Column(unique = true, nullable = false)
    private String TenDangNhap;
    
    @Column(nullable = false)
    private String MatKhau;
    
    private String HoTen;
    
    private String QuyenHan; // "BAC_SI", "BENH_NHAN", "QUAN_LY"

    // Getter và Setter
    public Long getID() { return ID; }
    public void setID(Long ID) { this.ID = ID; }

    public String getTenDangNhap() { return TenDangNhap; }
    public void setTenDangNhap(String TenDangNhap) { this.TenDangNhap = TenDangNhap; }

    public String getMatKhau() { return MatKhau; }
    public void setMatKhau(String MatKhau) { this.MatKhau = MatKhau; }

    public String getHoTen() { return HoTen; }
    public void setHoTen(String HoTen) { this.HoTen = HoTen; }

    public String getQuyenHan() { return QuyenHan; }
    public void setQuyenHan(String QuyenHan) { this.QuyenHan = QuyenHan; }
}