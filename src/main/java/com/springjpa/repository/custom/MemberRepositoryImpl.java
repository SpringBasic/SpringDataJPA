package com.springjpa.repository.custom;

import com.springjpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * - 스프링 데이터 JPA 사용자 정의 인터페이스 구현 클래스
 * # 김영한님 : 'QueryDsl 쿼리를 사용자 정의 인터페이스 구현 클래스 안에 정의 해서 메소드 호출 해서 사용'
 * # MemberRepository 만으로 해결 안될 경우 사용하자........!!!(복잡한 쿼리, 복잡한 동적 쿼리)
 * # but, 항상 커스텀을 사용할 필요 x -> 그냥 @Repository 와 Class 을 이용해서 복잡한 쿼리 용도의 클래스를 별도로 구현하면 된다.
**/
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
