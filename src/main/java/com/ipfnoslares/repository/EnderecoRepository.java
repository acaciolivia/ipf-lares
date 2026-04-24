package com.ipfnoslares.repository;

import com.ipfnoslares.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    /**
     * Retorna todos os endereços que possuem coordenadas geográficas.
     * Usado na busca por proximidade.
     */
    @Query("SELECT e FROM Endereco e WHERE e.dLatitude IS NOT NULL AND e.dLongitude IS NOT NULL")
    List<Endereco> findAllComCoordenadas();

    /**
     * Busca endereços pelo CEP exato.
     */
    List<Endereco> findBysCep(String sCep);
}
