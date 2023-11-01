package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * - 1차 캐시
 * - 동일성보장
 * - 변경 감지
 * - 쓰기 지연
 * - 지연 로딩
 **/
@SpringBootTest
@Transactional
@Rollback(false) // rollback x -> commit
public class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getName()).isEqualTo(savedMember.getName());
        assertThat(savedMember.getName()).isEqualTo(findMember.getName());
        // 같은 트랜잭션에서는 동일성 보장
        assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");


        // 1. 저장
        Member savedMember1 = memberJpaRepository.save(member1);
        Member savedMember2 = memberJpaRepository.save(member2);

        // 2. 단건 조회
        Member foundMember1 = memberJpaRepository.findMemberById(savedMember1.getId()).get();
        Member foundMember2 = memberJpaRepository.findMemberById(savedMember2.getId()).get();

        // 3. 리스트 검증
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        // 4. 삭세
        memberJpaRepository.delete(savedMember1);
        memberJpaRepository.delete(savedMember2);

        // 5. 갯수 조회
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(0);

        // 6. 변경 감지(Dirty Checking)
        Member foundMember3 = memberJpaRepository.save(new Member("member3"));
        foundMember3.changeName("new Member3"); // 변경된 new Member3 으로 DB 저장
    }


    // Spring Data JPA 가 제공하는 마법
    // 1. 메소드 이름으로 쿼리 실행
    // 2. 메소드 이름으로 JPA NamedQuery 호출
    // 3. @Query 사용해서 인터페이스에 쿼리 직접 정의

    @Test
    public void findByNameAndAgeGreaterThan() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("member1", 13);
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
}
