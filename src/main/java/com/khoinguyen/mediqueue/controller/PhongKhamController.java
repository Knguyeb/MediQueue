package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.PhongKham;
import com.khoinguyen.mediqueue.service.PhongKhamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/phongkham")
@CrossOrigin
public class PhongKhamController {
    private final PhongKhamService phongKhamService;

    public PhongKhamController(PhongKhamService phongKhamService) {
        this.phongKhamService = phongKhamService;
    }

    @GetMapping
    public List<PhongKham> getAll() { return phongKhamService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<PhongKham> getById(@PathVariable Long id) {
        Optional<PhongKham> opt = phongKhamService.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PhongKham> create(@RequestBody PhongKham p) {
        PhongKham saved = phongKhamService.save(p);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhongKham> update(@PathVariable Long id, @RequestBody PhongKham p) {
        Optional<PhongKham> opt = phongKhamService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        PhongKham existing = p;
        PhongKham saved = phongKhamService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!phongKhamService.existsById(id)) return ResponseEntity.notFound().build();
        phongKhamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
