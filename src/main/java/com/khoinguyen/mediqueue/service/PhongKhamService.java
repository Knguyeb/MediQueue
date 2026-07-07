package com.khoinguyen.mediqueue.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.khoinguyen.mediqueue.entity.PhongKham;
import com.khoinguyen.mediqueue.repository.PhongKhamRepository;

@Service
public class PhongKhamService {

    private final PhongKhamRepository phongKhamRepository;

    public PhongKhamService(PhongKhamRepository phongKhamRepository) {
        this.phongKhamRepository = phongKhamRepository;
    }

    public List<PhongKham> findAll() {
        return phongKhamRepository.findAll();
    }

    public Optional<PhongKham> findById(Integer id) {
        return phongKhamRepository.findById(id);
    }

    public PhongKham save(PhongKham phongKham) {
        return phongKhamRepository.save(phongKham);
    }

    public void deleteById(Integer id) {
        phongKhamRepository.deleteById(id);
    }

    // Gọi hàm tuỳ chỉnh từ Repository
    public List<PhongKham> layDanhSachTheoBenhVien(Integer maBenhVien) {
        return phongKhamRepository.findByBenhVien_MaBenhVien(maBenhVien);
    }
}