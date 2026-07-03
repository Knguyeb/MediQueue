package com.khoinguyen.mediqueue.controller;

import com.khoinguyen.mediqueue.entity.BenhNhan;
import com.khoinguyen.mediqueue.service.BenhNhanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/benhnhan")
@CrossOrigin
public class BenhNhanController {
    private final BenhNhanService benhNhanService;

    public BenhNhanController(BenhNhanService benhNhanService) {
        this.benhNhanService = benhNhanService;
    }

    @GetMapping
    public List<BenhNhan> getAll() { return benhNhanService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<BenhNhan> getById(@PathVariable Long id) {
        Optional<BenhNhan> opt = benhNhanService.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BenhNhan> create(@RequestBody BenhNhan b) {
        BenhNhan saved = benhNhanService.save(b);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BenhNhan> update(@PathVariable Long id, @RequestBody BenhNhan b) {
        Optional<BenhNhan> opt = benhNhanService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        BenhNhan existing = b;
        BenhNhan saved = benhNhanService.save(existing);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!benhNhanService.existsById(id)) return ResponseEntity.notFound().build();
        benhNhanService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
