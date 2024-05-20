package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class OrgStrutturaRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    private final QOrganigrammaStruttura qOrganigrammaStruttura = QOrganigrammaStruttura.organigrammaStruttura;

    public OrgStrutturaRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public String findAslByCodiceNSIS(String codNSIS) {
        return Objects.requireNonNull(queryFactory.select(qOrganigrammaStruttura.comune)
                .from(qOrganigrammaStruttura)
                    .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
                    .leftJoin(qOrganigrammaStruttura.comune)
                    .where(
                            qOrganigrammaStruttura.codiceNSIS.eq(codNSIS)
                            //.and(qOrganigrammaStruttura.tipologiaEdotto.eq())
                    )
                .fetchOne()
        ).toString();
    }

//    public String findDistretto(int prog, int idAsl) {
//        return Objects.requireNonNull(queryFactory.select(qOrganigrammaStruttura)
//                .from(qOrganigrammaStruttura)
//                .leftJoin(qOrganigrammaStruttura.tipologiaEdotto)
//                .leftJoin(qOrganigrammaStruttura.asl)
//                .where(qOrganigrammaStruttura.progDistretto.eq(prog)
//                        .and(qOrganigrammaStruttura.tipologiaEdotto.eq(24010)
//                        .and(qOrganigrammaStruttura.asl.eq(idAsl)))
//                )
//        );
//    }

    public Long findOneByCodiceEdotto(int codiceEdotto) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura.id)
                        .from(qOrganigrammaStruttura)
                        .where(qOrganigrammaStruttura.codiceEdotto.eq(codiceEdotto))
                        .fetchOne()
        );
    }

    public Long findOneByCodiceEdottoGrampaAndStabilimento(int codiceEdottoParent, int stabilimento) {
        return Objects.requireNonNull(
                queryFactory.select(qOrganigrammaStruttura.id)
                        .from(qOrganigrammaStruttura)
                        .leftJoin(qOrganigrammaStruttura.parent)
                        .where(qOrganigrammaStruttura.parent.eq(codiceEdottoParent)
                                .and(qOrganigrammaStruttura.progStabilimento.eq(stabilimento)))
                        .fetchOne()
        );
    }


}
