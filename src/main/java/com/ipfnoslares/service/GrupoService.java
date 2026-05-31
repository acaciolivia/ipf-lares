package com.ipfnoslares.service;

import com.ipfnoslares.dto.GrupoDTO;
import com.ipfnoslares.dto.MembroDTO;
import com.ipfnoslares.model.Grupo;
import com.ipfnoslares.model.Membro;
import com.ipfnoslares.repository.GrupoRepository;
import com.ipfnoslares.repository.MembroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para CRUD de grupos/células.
 *
 * Regras:
 *  - RN-001: cada Membro tem no máximo um grupo (FK em Membro).
 *  - RN-002: cada Grupo tem exatamente um líder (campo NOT NULL).
 */
@Service
public class GrupoService {

    private static final Logger oLogger = LoggerFactory.getLogger(GrupoService.class);

    private final GrupoRepository oGrupoRepository;
    private final MembroRepository oMembroRepository;

    public GrupoService(GrupoRepository oGrupoRepository,
                        MembroRepository oMembroRepository) {
        this.oGrupoRepository  = oGrupoRepository;
        this.oMembroRepository = oMembroRepository;
    }

    // ─────────────────────────────────────────────────────────
    // Listar todos (sem lMembros para resposta leve)
    // ─────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<GrupoDTO> listarTodos() {
        return oGrupoRepository.findAll().stream()
                .map(o -> toDTO(o, false))
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────
    // Buscar por ID (com lista de membros)
    // ─────────────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public GrupoDTO buscarPorId(Long nId) {
        Grupo oGrupo = oGrupoRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado com id: " + nId));
        return toDTO(oGrupo, true);
    }

    // ─────────────────────────────────────────────────────────
    // Criar
    // ─────────────────────────────────────────────────────────
    @Transactional
    public GrupoDTO criar(GrupoDTO oDto) {
        Membro oLider = resolverLider(oDto.getNLiderId());

        Grupo oGrupo = Grupo.builder()
                .sNome(oDto.getSNome())
                .sDescricao(oDto.getSDescricao())
                .oLider(oLider)
                .build();

        Grupo oSalvo = oGrupoRepository.save(oGrupo);
        oLogger.info("Grupo criado: id={} nome={} lider={}", oSalvo.getNId(), oSalvo.getSNome(), oLider.getNId());
        return toDTO(oSalvo, false);
    }

    // ─────────────────────────────────────────────────────────
    // Atualizar
    // ─────────────────────────────────────────────────────────
    @Transactional
    public GrupoDTO atualizar(Long nId, GrupoDTO oDto) {
        Grupo oExistente = oGrupoRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado com id: " + nId));

        Membro oLider = resolverLider(oDto.getNLiderId());

        Grupo oAtualizado = Grupo.builder()
                .nId(nId)
                .sNome(oDto.getSNome())
                .sDescricao(oDto.getSDescricao())
                .oLider(oLider)
                .dtCriacao(oExistente.getDtCriacao())
                .build();

        Grupo oSalvo = oGrupoRepository.save(oAtualizado);
        oLogger.info("Grupo atualizado: id={}", oSalvo.getNId());
        return toDTO(oSalvo, false);
    }

    // ─────────────────────────────────────────────────────────
    // Excluir (desvincula membros automaticamente)
    // ─────────────────────────────────────────────────────────
    @Transactional
    public void excluir(Long nId) {
        Grupo oGrupo = oGrupoRepository.findById(nId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado com id: " + nId));

        // Desvincula membros antes de excluir o grupo (RN-001: garante coerência).
        for (Membro oMembro : oGrupo.getLMembros()) {
            oMembro.setOGrupo(null);
            oMembroRepository.save(oMembro);
        }
        oGrupoRepository.deleteById(nId);
        oLogger.info("Grupo excluído: id={}", nId);
    }

    // ─────────────────────────────────────────────────────────
    // Adicionar membro ao grupo
    // ─────────────────────────────────────────────────────────
    @Transactional
    public void adicionarMembro(Long nGrupoId, Long nMembroId) {
        Grupo oGrupo = oGrupoRepository.findById(nGrupoId)
                .orElseThrow(() -> new RuntimeException("Grupo não encontrado: " + nGrupoId));
        Membro oMembro = oMembroRepository.findById(nMembroId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado: " + nMembroId));
        oMembro.setOGrupo(oGrupo);
        oMembroRepository.save(oMembro);
        oLogger.info("Membro {} adicionado ao grupo {}", nMembroId, nGrupoId);
    }

    // ─────────────────────────────────────────────────────────
    // Remover membro do grupo
    // ─────────────────────────────────────────────────────────
    @Transactional
    public void removerMembro(Long nGrupoId, Long nMembroId) {
        Membro oMembro = oMembroRepository.findById(nMembroId)
                .orElseThrow(() -> new RuntimeException("Membro não encontrado: " + nMembroId));
        if (oMembro.getOGrupo() != null && oMembro.getOGrupo().getNId().equals(nGrupoId)) {
            oMembro.setOGrupo(null);
            oMembroRepository.save(oMembro);
            oLogger.info("Membro {} removido do grupo {}", nMembroId, nGrupoId);
        }
    }

    // ─────────────────────────────────────────────────────────
    // Auxiliares
    // ─────────────────────────────────────────────────────────
    private Membro resolverLider(Long nLiderId) {
        if (nLiderId == null) {
            throw new RuntimeException("O líder do grupo é obrigatório.");
        }
        Membro oLider = oMembroRepository.findById(nLiderId)
                .orElseThrow(() -> new RuntimeException("Líder (membro) não encontrado: " + nLiderId));

        // Regra: um Visitante não pode ser líder
        if ("Visitante".equalsIgnoreCase(oLider.getSFuncao())) {
            throw new RuntimeException(
                    "Um Visitante não pode ser líder de um grupo. Altere a função do membro antes de associá-lo.");
        }
        // Regra: um Desigrejado não pode ser líder
        if (oLider.isBDesigrejado()) {
            throw new RuntimeException(
                    "Um membro Desigrejado não pode ser líder de um grupo.");
        }
        return oLider;
    }

    private GrupoDTO toDTO(Grupo oGrupo, boolean bIncluirMembros) {
        Membro oLider = oGrupo.getOLider();

        List<MembroDTO> lMembrosDto = null;
        if (bIncluirMembros && oGrupo.getLMembros() != null) {
            lMembrosDto = oGrupo.getLMembros().stream()
                    .map(this::membroParaDTOSimples)
                    .collect(Collectors.toList());
        }

        int nQtd = oGrupo.getLMembros() != null ? oGrupo.getLMembros().size() : 0;

        return new GrupoDTO(
                oGrupo.getNId(),
                oGrupo.getSNome(),
                oGrupo.getSDescricao(),
                oLider != null ? oLider.getNId() : null,
                oLider != null ? oLider.getSNome() : null,
                nQtd,
                lMembrosDto,
                oGrupo.getDtCriacao(),
                oGrupo.getDtAtualizacao()
        );
    }

    /** DTO simplificado de Membro para listagem dentro do Grupo. */
    private MembroDTO membroParaDTOSimples(Membro oMembro) {
        return new MembroDTO(
                oMembro.getNId(),
                oMembro.getSNome(),
                oMembro.getSTelefone(),
                oMembro.getSFuncao(),
                oMembro.isBWhatsapp(),
                oMembro.isBAceitaContato(),
                oMembro.isBDesigrejado(),
                oMembro.getOEndereco() != null ? oMembro.getOEndereco().getNId() : null,
                oMembro.getDtCriacao(),
                oMembro.getDtAtualizacao()
        );
    }
}
