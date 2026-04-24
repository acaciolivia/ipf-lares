package com.ipfnoslares.repository;

import com.ipfnoslares.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade Membro.
 * Herda os métodos padrão de CRUD do JpaRepository.
 *
 * Nota: queries com JPQL explícito evitam ambiguidades de derivação
 * automática causadas pela notação húngara nos nomes de campo.
 */
@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {

    /**
     * Busca membros cujo nome contenha o texto informado (ignora maiúsculas/minúsculas).
     */
    @Query("SELECT m FROM Membro m WHERE LOWER(m.sNome) LIKE LOWER(CONCAT('%', :sNome, '%'))")
    List<Membro> findByNomeContendo(@Param("sNome") String sNome);

    /**
     * Busca membros por função na igreja (ignora maiúsculas/minúsculas).
     */
    @Query("SELECT m FROM Membro m WHERE LOWER(m.sFuncao) = LOWER(:sFuncao)")
    List<Membro> findByFuncao(@Param("sFuncao") String sFuncao);
}
