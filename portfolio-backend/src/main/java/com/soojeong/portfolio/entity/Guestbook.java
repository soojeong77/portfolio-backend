package com.soojeong.portfolio.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guestbook")
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⚠️ DB 컬럼명 author_name에 맞게 매핑
    @Column(name = "author_name", nullable = false, length = 50)
    private String author;

    // ⚠️ DB varchar(1000)에 맞게 길이 수정
    @Column(nullable = false, length = 1000) 
    private String content;

    @Column(nullable = false, length = 100) 
    private String hashedPassword; 

    // PostgreSQL의 timestamptz 타입에 매핑됩니다.
    @Column(updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    // 기본 생성자 (JPA 필수)
    public Guestbook() {}

    // Getter and Setter
    public Long getId() { return id; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public LocalDateTime getCreatedDate() { return createdDate; }
}