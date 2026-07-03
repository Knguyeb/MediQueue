package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.service.BacSiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bacsi")
@CrossOrigin
public class BacSiController {
    private final BacSiService bacSiService;

    public BacSiController(BacSiService bacSiService) {
        this.bacSiService = bacSiService;
    }

    @GetMapping
    public List<BacSi> getAll() { return bacSiService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<BacSi> getById(@PathVariable Long id) {
        Optional<BacSi> opt = bacSiService.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BacSi b) {
        try {
            BacSi saved = bacSiService.save(b);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody BacSi b) {
        Optional<BacSi> opt = bacSiService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        try {
            BacSi existing = opt.get();
            existing.setTenDangNhap(b.getTenDangNhap());
            existing.setMatKhau(b.getMatKhau());
            existing.setHoTen(b.getHoTen());
            existing.setQuyenHan(b.getQuyenHan());
            existing.setChuyenKhoa(b.getChuyenKhoa());
            existing.setBenhVien(b.getBenhVien());
            BacSi saved = bacSiService.save(existing);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!bacSiService.existsById(id)) return ResponseEntity.notFound().build();
        bacSiService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
