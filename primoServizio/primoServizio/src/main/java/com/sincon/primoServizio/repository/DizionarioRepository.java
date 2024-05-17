package com.sincon.primoServizio.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DizionarioRepository extends JpaRepository<Dizionario, Long>, QuerydslPredicateExecutor<Dizionario> {

}
