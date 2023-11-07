package com.springjpa.repository;

import com.springjpa.entity.Member;
import com.springjpa.entity.Team;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

public class MemberSpec {

    /* 팀 이름 */
    public static Specification<Member> teamName(final String teamName) {
        return (Specification<Member>) (root, query, builder) -> {

            if(!StringUtils.hasText(teamName)) {
                return null;
            }

            Join<Member, Team> t = root.join("team", JoinType.INNER); // 회원과 조인
            return builder.equal(t.get("name"),teamName);
        };
    }

    /* 회원 이름 */
    public static Specification<Member> username(final String username) {
        return (Specification<Member>) (root, query, builder) ->
            builder.equal(root.get("name"), username);
    }
}


