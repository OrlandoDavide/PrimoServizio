package com.sincon.primoServizio.repository;

import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.QOrganigrammaStruttura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganigrammaStrutturaRepository extends JpaRepository<OrganigrammaStruttura, Long>, QuerydslPredicateExecutor<QOrganigrammaStruttura> {
}
