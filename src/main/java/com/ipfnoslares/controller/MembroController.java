package com.ipfnoslares.controller;

import com.ipfnoslares.dto.MembroDTO;
import com.ipfnoslares.service.MembroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de membros da igreja.
 *
 * Endpoints disponíveis:
 *   GET    /api/membros        — Lista todos os membros
 *   GET    /api/membros/{id}   — Busca membro por ID
 *   POST   /api/membros        — Cadastra novo membro
 *   PUT    /api/membros/{id}   — Atualiza membro existente
 *   DELETE /api/membros/{id}   — Remove membro
 */
@RestController
@RequestMapping("/api/membros")
public class MembroController {

    private final MembroService oMembroService;

    public MembroController(MembroService oMembroService) {
        this.oMembroService = oMembroService;
    }

    // =========================================================
    // GET /api/membros
    // =========================================================

    @GetMapping
    public ResponseEntity<List<MembroDTO>> listarTodos() {
        List<MembroDTO> lMembros = oMembroService.listarTodos();
        return ResponseEntity.ok(lMembros);
    }

    // =========================================================
    // GET /api/membros/{id}
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<MembroDTO> buscarPorId(@PathVariable Long id) {
        try {
            MembroDTO oMembro = oMembroService.buscarPorId(id);
            return ResponseEntity.ok(oMembro);
        } catch (RuntimeException oEx) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // POST /api/membros
    // =========================================================

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody MembroDTO oDto) {
        try {
            MembroDTO oCriado = oMembroService.criar(oDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(oCriado);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    // =========================================================
    // PUT /api/membros/{id}
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                        @Valid @RequestBody MembroDTO oDto) {
        try {
            MembroDTO oAtualizado = oMembroService.atualizar(id, oDto);
            return ResponseEntity.ok(oAtualizado);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    // =========================================================
    // DELETE /api/membros/{id}
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            oMembroService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException oEx) {
            return ResponseEntity.notFound().build();
        }
    }
}
