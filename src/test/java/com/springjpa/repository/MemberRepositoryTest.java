package com.springjpa.repository;

import com.springjpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

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
}
