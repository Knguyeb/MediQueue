package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.DanhMucChuyenKhoa;
import com.khoinguyen.mediqueue.repository.DanhMucChuyenKhoaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhMucChuyenKhoaService {

    private final DanhMucChuyenKhoaRepository danhMucChuyenKhoaRepository;

    public DanhMucChuyenKhoaService(DanhMucChuyenKhoaRepository danhMucChuyenKhoaRepository) {
        this.danhMucChuyenKhoaRepository = danhMucChuyenKhoaRepository;
    }

    public List<DanhMucChuyenKhoa> findAll() {
        return danhMucChuyenKhoaRepository.findAll();
    }

    public Optional<DanhMucChuyenKhoa> findById(Integer id) {
        return danhMucChuyenKhoaRepository.findById(id);
    }

    public DanhMucChuyenKhoa save(DanhMucChuyenKhoa danhMucChuyenKhoa) {
        return danhMucChuyenKhoaRepository.save(danhMucChuyenKhoa);
    }

    public void deleteById(Integer id) {
        danhMucChuyenKhoaRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return danhMucChuyenKhoaRepository.existsById(id);
    }

    public boolean kiemTraTrungTen(String tenDanhMuc) {
        return danhMucChuyenKhoaRepository.existsByTenDanhMucIgnoreCase(tenDanhMuc);
    }

    public Page<DanhMucChuyenKhoa> timKiemVaPhanTrang(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return danhMucChuyenKhoaRepository.findAll(pageable);
        }
        return danhMucChuyenKhoaRepository.findByTenDanhMucContainingIgnoreCase(keyword.trim(), pageable);
    }
}
