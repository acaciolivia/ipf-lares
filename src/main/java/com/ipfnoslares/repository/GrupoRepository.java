package com.ipfnoslares.repository;

import com.ipfnoslares.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /**
     * Verifica se um membro é líder de qualquer grupo.
     * Usado para impedir que um líder seja marcado como Visitante ou Desigrejado.
     */
    @Query("SELECT COUNT(g) > 0 FROM Grupo g WHERE g.oLider.nId = :nMembroId")
    boolean isMembroLiderDeAlgumGrupo(@Param("nMembroId") Long nMembroId);
}
