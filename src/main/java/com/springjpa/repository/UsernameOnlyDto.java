package com.springjpa.repository;

// 클래스 기반 Projection
// UsernameOnly 처럼 인터페이스 기반이 아니라 구체적인 DTO 형식도 가능
public class UsernameOnlyDto {
    private final String name;

    public UsernameOnlyDto(String name) {
        this.name = name;
    }

    public String getUsername() {
        return this.name;
    }
}
