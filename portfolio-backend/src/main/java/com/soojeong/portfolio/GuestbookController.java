package com.soojeong.portfolio;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/guestbook")
public class GuestbookController {

    // 메모리 기반 방명록 저장소
    private final List<Map<String, Object>> guestbooks = new ArrayList<>();
    private int currentId = 1;

    // ----------------------------------------------------
    // 1. 방명록 목록 조회 (GET)
    // ----------------------------------------------------
    @GetMapping
    public List<Map<String, Object>> getGuestbooks() {
        // 최신순으로 반환
        guestbooks.sort((a, b) -> {
            Date dateA = new Date(a.get("regDate").toString());
            Date dateB = new Date(b.get("regDate").toString());
            return dateB.compareTo(dateA);
        });
        return guestbooks;
    }

    // ----------------------------------------------------
    // 2. 방명록 작성 (POST)
    // ----------------------------------------------------
    @PostMapping
    public Map<String, Object> addGuestbook(@RequestBody Map<String, String> body) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("gId", currentId++);
        entry.put("author", body.get("author"));
        entry.put("content", body.get("content"));
        entry.put("password", body.get("password"));
        entry.put("regDate", new Date().toString()); // ISO 문자열로 바꿔도 OK

        guestbooks.add(entry);
        return entry;
    }

    // ----------------------------------------------------
    // 3. 방명록 삭제 (DELETE)
    // ----------------------------------------------------
    @DeleteMapping("/{id}")
    public Map<String, String> deleteGuestbook(@PathVariable int id, @RequestBody Map<String, String> body) {
        String password = body.get("password");

        boolean removed = guestbooks.removeIf(entry -> 
            (int) entry.get("gId") == id && entry.get("password").equals(password)
        );

        if (removed) {
            return Map.of("status", "success");
        } else {
            return Map.of("status", "fail");
        }
    }
}
