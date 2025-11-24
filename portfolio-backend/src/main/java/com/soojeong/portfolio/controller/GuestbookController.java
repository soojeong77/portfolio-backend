package com.soojeong.portfolio.controller; 

import com.soojeong.portfolio.entity.Guestbook;
import com.soojeong.portfolio.repository.GuestbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/guestbook")
// CORS 오류 해결: allowCredentials가 없는 "*"만 남깁니다.
// 이렇게 하면 모든 출처에서 접근 가능하며, 충돌 문제가 해결됩니다.
@CrossOrigin(origins = "*") 
public class GuestbookController {

    private final GuestbookRepository guestbookRepository;
    private final PasswordEncoder passwordEncoder; 

    // 생성자 주입
    @Autowired
    public GuestbookController(GuestbookRepository guestbookRepository, PasswordEncoder passwordEncoder) {
        this.guestbookRepository = guestbookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =======================================================
    // 1. 방명록 목록 조회 (GET)
    // =======================================================
    @GetMapping
    public List<Guestbook> getGuestbooks() {
        return guestbookRepository.findAllByOrderByCreatedDateDesc();
    }

    // =======================================================
    // 2. 방명록 작성 (POST)
    // =======================================================
    @PostMapping
    public ResponseEntity<Guestbook> addGuestbook(@RequestBody Guestbook newEntry) {
        
        // 1. 클라이언트에서 받은 비밀번호를 해싱합니다.
        // Entity의 hashedPassword 필드를 입력 시에는 임시로 raw password 저장용으로 사용
        String rawPassword = newEntry.getHashedPassword(); 
        String hashedPassword = passwordEncoder.encode(rawPassword);
        
        // 2. 해시된 비밀번호를 Entity에 저장합니다.
        newEntry.setHashedPassword(hashedPassword);
        
        // 3. 데이터 저장
        Guestbook savedEntry = guestbookRepository.save(newEntry);
        
        // 4. 보안을 위해 응답 전에 비밀번호 해시 정보 제거
        savedEntry.setHashedPassword(null); 
        
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    // =======================================================
    // 3. 방명록 삭제 (DELETE)
    // =======================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteGuestbook(@PathVariable Long id, @RequestBody Map<String, String> body) {
        
        String rawPasswordFromClient = body.get("password"); 
        Optional<Guestbook> entryOptional = guestbookRepository.findById(id);

        if (entryOptional.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("status", "fail", "message", "Entry not found"), 
                HttpStatus.NOT_FOUND
            );
        }

        Guestbook entry = entryOptional.get();
        String storedHashedPassword = entry.getHashedPassword();
        
        // 비밀번호 검증
        if (passwordEncoder.matches(rawPasswordFromClient, storedHashedPassword)) {
            guestbookRepository.delete(entry);
            return new ResponseEntity<>(Map.of("status", "success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                Map.of("status", "fail", "message", "Invalid password"), 
                HttpStatus.UNAUTHORIZED
            );
        }
    }
}