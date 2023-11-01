package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * - Spring Data JPA 가 애플리케이션 로딩 시점에 프록시 형태로 구현 객체를 자동 으로 생성
 * - @Repository 없어도 동작, Spring Data JPA 가 프록시 형태로 구현 객체를 만들고 Bean 등록
**/
public interface MemberRepository extends JpaRepository<Member,Long> {
}
