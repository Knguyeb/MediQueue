package com.khoinguyen.mediqueue.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "benh_nhan")
public class BenhNhan extends TaiKhoan {
    private LocalDate NgaySinh;

    // Getter và Setter
    public LocalDate getNgaySinh() { return NgaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.NgaySinh = ngaySinh; }
}
