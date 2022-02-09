package org.lsi.dao;

import org.lsi.entities.Client;
import org.lsi.entities.Compte;
import org.lsi.entities.CompteCourant;
import org.lsi.entities.CompteEpargne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface CompteRepository extends JpaRepository<Compte, String> {

    @Query("select p from Compte p where p.client like :x")
    public Page<Compte> findC(@Param("x") Client id, Pageable pageable);

    @Query("select c from Compte c where c.codeCompte like :x")
    public Compte findOne(@Param("x")String id);

    //@Query("select p from Compte p")
    //public List<Compte> findAll();

    @Query("select cp from Compte cp where cp.codeCompte like :x2")
    public Page<Compte> search(@Param("x2")String code, Pageable pageable);

    @Query("select cp3 from CompteCourant cp3 where cp3.codeCompte like :x2")
    public Page<CompteCourant> CC(@Param("x2")String code2, Pageable pageable);

    @Query("select cp4 from CompteEpargne cp4 where cp4.codeCompte like :x2")
    public Page<CompteEpargne> CE(@Param("x2")String code3, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Compte cp2 set cp2.etat = 0 where cp2.codeCompte like :x3")
    public void update(@Param("x3")String code);

}
