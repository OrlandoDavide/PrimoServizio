package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import jakarta.persistence.EntityManager;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class OrgStrutturaRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    private final QOrganigrammaStruttura qOrganigrammaStruttura = QOrganigrammaStruttura.organigrammaStruttura;

    public OrgStrutturaRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public OrganigrammaStruttura findAslByCodiceNSIS(String codNSIS) {
        return Objects.requireNonNull(queryFactory.select(qOrganigrammaStruttura)
                .from(qOrganigrammaStruttura)
                    .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
                    .leftJoin(qOrganigrammaStruttura.comune)
                    .where(
                            qOrganigrammaStruttura.codiceNSIS.eq(codNSIS)
                            .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq((long)24011))
                    )
                .fetchOne()
        );
    }

    public String findDistretto(int prog, int idAsl) {
        return Objects.requireNonNull(queryFactory.select(qOrganigrammaStruttura)
                .from(qOrganigrammaStruttura)
                .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
                .leftJoin(qOrganigrammaStruttura.asl)
                .where(qOrganigrammaStruttura.progDistretto.eq(prog)
                        .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq(24010L)
                        .and(qOrganigrammaStruttura.asl.id.eq((long) idAsl)))
                )
        ).toString();
    }

    public long findOneByCodiceEdotto(int codiceEdotto) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura.id)
                        .from(qOrganigrammaStruttura)
                        .where(qOrganigrammaStruttura.codiceEdotto.eq(codiceEdotto))
                        .fetchOne()
        );
    }

    public OrganigrammaStruttura findOneByCodiceEdottoOfOriginal(int codiceEdotto) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.strutturaOriginale)
                        .where(qOrganigrammaStruttura.strutturaOriginale.codiceEdotto.eq(codiceEdotto))
                        .fetchOne()
        );
    }

    public Long findOneByCodiceEdottoGrampaAndStabilimento(int codiceEdottoParent, int stabilimento) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura.id)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.parent)
                        .where(qOrganigrammaStruttura.parent.id.eq((long) codiceEdottoParent)
                                .and(qOrganigrammaStruttura.progStabilimento.eq(stabilimento)))
                        .fetchOne()
        );
    }

//    public OrganigrammaStruttura findByCodiciSpecialitaClinica() {
//
//    }

    public OrganigrammaStruttura findOneByCodiceEdottoAndTipologiaEdotto(int codiceEdotto, int tipologiaEdotto) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .where(qOrganigrammaStruttura.codiceEdotto.eq(codiceEdotto)
                                .and(qOrganigrammaStruttura.tipologiaEdotto.eq(tipologiaEdotto)))
                        .fetchOne()
        );
    }
}
