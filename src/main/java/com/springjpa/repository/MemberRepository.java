package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // 메소드 이름으로 쿼리 생성은 길어 지면 너무 복잡 해지는 문제가 있음
    // 오타가 발생한 경우, 애플리케이션 실행 시 에러 발생
    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findByJpql(@Param("name") String name, @Param("age") int age);
}
