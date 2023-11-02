package com.springjpa.repository;

import com.springjpa.dto.MemberDto;
import com.springjpa.entity.Member;
import com.springjpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    public void basicCRUDTest() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        Member foundMember1 = memberRepository.findById(savedMember1.getId()).get();
        Member foundMember2 = memberRepository.findById(savedMember2.getId()).get();


        // 동일성 보장
        assertThat(savedMember1).isEqualTo(foundMember1);
        assertThat(savedMember2).isEqualTo(foundMember2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void findByNameAndAgeGreaterThan() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);
        Member m3 = new Member("member2", 30);

        memberRepository.saveAll(Arrays.asList(m1, m2, m3));

        List<Member> result = memberRepository.findByNameAndAgeGreaterThan("member2", 19);

        for (Member member : result) {
            System.out.println("member = " + member);
            assertThat(member.getAge()).isGreaterThanOrEqualTo(20);
        }

        // 메소드 이름으로 쿼리 실행 : 만병통치 x -> 너무 길어지면 안좋다(김영한님 왈 "개인적으로 2개 지향")
    }

    @Test
    public void findByHello() {
        // [조회] : find...By, read...By, get...By(by 뒤에 where 조건)
        List<Member> result01 = memberRepository.findHelloBy();
        List<Member> result02 = memberRepository.getHelloBy();
        List<Member> result03 = memberRepository.readHelloBy();

        for (Member m : result01) {
            System.out.println(m);
        }
    }

    @Test
    public void springDataJpaTest() {

        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);
        Member m3 = new Member("member2", 20);

        memberRepository.saveAll(Arrays.asList(m1, m2, m3));

        List<Member> result = memberRepository.findDistinctByName("member2");

        for (Member m : result) {
            System.out.println("m = " + m);
        }
    }

    @Test
    public void findTopAndFirst() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));

        List<Member> result = memberRepository.findTop3ByOrderByNameDesc();
        for (Member member : result) {
            System.out.println("member = " + member);
        }

        List<Member> result02 = memberRepository.findFirstByOrderByName();
        for (Member member : result) {
            System.out.println("member = " + member);
        }

        List<Member> result03 = memberRepository.findFirst3ByOrderByNameDesc();
        for (Member member : result03) {
            System.out.println("member = " + member);
        }

        List<Member> result04 = memberRepository.queryFirst3ByOrderByNameDesc();
        for (Member member : result04) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void findTopAndFirst02() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member2", 30));
        memberRepository.save(new Member("member2", 40));

        List<Member> result = memberRepository.findFirst3ByName("member2");

        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    /**
     * - 레포지토리 메소드에 @Query 쿼리 정의하기
     **/
    @Test
    public void findMemberByQueryAnnotation() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);
        Member m3 = new Member("member3", 30);
        Member m4 = new Member("member2", 20);

        memberRepository.saveAll(Arrays.asList(m1, m2, m3, m4));

        List<Member> result = memberRepository.findByJpql("member2", 20);

        for (Member m : result) {
            System.out.println("m = " + m);
        }
    }

    /**
     * - @Query 결과를 DTO 로 정의하기
     **/

    @Test
    public void findMemberByQueryAnnotationToString() {
        Member m1 = new Member("member1", 10);
        Member m2 = new Member("member2", 20);
        Member m3 = new Member("member3", 30);
        Member m4 = new Member("member2", 20);

        memberRepository.saveAll(Arrays.asList(m1, m2, m3, m4));

        List<String> memberNames = memberRepository.findMembernames();

        for (String name : memberNames) {
            System.out.println("name = " + name);
        }
    }

    /**
     * - @Query 결과를 MemberDto 로 변환하기
     **/
    @Test
    public void findMemberByQueryAnnotationToMemberDto() {

        Team team1 = teamRepository.save(new Team("team1"));

        memberRepository.save(new Member("member1", 10, team1));
        memberRepository.save(new Member("member2", 20, team1));

        List<MemberDto> result = memberRepository.findMemberToMemberDto();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * - 컬렉션 파라미터 바인딩
     **/
    @Test
    public void findMemberByCollectionParameterBinding() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));

        List<Member> members = memberRepository.findMemberFromNames(Arrays.asList("member1", "member2"));
        for (Member m : members) {
            System.out.println("m = " + m);
        }
    }

    /**
     * - 유연한 반환 타입 조회
     **/
    @Test
    public void findMemberUsingFlexibleReturnType() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));

        List<Member> members = memberRepository.findListByName("member2");


        // 단건 조회일 때, 없으면(getSingleResult) 순수 JPA 는 NoResultException 터트리지만,
        // Spring Data JPA 는 자동 으로 감싸서 예외 처리 -> null
        Member oneMember = memberRepository.findOneByName("member1");

        Member optionalMember = memberRepository.findOptionalByName("member1").get();
        for (Member m : members) {
            System.out.println("m = " + m);
        }

        System.out.println(oneMember);
        System.out.println(optionalMember);
    }

    /**
     * - 페이징 & 정렬
     * 데이터베이스에 있는 모든 데이터를 어플리케이션으로 가져오는건 x
     * -> Spring Data JPA 는 페이징 정렬 관련된 혁신적인 방법 제공
     **/

    @Test
    public void SpringDataJpaPagingTest() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));

        // << 파라 미터 >>
        // Pageable, Sort

        // shift + F6 : 전체 수정
        PageRequest pageRequest
                = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        int age = 10;

        // 순수 JPA 페이징에서는 페이지 계산을 위해 count 쿼리 필요 with offset, limit !!!!!

        // << 반환 타임 >>
        // page : count 쿼리 결과를 포함 하는 페이징(total count 쿼리 함께 날림)
        // slice : count 쿼리 없이 [다음 페이지]만 확인 가능
        // list : count 쿼리 없이 결과 확인
        Page<Member> result = memberRepository.findByAge(age, pageRequest);
        List<Member> content = result.getContent();


        for (Member m : content) {
            System.out.println("m = " + m);
        }

        // Page 타입 에서 제공하는 메소드
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(0); // 현제 페이지
        assertThat(result.getTotalPages()).isEqualTo(3); // 총 페이지
        assertThat(result.isFirst()).isTrue(); // 첫 페이지 여부
        assertThat(result.hasNext()).isTrue(); // 다음 페이지가 있는지 여부

        // pageRequest 의 size + 1 만큼 보낸다.
        Slice<Member> sliceResult = memberRepository.findMemberByAge(age, pageRequest);

        for (Member member : sliceResult.getContent()) {
            System.out.println("member = " + member);
        }

        assertThat(sliceResult.getNumber()).isEqualTo(0);
        assertThat(sliceResult.hasNext()).isTrue();
        assertThat(sliceResult.isFirst()).isTrue();
    }

    @Test
    public void SpringDataJpaPagingTestUsingDividedCountQuery() {

        Team teamA = teamRepository.save(new Team("teamA"));
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamA));
        memberRepository.save(new Member("member3", 10, teamA));
        memberRepository.save(new Member("member4", 10, teamA));
        memberRepository.save(new Member("member5", 10, teamA));
        memberRepository.save(new Member("member6", 10, teamA));
        memberRepository.save(new Member("member7", 10, teamA));
        memberRepository.save(new Member("member8", 10, teamA));
        memberRepository.save(new Member("member9", 10, teamA));


        // content 관련 쿼리는 join 쿼리, count 쿼리는 순수 member 관련 쿼리
        Page<Member> result = memberRepository.findByAgeDivideCountQuery(10, PageRequest.of(0, 3));
    }

    @Test
    public void bulkUpdate() {

        /**
         * 벌크성 수정 쿼리의 문제점 : 영속성 컨텍스트를 무시 하고 바로 DB 수정
         * -> 영속성 컨텍스트와 맞지 않는 결과 발생 가능
         **/
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 30));
        memberRepository.save(new Member("member4", 40));
        memberRepository.save(new Member("member5", 50));
        memberRepository.save(new Member("member6", 60));
        memberRepository.save(new Member("member7", 70));
        memberRepository.save(new Member("member8", 80));
        memberRepository.save(new Member("member9", 90));

        // age 가 20 살 이상인 Member age + 1
        // bulkAgePlus 는 jpql 이므로 이전 영속성 컨텍스트를 flush 해서 변경 내용 저장
        // flush 시는 clear 가 아니다
        int changedCount = memberRepository.bulkAgePlus(20);
        System.out.println("changedCount = " + changedCount);

        em.flush(); // DB 반영
        em.clear();

        // 그렇다면 "member9" 의 이름을 가진 사람은 90 살일까 91살일까?
        // -> 90 살 !! 이유 : DB 는 91 살이나 영속성 컨텍스트는 90 살 (아직 트랜잭션 안 끝나서 DB 번영 x)
        Member member9 = memberRepository.findOneByName("member9");
        System.out.println(member9.getAge());
        assertThat(changedCount).isEqualTo(8);
    }


    @Test
    public void findMemberLazy() {

        // member1 -> TeamA
        // member2 -> TeamB
        // member 조회 시 실제 team 엔티티 객체 조회 x -> 프록시 객체로 조회
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.saveAll(Arrays.asList(teamA,teamB));

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamB);
        memberRepository.saveAll(Arrays.asList(member1,member2));

        em.flush();
        em.clear();

        List<Member> result = memberRepository.findAll();

        for(Member member : result) {
            System.out.println("member = " + member);
            // getClass 호출 시, 프록시 객체 가져옴
            // N + 1 문제 발생
            // : 연관관계의 지연로딩으로 인해 발생하는 문제로, 예를 들어 하나의 쿼리로 N 개의 결과를 가져올 때
            // 추후 N 개의 엔티티의 연관관계를 맺고 있는 엔티티를 가져오기 위해 N 번의 추가적인 쿼리가 더 나가는 문제
            // 데이터베이스 서버에 접근하기 위해 네트워크를 거쳐야 하기 때문에 성능상 좋지 않음.
            // fetch join 으로 일부 해결 가능
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
        }
    }


    /**
     * - fetch join 간단 실습
     * : fetch join : 연관된 데이터를 join 을 통해 한번에 가져 오는 join 방식
    **/
    @Test
    public void findMemberUsingFetchJoin() {
        // member1 -> TeamA
        // member2 -> TeamB
        // member 조회 시 실제 team 엔티티 객체 조회 x -> 프록시 객체로 조회
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.saveAll(Arrays.asList(teamA, teamB));

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.saveAll(Arrays.asList(member1, member2));

        em.flush();
        em.clear();

        List<Member> result = memberRepository.findMemberByFetchJoin();

        for (Member m : result) {
            System.out.println("m = " + m);
            // 프록시 객체 x -> 순수 Team 객체 가져옴!!!!!
            System.out.println("m.getTeam().getClass() = " + m.getTeam().getClass());
        }
    }

    @Test
    public void findAllMemberUsingEntityGraph() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.saveAll(Arrays.asList(teamA,teamB));

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamB);

        memberRepository.saveAll(Arrays.asList(member1,member2));


        List<Member> members = memberRepository.findAll();

        for(Member member : members) {
            System.out.println("member = " + member);
        }
    }


    @Test
    public void findMemberUsingEntityGraph() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.saveAll(Arrays.asList(teamA,teamB));

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamB);

        memberRepository.saveAll(Arrays.asList(member1,member2));

        List<Member> members = memberRepository.findEntityGraphByName("member1");


    }
}

