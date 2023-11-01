package com.springjpa.service;

import com.springjpa.dto.MemberDto;
import com.springjpa.entity.Member;
import com.springjpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Page<MemberDto> getMemberDtoUsingPagingByPage(int age) {
        Page<Member> result = memberRepository.findByAge(age, PageRequest.of(0, 3));
        return result.map(member -> new MemberDto(member.getId(),member.getName(),member.getTeam().getName()));
    }

    @Transactional
    public Slice<MemberDto> getMemberDtoUSingPagingBySlice(int age) {
        Slice<Member> result = memberRepository.findMemberByAge(age, PageRequest.of(0,3));
        return result.map(member -> new MemberDto(member.getId(),member.getName(),member.getTeam().getName()));
    }
}
