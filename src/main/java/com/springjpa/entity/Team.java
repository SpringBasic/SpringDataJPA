package com.springjpa.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@ToString(of = {"id","name"})
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    @Builder
    public Team(String name, List<Member> members) {
        this.name = name;
        this.members = members;
    }

    public Team(String name) {
        this.name = name;
    }
}
