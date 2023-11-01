package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * - 순수 JPA 기반 레포지토리
**/
@Repository
public class MemberJpaRepository {

    @Autowired
    EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public Optional<Member> findMemberById(Long id) {
        // nullable return value
        return Optional.ofNullable(em.find(Member.class,id));
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    // name & age 사용해서 회원 리스트 조회
    public List<Member> findByUsernameAndAgeGreaterThan(String name, int age) {
        return em.createQuery("select m from Member m where m.name = :name and m.age > :age", Member.class)
                .setParameter("name",name)
                .setParameter("age",age)
                .getResultList();
    }
}
