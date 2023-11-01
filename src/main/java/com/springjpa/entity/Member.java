package com.springjpa.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name","age"})
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String name) {
        this.name = name;
    }

    @Builder
    public Member(String name, int age, Team team) {
        this.name = name;
        this.age = age;
        if(team != null) { // 기존에 Team 이 존재하는 경우
            this.changeTeam(team);
        }
    }

    @Builder
    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /* 팀 변경 메소드 */
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this); // 객체 지향성
    }

    /* 이름 변경 */
    public void changeName(String name) {
        this.name = name;
    }
}
