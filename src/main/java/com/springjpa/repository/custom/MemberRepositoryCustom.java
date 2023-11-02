package com.springjpa.repository.custom;

import com.springjpa.entity.Member;

import java.util.List;

/**
 * - 스프링 데이터 JPA 사용자 정의 인터페이스
 * - 내가 정의하고 싶은 쿼리를 설계 하고 Spring Data JPA repository 에서 상속
**/
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
