package com.springjpa.dto;

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
}
