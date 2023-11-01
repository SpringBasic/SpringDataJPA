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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

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
        Member m1 = new Member("member1",10);
        Member m2 = new Member("member2",20);
        Member m3 = new Member("member2",30);

        memberRepository.saveAll(Arrays.asList(m1,m2,m3));

        List<Member> result = memberRepository.findByNameAndAgeGreaterThan("member2",19);

        for(Member member : result) {
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

        for(Member m : result01) {
            System.out.println(m);
        }
    }

    @Test
    public void springDataJpaTest() {

        Member m1 = new Member("member1",10);
        Member m2 = new Member("member2",20);
        Member m3 = new Member("member2",20);

        memberRepository.saveAll(Arrays.asList(m1,m2,m3));

        List<Member> result = memberRepository.findDistinctByName("member2");

        for(Member m : result) {
            System.out.println("m = " + m);
        }
    }

    @Test
    public void findTopAndFirst() {
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",20));
        memberRepository.save(new Member("member3",30));
        memberRepository.save(new Member("member4",40));

        List<Member> result = memberRepository.findTop3ByOrderByNameDesc();
        for(Member member : result) {
            System.out.println("member = " + member);
        }

        List<Member> result02 = memberRepository.findFirstByOrderByName();
        for(Member member : result) {
            System.out.println("member = " + member);
        }

        List<Member> result03 = memberRepository.findFirst3ByOrderByNameDesc();
        for(Member member : result03) {
            System.out.println("member = " + member);
        }

        List<Member> result04 = memberRepository.queryFirst3ByOrderByNameDesc();
        for(Member member : result04) {
            System.out.println("member = " + member);
        }
    }
    
    @Test
    public void findTopAndFirst02() {
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",20));
        memberRepository.save(new Member("member2",30));
        memberRepository.save(new Member("member2",40));

        List<Member> result = memberRepository.findFirst3ByName("member2");
        
        for(Member member : result) {
            System.out.println("member = " + member);
        }
    }

    /**
     * - 레포지토리 메소드에 @Query 쿼리 정의하기
    **/
    @Test
    public void findMemberByQueryAnnotation() {
        Member m1 = new Member("member1",10);
        Member m2 = new Member("member2",20);
        Member m3 = new Member("member3",30);
        Member m4 = new Member("member2",20);
        
        memberRepository.saveAll(Arrays.asList(m1,m2,m3,m4));

        List<Member> result = memberRepository.findByJpql("member2", 20);
        
        for(Member m : result) {
            System.out.println("m = " + m);
        }
    }

    /**
     * - @Query 결과를 DTO 로 정의하기
    **/

    @Test
    public void findMemberByQueryAnnotationToString() {
        Member m1 = new Member("member1",10);
        Member m2 = new Member("member2",20);
        Member m3 = new Member("member3",30);
        Member m4 = new Member("member2",20);

        memberRepository.saveAll(Arrays.asList(m1,m2,m3,m4));

        List<String> memberNames = memberRepository.findMembernames();

        for(String name : memberNames) {
            System.out.println("name = " + name);
        }
    }

    /**
     * - @Query 결과를 MemberDto 로 변환하기
    **/
    @Test
    public void findMemberByQueryAnnotationToMemberDto() {

        Team team1 = teamRepository.save(new Team("team1"));

        memberRepository.save(new Member("member1",10,team1));
        memberRepository.save(new Member("member2",20,team1));

        List<MemberDto> result = memberRepository.findMemberToMemberDto();

        for(MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    /**
     * - 컬렉션 파라미터 바인딩
    **/
    @Test
    public void findMemberByCollectionParameterBinding() {
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",20));

        List<Member> members = memberRepository.findMemberFromNames(Arrays.asList("member1", "member2"));
        for(Member m : members) {
            System.out.println("m = " + m);
        }
    }

    /**
     * - 유연한 반환 타입 조회
    **/
    @Test
    public void findMemberUsingFlexibleReturnType() {
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",20));

        List<Member> members = memberRepository.findListByName("member2");


        // 단건 조회일 때, 없으면(getSingleResult) 순수 JPA 는 NoResultException 터트리지만,
        // Spring Data JPA 는 자동 으로 감싸서 예외 처리 -> null
        Member oneMember = memberRepository.findOneByName("member1");

        Member optionalMember = memberRepository.findOptionalByName("member1").get();
        for(Member m : members) {
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
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));
        memberRepository.save(new Member("member6",10));
        memberRepository.save(new Member("member7",10));
        memberRepository.save(new Member("member8",10));
        memberRepository.save(new Member("member9",10));

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


        for(Member m : content) {
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
        
        for(Member member : sliceResult.getContent()) {
            System.out.println("member = " + member);
        }

        assertThat(sliceResult.getNumber()).isEqualTo(0);
        assertThat(sliceResult.hasNext()).isTrue();
        assertThat(sliceResult.isFirst()).isTrue();
    }

    @Test
    public void SpringDataJpaPagingTestUsingDividedCountQuery() {

        Team teamA = teamRepository.save(new Team("teamA"));
        memberRepository.save(new Member("member1",10,teamA));
        memberRepository.save(new Member("member2",10,teamA));
        memberRepository.save(new Member("member3",10,teamA));
        memberRepository.save(new Member("member4",10,teamA));
        memberRepository.save(new Member("member5",10,teamA));
        memberRepository.save(new Member("member6",10,teamA));
        memberRepository.save(new Member("member7",10,teamA));
        memberRepository.save(new Member("member8",10,teamA));
        memberRepository.save(new Member("member9",10,teamA));


        // content 관련 쿼리는 join 쿼리, count 쿼리는 순수 member 관련 쿼리
        Page<Member> result = memberRepository.findByAgeDivideCountQuery(10, PageRequest.of(0, 3));
    }
}

