package com.khoinguyen.mediqueue.service;

import com.khoinguyen.mediqueue.entity.BacSi;
import com.khoinguyen.mediqueue.repository.BacSiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BacSiService {
    private final BacSiRepository bacSiRepository;

    public BacSiService(BacSiRepository bacSiRepository) {
        this.bacSiRepository = bacSiRepository;
    }

    public List<BacSi> findAll() { return bacSiRepository.findAll(); }
    public Optional<BacSi> findById(Long id) { return bacSiRepository.findById(id); }
    
    public BacSi save(BacSi b) {
        validateBacSi(b);
        return bacSiRepository.save(b);
    }
    
    private void validateBacSi(BacSi b) {
        if (b.getBenhVien() == null) {
            throw new IllegalArgumentException("Bác sĩ phải được gán cho một bệnh viện");
        }
    }
    
    public void deleteById(Long id) { bacSiRepository.deleteById(id); }
    public boolean existsById(Long id) { return bacSiRepository.existsById(id); }
}
