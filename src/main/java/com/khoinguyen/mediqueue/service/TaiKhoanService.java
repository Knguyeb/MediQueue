package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.repository.TaiKhoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;

    public TaiKhoanService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    public List<TaiKhoan> findAll() { return taiKhoanRepository.findAll(); }
    public Optional<TaiKhoan> findById(Long id) { return taiKhoanRepository.findById(id); }
    public TaiKhoan save(TaiKhoan t) { return taiKhoanRepository.save(t); }
    public void deleteById(Long id) { taiKhoanRepository.deleteById(id); }
    public boolean existsById(Long id) { return taiKhoanRepository.existsById(id); }
}
