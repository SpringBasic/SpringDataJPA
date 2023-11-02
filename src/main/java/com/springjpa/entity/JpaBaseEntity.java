package com.springjpa.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 속성만 공유(only 데이터)
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // prePersist,preUpdate = 스프링 JPA
    // 저장되기 전, 동작하는 메소드
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    // 수정되기 전, 동작하는 메소드
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
