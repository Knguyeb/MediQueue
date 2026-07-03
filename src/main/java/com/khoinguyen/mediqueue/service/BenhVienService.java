package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.BenhVien;
import com.khoinguyen.mediqueue.repository.BenhVienRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class BenhVienService {

    private final BenhVienRepository benhVienRepository;

    public BenhVienService(BenhVienRepository benhVienRepository) {
        this.benhVienRepository = benhVienRepository;
    }

    public List<BenhVien> findAll() {
        return benhVienRepository.findAll();
    }

    public Optional<BenhVien> findById(Integer id) {
        return benhVienRepository.findById(id);
    }

    public BenhVien save(BenhVien benhVien) {
        return benhVienRepository.save(benhVien);
    }

    public void deleteById(Integer id) {
        benhVienRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return benhVienRepository.existsById(id);
    }

    public Page<BenhVien> timKiemVaPhanTrang(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return benhVienRepository.findAll(pageable);
        }
        // Gọi hàm mới và truyền từ khóa vào cả 2 chỗ (để tìm trong Tên HOẶC Địa chỉ)
        return benhVienRepository.findByTenBenhVienContainingIgnoreCaseOrDiaChiContainingIgnoreCase(keyword, keyword, pageable);
    }
}