package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrgStrutturaRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    private final QOrganigrammaStruttura qOrganigrammaStruttura = QOrganigrammaStruttura.organigrammaStruttura;

    public OrgStrutturaRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public OrganigrammaStruttura findAslByCodiceNSIS(String codNSIS) {
        return (queryFactory.select(qOrganigrammaStruttura)
                .from(qOrganigrammaStruttura)
                    .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
                    .leftJoin(qOrganigrammaStruttura.comune)
                    .where(
                            qOrganigrammaStruttura.codiceNSIS.eq(codNSIS)
                            .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq(24011L))
                    )
                .fetchOne()
        );
    }

    public OrganigrammaStruttura findDistretto(int prog, Long idAsl) {
        return (
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
                        .leftJoin(qOrganigrammaStruttura.asl)
                        .where(
                                qOrganigrammaStruttura.progDistretto.eq(prog)
                                .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq(24010L)
                                .and(qOrganigrammaStruttura.asl.id.eq(idAsl)))
                        )
                        .fetchOne()
        );
    }

    public OrganigrammaStruttura findOneByCodiceEdotto(int codiceEdotto) {
        return (
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .where(qOrganigrammaStruttura.codiceEdotto.eq(codiceEdotto))
                        .fetchOne()
        );
    }

    public OrganigrammaStruttura findOneByCodiceEdottoOfOriginal(int codiceEdotto) {
        return (
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.strutturaOriginale)
                        .where(qOrganigrammaStruttura.strutturaOriginale.codiceEdotto.eq(codiceEdotto))
                        .fetchOne()
        );
    }

    public OrganigrammaStruttura findOneByCodiceEdottoGrampaAndStabilimento(int codiceEdottoParent, int stabilimento) {
        return (
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.parent)
                        .where(
                                qOrganigrammaStruttura.parent.id.eq((long) codiceEdottoParent)
                                .and(qOrganigrammaStruttura.progStabilimento.eq(stabilimento))
                        )
                        .fetchOne()
        );
    }

    public List<OrganigrammaStruttura> findByCodiciSpecialitaClinica(String[] codici) {

        return queryFactory.selectFrom(qOrganigrammaStruttura)
                .where(qOrganigrammaStruttura.codiceSpecialitaClinica.in(codici))
                .fetch();
    }

    public OrganigrammaStruttura findOneByCodiceEdottoAndTipologiaEdotto(int codiceEdotto, int tipologiaEdotto) {
        return (
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .where(
                                qOrganigrammaStruttura.codiceEdotto.eq(codiceEdotto)
                                .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq((long) tipologiaEdotto))
                        )
                        .fetchOne()
        );
    }
}
