package com.sincon.primoServizio.repository;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>, QuerydslPredicateExecutor<Utente> {
    Utente findUtenteByEmail(String email);
    Utente findUtenteById(Long id);
    @Query(value = "SELECT * FROM utente", nativeQuery = true)
    List<UtenteDto> findAllDto();
}
