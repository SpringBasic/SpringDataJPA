package com.springjpa.controller;

import com.springjpa.dto.MemberDto;
import com.springjpa.entity.Member;
import com.springjpa.repository.MemberRepository;
import com.springjpa.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;


    // Page, Slice 형식 으로 반환 하는 것 Good!
    @GetMapping("/member/page")
    public Page<MemberDto> findMemberByAgeUsingPagingPage(
            @RequestParam int age
    ) {
        return memberService.getMemberDtoUsingPagingByPage(age);
    }

    @GetMapping("/member/slice")
    public Slice<MemberDto> findMemberByAgeUsingPagingSlice(
            @RequestParam int age
    ) {
        return memberService.getMemberDtoUSingPagingBySlice(age);
    }
}
