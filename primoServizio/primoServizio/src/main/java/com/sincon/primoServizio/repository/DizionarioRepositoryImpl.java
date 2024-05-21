package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.model.QDizionario;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DizionarioRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    private final QDizionario qDizionario = QDizionario.dizionario;

    public DizionarioRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Dizionario getDizionarioByCodifica(String codifica, String categoria) {
        return queryFactory.selectFrom(qDizionario)
                .where(
                        qDizionario.categoria.eq(categoria.toUpperCase())
                        .and(qDizionario.codifica.eq(codifica.toUpperCase()))
                )
                .fetchOne();
    }
}
