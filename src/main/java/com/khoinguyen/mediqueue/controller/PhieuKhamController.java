package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.PhieuKham;
import com.khoinguyen.mediqueue.service.PhieuKhamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/phieukham")
@CrossOrigin
public class PhieuKhamController {
    private final PhieuKhamService phieuKhamService;

    public PhieuKhamController(PhieuKhamService phieuKhamService) {
        this.phieuKhamService = phieuKhamService;
    }

    @GetMapping
    public List<PhieuKham> getAll() { return phieuKhamService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuKham> getById(@PathVariable Long id) {
        Optional<PhieuKham> opt = phieuKhamService.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PhieuKham> create(@RequestBody PhieuKham p) {
        PhieuKham saved = phieuKhamService.save(p);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuKham> update(@PathVariable Long id, @RequestBody PhieuKham p) {
        Optional<PhieuKham> opt = phieuKhamService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        PhieuKham existing = p;
        PhieuKham saved = phieuKhamService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!phieuKhamService.existsById(id)) return ResponseEntity.notFound().build();
        phieuKhamService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
