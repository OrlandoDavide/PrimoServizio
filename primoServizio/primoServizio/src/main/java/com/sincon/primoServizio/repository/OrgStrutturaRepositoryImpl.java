package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
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
                            .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq(24011))
                    )
                .fetchOne()
        );
    }

    public OrganigrammaStruttura findDistretto(int prog, int idAsl) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
                        .leftJoin(qOrganigrammaStruttura.asl)
                        .where(
                                qOrganigrammaStruttura.progDistretto.eq(prog)
                                .and(qOrganigrammaStruttura.tipologiaEdotto.id.eq(24010)
                                .and(qOrganigrammaStruttura.asl.id.eq(idAsl)))
                        )
                        .fetchOne()
        );
    }

    public OrganigrammaStruttura findOneByCodiceEdotto(int codiceEdotto) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura)
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

    public OrganigrammaStruttura findOneByCodiceEdottoGrampaAndStabilimento(int codiceEdottoParent, int stabilimento) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.parent)
                        .where(
                                qOrganigrammaStruttura.parent.id.eq(codiceEdottoParent)
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
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura)
                        .from(qOrganigrammaStruttura)
                        .where(
                                qOrganigrammaStruttura.codiceEdotto.eq(codiceEdotto)
                                .and(qOrganigrammaStruttura.tipologiaEdotto.eq(tipologiaEdotto))
                        )
                        .fetchOne()
        );
    }
}
