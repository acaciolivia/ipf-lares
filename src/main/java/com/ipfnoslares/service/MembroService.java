package com.ipfnoslares.service;

import com.ipfnoslares.dto.MembroDTO;
import com.ipfnoslares.model.Endereco;
import com.ipfnoslares.model.Membro;
import com.ipfnoslares.repository.EnderecoRepository;
import com.ipfnoslares.repository.MembroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de negócio para operações de CRUD de membros da igreja.
 */
@Service
public class MembroService {

    private static final Logger oLogger = LoggerFactory.getLogger(MembroService.class);

    private final MembroRepository oMembroRepository;
    private final EnderecoRepository oEnderecoRepository;

    public MembroService(MembroRepository oMembroRepository,
                         EnderecoRepository oEnderecoRepository) {
        this.oMembroRepository   = oMembroRepository;
        this.oEnderecoRepository = oEnderecoRepository;
    }

    // =========================================================
    // Listar todos
    // =========================================================

    @Transactional(readOnly = true)
    public List<MembroDTO> listarTodos() {
        return oMembroRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // Buscar por ID
    // =========================================================

    @Transactional(readOnly = true)
    public MembroDTO buscarPorId(Long nId) {
        Membro oMembro = oMembroRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com id: " + nId));
        return toDTO(oMembro);
    }

    // =========================================================
    // Criar
    // =========================================================

    @Transactional
    public MembroDTO criar(MembroDTO oDto) {
        // Busca o endereço vinculado (se informado)
        Endereco oEndereco = resolverEndereco(oDto.getNEnderecoId());

        // Define função padrão se não informada
        String sFuncao = (oDto.getSFuncao() != null && !oDto.getSFuncao().isBlank())
                ? oDto.getSFuncao()
                : "Membro";

        Membro oMembro = Membro.builder()
                .sNome(oDto.getSNome())
                .sTelefone(oDto.getSTelefone())
                .sFuncao(sFuncao)
                .oEndereco(oEndereco)
                .build();

        Membro oSalvo = oMembroRepository.save(oMembro);
        oLogger.info("Membro criado com id: {} vinculado ao endereço: {}",
                oSalvo.getNId(),
                oEndereco != null ? oEndereco.getNId() : "nenhum");
        return toDTO(oSalvo);
    }

    // =========================================================
    // Atualizar
    // =========================================================

    @Transactional
    public MembroDTO atualizar(Long nId, MembroDTO oDto) {
        oMembroRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado com id: " + nId));

        // Busca o endereço vinculado (se informado)
        Endereco oEndereco = resolverEndereco(oDto.getNEnderecoId());

        // Define função padrão se não informada
        String sFuncao = (oDto.getSFuncao() != null && !oDto.getSFuncao().isBlank())
                ? oDto.getSFuncao()
                : "Membro";

        Membro oAtualizado = Membro.builder()
                .nId(nId)
                .sNome(oDto.getSNome())
                .sTelefone(oDto.getSTelefone())
                .sFuncao(sFuncao)
                .oEndereco(oEndereco)
                .build();

        Membro oSalvo = oMembroRepository.save(oAtualizado);
        oLogger.info("Membro atualizado: id={}", oSalvo.getNId());
        return toDTO(oSalvo);
    }

    // =========================================================
    // Excluir
    // =========================================================

    @Transactional
    public void excluir(Long nId) {
        if (!oMembroRepository.existsById(nId)) {
            throw new RuntimeException("Membro não encontrado com id: " + nId);
        }
        oMembroRepository.deleteById(nId);
        oLogger.info("Membro excluído: id={}", nId);
    }

    // =========================================================
    // Métodos auxiliares
    // =========================================================

    /** Busca o Endereco pelo ID, retornando null se não informado. */
    private Endereco resolverEndereco(Long nEnderecoId) {
        if (nEnderecoId == null) return null;
        return oEnderecoRepository.findById(nEnderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado com id: " + nEnderecoId));
    }

    // =========================================================
    // Conversão Entidade → DTO
    // =========================================================

    private MembroDTO toDTO(Membro oMembro) {
        return new MembroDTO(
                oMembro.getNId(),
                oMembro.getSNome(),
                oMembro.getSTelefone(),
                oMembro.getSFuncao(),
                oMembro.getOEndereco() != null ? oMembro.getOEndereco().getNId() : null,
                oMembro.getDtCriacao(),
                oMembro.getDtAtualizacao()
        );
    }
}
