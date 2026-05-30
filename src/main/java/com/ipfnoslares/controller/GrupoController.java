package com.ipfnoslares.controller;

import com.ipfnoslares.dto.GrupoDTO;
import com.ipfnoslares.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para grupos/células da igreja.
 *
 *  GET    /api/grupos                          → lista todos os grupos (sem membros)
 *  GET    /api/grupos/{id}                     → detalhe do grupo (com lista de membros)
 *  POST   /api/grupos                          → cria grupo
 *  PUT    /api/grupos/{id}                     → atualiza grupo
 *  DELETE /api/grupos/{id}                     → exclui grupo (desvincula membros)
 *  POST   /api/grupos/{id}/membros/{idMembro}  → adiciona membro ao grupo
 *  DELETE /api/grupos/{id}/membros/{idMembro}  → remove membro do grupo
 */
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    private final GrupoService oGrupoService;

    public GrupoController(GrupoService oGrupoService) {
        this.oGrupoService = oGrupoService;
    }

    @GetMapping
    public ResponseEntity<List<GrupoDTO>> listarTodos() {
        return ResponseEntity.ok(oGrupoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(oGrupoService.buscarPorId(id));
        } catch (RuntimeException oEx) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody GrupoDTO oDto) {
        try {
            GrupoDTO oCriado = oGrupoService.criar(oDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(oCriado);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody GrupoDTO oDto) {
        try {
            return ResponseEntity.ok(oGrupoService.atualizar(id, oDto));
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            oGrupoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @PostMapping("/{id}/membros/{idMembro}")
    public ResponseEntity<?> adicionarMembro(@PathVariable Long id, @PathVariable Long idMembro) {
        try {
            oGrupoService.adicionarMembro(id, idMembro);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    @DeleteMapping("/{id}/membros/{idMembro}")
    public ResponseEntity<?> removerMembro(@PathVariable Long id, @PathVariable Long idMembro) {
        try {
            oGrupoService.removerMembro(id, idMembro);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }
}
