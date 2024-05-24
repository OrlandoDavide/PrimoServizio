package com.sincon.primoServizio.repository;

import com.sincon.primoServizio.model.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, String>, QuerydslPredicateExecutor<ComuneRepository> {

    @Query(value = "SELECT * FROM comune WHERE ISTAT =?1", nativeQuery = true)
    public Comune findByCodiceIstat(@Param("ISTAT") String codIstat);
}
