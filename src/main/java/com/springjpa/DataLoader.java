//package com.springjpa;
//
//import com.springjpa.entity.Member;
//import com.springjpa.entity.Team;
//import com.springjpa.repository.MemberRepository;
//import com.springjpa.repository.TeamRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
///**
// * - application 구동 시 데이터 삽입
// **/
//@Component
//public class DataLoader implements CommandLineRunner {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    TeamRepository teamRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Team teamA = new Team("teamA");
//        Team teamB = new Team("teamB");
//
//        teamRepository.saveAll(Arrays.asList(teamA,teamB));
//
//        // 10 개 데이터
//        Member member01 = new Member("member1", 10, teamA);
//        Member member02 = new Member("member2", 10, teamA);
//        Member member03 = new Member("member3", 10, teamB);
//        Member member04 = new Member("member4", 10, teamB);
//        Member member05 = new Member("member5", 10, teamB);
//        Member member06 = new Member("member6", 10, teamA);
//        Member member07 = new Member("member7", 10, teamA);
//        Member member08 = new Member("member8", 10, teamB);
//        Member member09 = new Member("member9", 10, teamB);
//        Member member10 = new Member("member10", 10, teamB);
//
//        memberRepository.saveAll(Arrays.asList(member01, member02, member03, member04
//                , member05, member06, member07, member08, member09, member10));
//    }
//}
