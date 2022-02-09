package org.lsi.dao;

import org.lsi.entities.Groupe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//public interface GroupeRepository extends JpaRepository<Groupe, Long> {
public interface GroupeRepository extends JpaRepository<Groupe, Long> {

    @Query("select g from Groupe g where g.nomGroupe like:x")

    public Page<Groupe> chercher(@Param("x") String motC, Pageable pageable);

}
