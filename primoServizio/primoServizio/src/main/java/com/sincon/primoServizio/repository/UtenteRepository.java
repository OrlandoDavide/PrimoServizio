package com.sincon.primoServizio.repository;

import com.sincon.primoServizio.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>, QuerydslPredicateExecutor<Utente> {

    boolean existsUtenteByEmail(String email);

    Utente findUtenteByEmail(String email);

    Utente findUtenteById(Long id);
}
