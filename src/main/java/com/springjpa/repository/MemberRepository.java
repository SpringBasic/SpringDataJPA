package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * - Spring Data JPA 가 애플리케이션 로딩 시점에 프록시 형태로 구현 객체를 자동 으로 생성
 * - @Repository 없어도 동작, Spring Data JPA 가 프록시 형태로 구현 객체를 만들고 Bean 등록
**/
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    List<Member> findHelloBy();

    List<Member> readHelloBy();

    List<Member> getHelloBy();

    // 엔티티 결과의 중복을 제거(name 열의 중복 제거 x)
    List<Member> findDistinctByName(String name);

    List<Member> findTop3ByOrderByNameDesc();

    List<Member> findFirstByOrderByName();

    List<Member> findFirst3ByOrderByNameDesc();

    List<Member> queryFirst3ByOrderByNameDesc();

    List<Member> findFirst3ByName(String name);
}
