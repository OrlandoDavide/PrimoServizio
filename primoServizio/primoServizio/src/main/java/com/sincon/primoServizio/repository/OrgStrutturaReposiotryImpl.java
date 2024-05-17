package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class OrgStrutturaReposiotryImpl {

    private final JPAQueryFactory queryFactory;

    public OrgStrutturaReposiotryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
