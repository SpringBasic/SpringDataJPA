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


    @Test
    public void pagingTest() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));
        memberJpaRepository.save(new Member("member7", 10));
        memberJpaRepository.save(new Member("member8", 10));
        memberJpaRepository.save(new Member("member9", 10));

        int age = 10;
        int offset = 1;
        int limit = 3;

        // offset + limit 데이터 리스트 가져오기
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long count = memberJpaRepository.totalCount(age);

        for (Member member : members) {
            // member 8 ~ member 6 (3 개)
            System.out.println("member = " + member);
        }

        // 페이지 계산 공식 적용 ...
        // totalPage = totalCount / size(한 페이지당 데이터 수) ...
        // 마지막 페이지 ..
        // 최초 페이지

        // ex ) 1 page = offset (0), limit (10) , 2 page = offset(11), limit(10)
        // 이와 같은 고대로부터 내려오는(?) 공식 적용하는게 너무 번거로움

        assertThat(count).isEqualTo(9);
    }

    @Test
    public void bulkUpdate() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 20));
        memberJpaRepository.save(new Member("member3", 30));
        memberJpaRepository.save(new Member("member4", 40));
        memberJpaRepository.save(new Member("member5", 50));
        memberJpaRepository.save(new Member("member6", 60));
        memberJpaRepository.save(new Member("member7", 70));
        memberJpaRepository.save(new Member("member8", 80));
        memberJpaRepository.save(new Member("member9", 90));


        // 20 살 이상인 사람은 20 + 1
        int result = memberJpaRepository.bulkAgePlus(20);

        assertThat(result).isEqualTo(8);
    }
}