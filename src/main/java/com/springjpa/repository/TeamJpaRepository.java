package com.springjpa.repository;

import com.springjpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {
    @PersistenceContext
    EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t",Team.class)
                .getResultList();
    }

    public Optional<Team> findTeamById(Long id) {
        // nullable return value
        return Optional.ofNullable(em.find(Team.class,id));
    }

    public long count() {
        return em.createQuery("select count(t) from Team t",Long.class)
                .getSingleResult();
    }
}
