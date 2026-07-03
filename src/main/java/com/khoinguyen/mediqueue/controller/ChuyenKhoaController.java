package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.ChuyenKhoa;
import com.khoinguyen.mediqueue.service.ChuyenKhoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chuyenkhoa")
@CrossOrigin
public class ChuyenKhoaController {
    private final ChuyenKhoaService chuyenKhoaService;

    public ChuyenKhoaController(ChuyenKhoaService chuyenKhoaService) {
        this.chuyenKhoaService = chuyenKhoaService;
    }

    @GetMapping
    public List<ChuyenKhoa> getAll() { return chuyenKhoaService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<ChuyenKhoa> getById(@PathVariable Long id) {
        Optional<ChuyenKhoa> opt = chuyenKhoaService.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ChuyenKhoa> create(@RequestBody ChuyenKhoa c) {
        ChuyenKhoa saved = chuyenKhoaService.save(c);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChuyenKhoa> update(@PathVariable Long id, @RequestBody ChuyenKhoa c) {
        Optional<ChuyenKhoa> opt = chuyenKhoaService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        ChuyenKhoa existing = c;
        ChuyenKhoa saved = chuyenKhoaService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!chuyenKhoaService.existsById(id)) return ResponseEntity.notFound().build();
        chuyenKhoaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
