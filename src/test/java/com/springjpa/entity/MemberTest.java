package com.springjpa.entity;

import com.springjpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

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

    @Test
    public void auditingTest() throws InterruptedException {

        Member member = new Member("member1");
        memberRepository.save(member); // insert -> 영속성 컨텍스트

        Thread.sleep(100);
        member.changeName("new_member1"); // update -> 영속성 컨텍스( 모든 필드 업데이트 )


        em.flush();
        em.clear();

        Member foundMember = memberRepository.findOneByName("new_member1");
        System.out.println("member.getCreatedAt() = " + foundMember.getCreatedAt());
        System.out.println("member.getUpdatedAt() = " + foundMember.getUpdatedAt());
    }
}