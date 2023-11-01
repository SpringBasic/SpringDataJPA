package com.springjpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
public class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);


        Member member1 = new Member("member2",10,teamA);
        Member member2 = new Member("member3",20,teamA);
        Member member3 = new Member("member4",30,teamB);
        Member member4 = new Member("member5",40,teamB);
        Member member5 = new Member("member6",50,teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);
        // 1차 캐시 저장

        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for(Member m : result) {
            System.out.println("member = " + m);
            System.out.println("m.getTeam().getName() = " + m.getTeam().getName());
        }
    }
}