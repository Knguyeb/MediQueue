package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.TaiKhoan;
import com.khoinguyen.mediqueue.service.TaiKhoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/taikhoan")
@CrossOrigin
public class TaiKhoanController {
    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @GetMapping
    public List<TaiKhoan> getAll() { return taiKhoanService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<TaiKhoan> getById(@PathVariable Long id) {
        Optional<TaiKhoan> opt = taiKhoanService.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaiKhoan> create(@RequestBody TaiKhoan t) {
        TaiKhoan saved = taiKhoanService.save(t);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaiKhoan> update(@PathVariable Long id, @RequestBody TaiKhoan t) {
        Optional<TaiKhoan> opt = taiKhoanService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        TaiKhoan existing = t;
        TaiKhoan saved = taiKhoanService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!taiKhoanService.existsById(id)) return ResponseEntity.notFound().build();
        taiKhoanService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
