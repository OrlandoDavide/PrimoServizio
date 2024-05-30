package com.sincon.primoServizio.repository;

import com.sincon.primoServizio.model.QUtente;
import com.sincon.primoServizio.model.Utente;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>, QuerydslPredicateExecutor<QUtente> {
    Utente findUtenteByEmail(String email);

    Utente findUtenteById(Long id);

    //@Query(value = "SELECT * FROM utente", nativeQuery = true)
    @NonNull
    List<Utente> findAll();

    @Query(value = "SELECT u.attivo FROM utente u WHERE u.id = :id", nativeQuery = true)
    Boolean findStatoUtenteById(@Param("id") Long id);
}
