package com.springjpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    // Member 의 name
    // SpEL 문법 사용 ( target = Member )
    @Value("#{target.name + ' ' + target.age + ' ' + target.team.name}")
    String getName();


    // 이렇게 SpEL 문법으로 진행하는 것을 Open Projections 이라고 함
    // 단 이렇게 SpEL 문법을 사용 하면 DB 에서 엔티티 필드를 모두 조회한 다음에 계산
}
