package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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


}
