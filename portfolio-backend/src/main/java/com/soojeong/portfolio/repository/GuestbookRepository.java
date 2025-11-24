package com.soojeong.portfolio.repository;

import com.soojeong.portfolio.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository<Entity 타입, ID 타입> 상속
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    // 작성 날짜(createdDate) 기준으로 내림차순(Desc)으로 모든 데이터를 조회
    List<Guestbook> findAllByOrderByCreatedDateDesc();
}