package com.sincon.primoServizio.repository;

import com.sincon.primoServizio.model.Dizionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DizionarioRepository extends JpaRepository<Dizionario, Integer>, QuerydslPredicateExecutor<Dizionario> {
}
