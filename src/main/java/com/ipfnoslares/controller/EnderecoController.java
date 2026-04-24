package com.ipfnoslares.controller;

import com.ipfnoslares.dto.EnderecoDTO;
import com.ipfnoslares.dto.EnderecoProximoDTO;
import com.ipfnoslares.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de endereços.
 *
 * Endpoints disponíveis:
 *   GET    /api/enderecos               — Lista todos os endereços
 *   GET    /api/enderecos/{id}          — Busca endereço por ID
 *   POST   /api/enderecos               — Cria novo endereço (via CEP)
 *   PUT    /api/enderecos/{id}          — Atualiza endereço existente
 *   DELETE /api/enderecos/{id}          — Remove endereço
 *   GET    /api/enderecos/cep/{cep}     — Consulta ViaCEP (autopreenchimento, sem salvar)
 *   GET    /api/enderecos/proximos      — Lista endereços ordenados por proximidade
 */
@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService oEnderecoService;

    public EnderecoController(EnderecoService oEnderecoService) {
        this.oEnderecoService = oEnderecoService;
    }

    // =========================================================
    // GET /api/enderecos
    // =========================================================

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() {
        List<EnderecoDTO> lEnderecos = oEnderecoService.listarTodos();
        return ResponseEntity.ok(lEnderecos);
    }

    // =========================================================
    // GET /api/enderecos/{id}
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> buscarPorId(@PathVariable Long id) {
        try {
            EnderecoDTO oEndereco = oEnderecoService.buscarPorId(id);
            return ResponseEntity.ok(oEndereco);
        } catch (RuntimeException oEx) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // POST /api/enderecos
    // =========================================================

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody EnderecoDTO oDto) {
        try {
            EnderecoDTO oCriado = oEnderecoService.criar(oDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(oCriado);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    // =========================================================
    // PUT /api/enderecos/{id}
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id,
                                        @Valid @RequestBody EnderecoDTO oDto) {
        try {
            EnderecoDTO oAtualizado = oEnderecoService.atualizar(id, oDto);
            return ResponseEntity.ok(oAtualizado);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    // =========================================================
    // DELETE /api/enderecos/{id}
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            oEnderecoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException oEx) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // GET /api/enderecos/cep/{cep}  — Autopreenchimento via ViaCEP
    // =========================================================

    @GetMapping("/cep/{cep}")
    public ResponseEntity<?> consultarCep(@PathVariable String cep) {
        try {
            EnderecoDTO oEndereco = oEnderecoService.consultarViaCep(cep);
            return ResponseEntity.ok(oEndereco);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }

    // =========================================================
    // GET /api/enderecos/proximos?cep={cep}
    // =========================================================

    /**
     * Retorna endereços cadastrados ordenados por distância crescente em relação
     * ao CEP informado. Se {@code raioKm} for informado, retorna apenas os
     * endereços dentro desse raio (valores aceitos: 5 ou 10).
     *
     * @param cep    CEP de referência do usuário
     * @param raioKm Raio máximo em km (opcional; null = todos)
     */
    @GetMapping("/proximos")
    public ResponseEntity<?> buscarProximos(
            @RequestParam String cep,
            @RequestParam(required = false) Double raioKm) {
        try {
            List<EnderecoProximoDTO> lProximos = oEnderecoService.buscarProximos(cep, raioKm);
            return ResponseEntity.ok(lProximos);
        } catch (RuntimeException oEx) {
            return ResponseEntity.badRequest().body(oEx.getMessage());
        }
    }
}
