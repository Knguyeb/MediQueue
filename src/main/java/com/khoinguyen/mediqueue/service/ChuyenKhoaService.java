package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.ChuyenKhoa;
import com.khoinguyen.mediqueue.repository.ChuyenKhoaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChuyenKhoaService {
    private final ChuyenKhoaRepository chuyenKhoaRepository;

    public ChuyenKhoaService(ChuyenKhoaRepository chuyenKhoaRepository) {
        this.chuyenKhoaRepository = chuyenKhoaRepository;
    }

    public List<ChuyenKhoa> findAll() { return chuyenKhoaRepository.findAll(); }
    public Optional<ChuyenKhoa> findById(Long id) { return chuyenKhoaRepository.findById(id); }
    public ChuyenKhoa save(ChuyenKhoa c) { return chuyenKhoaRepository.save(c); }
    public void deleteById(Long id) { chuyenKhoaRepository.deleteById(id); }
    public boolean existsById(Long id) { return chuyenKhoaRepository.existsById(id); }
}
