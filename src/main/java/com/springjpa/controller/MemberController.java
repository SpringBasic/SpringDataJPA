package com.springjpa.controller;

import com.springjpa.dto.MemberDto;
import com.springjpa.entity.Member;
import com.springjpa.repository.MemberRepository;
import com.springjpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;


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

    /**
     * - 도메인 클래스 컨버터
     * - only 조회 용도
     **/
    @GetMapping("/member/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getName();
    }

    /**
     * - controller 페이징
     * # @PageableDefault 로 디폴트 설정 가능
     * # ModelAttribute 로 @RequestParam 또한 받을 수 있음
     * # Page 형태로 반환 하면 Good -> 프론트 개발자들이 편하게 사용 가능
    **/
    @GetMapping("/members/page")
    public Page<MemberDto> findMemberUsingPaging(Pageable pageable) {
        // http://localhost:8081/members?page=0&size=2&sort=id,desc
        // 쿼리스트링을 Pageable 클래스로 바인딩

        System.out.println(pageable.getSort()); // id : DESC
        System.out.println(pageable.getPageNumber()); // 0
        System.out.println(pageable.getOffset()); // 4
        System.out.println(pageable.getPageSize());  // 2

        // 페이지를 1부터 설정하는 방법
        // 페이지를 0부터 하는 것이 가장 편함 but, 프론트와 협의 후 결정
        pageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize());
        return memberRepository.findPageBy(pageable)
                .map(MemberDto::new);

        // -> 이렇게 구현할 경우 응답 Page 에서 pageNumber key 값이 문제가 된다 !! (감안)
    }

    @GetMapping("/members/slice")
    public Slice<MemberDto> findMemberUsingSlicing(Pageable pageable) {
        Slice<Member> result = memberRepository.findSliceBy(pageable);
        return result.map(MemberDto::new);
    }
}
