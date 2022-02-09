package org.lsi.dao;

import org.lsi.entities.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeRepository extends JpaRepository<Employe, Long> {

    @Query("select e from Employe e where e.codeEmploye =:x")
    public Employe findOne(@Param("x") Long codeEmployeSup);



}
