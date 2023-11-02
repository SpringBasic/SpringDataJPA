package com.springjpa.dto;

import com.springjpa.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id","name","teamName"})
public class MemberDto {
    private Long id;
    private String name;
    private String teamName;

    public MemberDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.teamName = member.getTeam().getName();
    }
}
