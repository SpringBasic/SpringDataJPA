package com.springjpa.repository.custom;

import com.springjpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 항상 사용자 정의 레포지토리를 생성해서 Spring Data Jpa Repository 인터페이스에서 상속해서 사용할 필요 없다.
 * 무조건 사용자 정의 레포지토리를 생성해서 상속하면 하나의 MemberRepository 가 무거워진다.
 * 따라서, 그냥 MemberQueryRepository 와 같이 인터페이스가 아닌 클래스로 만들고 스프링 빈으로 등록해서 사용해도 됨
**/
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private EntityManager entityManager;

    public List<Member> findMemberByName(String name) {
        return entityManager.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
