package com.springjpa.repository;

/**
 * 중첩 구조 projection
**/
public interface NestedClosedProjections {
    String getName();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
