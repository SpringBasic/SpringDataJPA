package com.springjpa.repository;

import com.springjpa.dto.MemberDto;
import com.springjpa.entity.Member;
import com.springjpa.repository.custom.MemberRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * - Spring Data JPA 가 애플리케이션 로딩 시점에 프록시 형태로 구현 객체를 자동 으로 생성
 * - @Repository 없어도 동작, Spring Data JPA 가 프록시 형태로 구현 객체를 만들고 Bean 등록
**/
public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

    List<Member> findHelloBy();

    List<Member> readHelloBy();

    List<Member> getHelloBy();

    // 엔티티 결과의 중복을 제거(name 열의 중복 제거 x)
    List<Member> findDistinctByName(String name);

    List<Member> findTop3ByOrderByNameDesc();

    List<Member> findFirstByOrderByName();

    List<Member> findFirst3ByOrderByNameDesc();

    List<Member> queryFirst3ByOrderByNameDesc();

    List<Member> findFirst3ByName(String name);

    // @Query 을 통해 레포지토리에 직접 쿼리 설정하기
    // 메소드 이름으로 쿼리 생성은 길어 지면 너무 복잡 해지는 문제가 있음
    // 오타가 발생한 경우, 애플리케이션 실행 시 에러 발생
    @Query("select m from Member m where m.name = :name and m.age = :age")
    List<Member> findByJpql(@Param("name") String name, @Param("age") int age);


    @Query("select m.name from Member m")
    List<String> findMembernames();


    // MemberDto 로 바로 조회 하기(조회 후 별도로 MemberDto로 변환할 필요 x)
    @Query("select new com.springjpa.dto.MemberDto(m.id,m.name,t.name) from Member m inner join m.team t")
    List<MemberDto> findMemberToMemberDto();

    // 파라미터 바인딩 쿼리 ( 위치 기반 vs 이름 기반 )
    // 가급적 이름 기반 사용 ( 위치 기반은 위치가 변할 수 있음 )
    @Query("select m from Member m where m.name = :name")
    List<Member> findMemberFromName(@Param("name") String name);


    // 컬렉션 파라미터 바인딩 ( =(동등 조건) 대신 in(멤버쉽 조건) 사용 )
    // 멤버쉽 조건 -> 유한한 집합을 하나의 단일 조건 으로 표현식 구성 가능
    @Query("select m from Member m where m.name in :names")
    List<Member> findMemberFromNames(@Param("names") Collection<String> names);


    // Spring Data JPA 는 유연한 반환 타입 지원
    // 리스트 조회
    List<Member> findListByName(String name);

    // 단건 조회
    Member findOneByName(String name);

    // optional<단건> 조회
    Optional<Member> findOptionalByName(String name);


    // Spring Data JPA 페이징 + 정렬
    // page 는 ZerobasedIndex ( 0 부터 시작 )
    // 1. Page
    Page<Member> findByAge(int age, Pageable pageable);

    // 2. Slice
    Slice<Member> findMemberByAge(int age, Pageable pageable);


    // 3. 반환 타입이 Page 인 경우 Count 쿼리를 분리 가능
    // 예를 들어, Join 쿼리인 경우 Count 쿼리도 조인 동작 -> 성능 저하
    @Query(value = "select m from Member m inner join m.team t",
    countQuery = "select count(m) from Member m")
    Page<Member> findByAgeDivideCountQuery(int age, Pageable pageable);


    // 벌크성 수정 쿼리
    // @Modifying -> executeUpdate
    // clearAutomatically -> 벌크 쿼리 이후 자동 클리어
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // Member 와 관련된 Team 을 전부 한번에 가져옴
    // left join -> 두 테이블 중 해당 열의 대한 데이터를 한 테이블만 가지고 있어도 결과 셋에 포함
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMemberByFetchJoin();

    // member 전체 조회 시, 자동 으로 fetch join
    // fetch join 관련 jpql 짜기 귀찮을 때 사용
    // fetch join 은 기본적 으로 left outer join ( team(team_id) 없는 member 도 조회 )
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();


    // 메소드 이름으로 쿼리 생성 방식에서 @EntityGraph 적용 가능
    @EntityGraph(attributePaths = ("team"))
    List<Member> findEntityGraphByName(@Param("name") String name);

    // 김영한님 왈 :
    // 일반적 으로 간단한 경우 : 직접 jpql 을 이용해서 페치 조인 구현
    // 귀찮은 경우, 복잡한 경우 : @EntityGraph 사용
    // Spring Data JPA 보다 Spring JPA 가 더 중요!!!!


    // Query Hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value = "true"))
    Member findReadOnlyByName(String name);


    // LOCK
    // 읽기 락 vs 쓰기 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockMemberByName(String name);
}
