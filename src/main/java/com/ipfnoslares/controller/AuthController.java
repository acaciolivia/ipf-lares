package com.ipfnoslares.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ipfnoslares.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Endpoints de autenticação para acesso à área administrativa.
 *
 *  POST /api/auth/login      — valida senha, retorna token + duração
 *  GET  /api/auth/verificar  — verifica se o token enviado ainda é válido
 *  POST /api/auth/logout     — encerra a sessão
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService oAuthService;

    public AuthController(AuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPayload oPayload) {
        try {
            String sToken = oAuthService.autenticar(oPayload.getSSenha());
            Map<String, Object> mResp = new HashMap<>();
            mResp.put("sToken", sToken);
            mResp.put("nHorasValido", oAuthService.getDuracaoSessaoHoras());
            return ResponseEntity.ok(mResp);
        } catch (RuntimeException oEx) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(oEx.getMessage());
        }
    }

    @GetMapping("/verificar")
    public ResponseEntity<?> verificar(@RequestHeader(value = "X-Auth-Token", required = false) String sToken) {
        boolean bValido = oAuthService.tokenValido(sToken);
        Map<String, Object> mResp = new HashMap<>();
        mResp.put("bValido", bValido);
        return ResponseEntity.ok(mResp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "X-Auth-Token", required = false) String sToken) {
        oAuthService.encerrar(sToken);
        return ResponseEntity.noContent().build();
    }

    /** Payload de login. Anotações Jackson para serialização correta com Hungarian. */
    @JsonAutoDetect(
            fieldVisibility    = JsonAutoDetect.Visibility.ANY,
            getterVisibility   = JsonAutoDetect.Visibility.NONE,
            setterVisibility   = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE
    )
    public static class LoginPayload {
        @JsonProperty("sSenha")
        private String sSenha;

        public String getSSenha()           { return sSenha; }
        public void setSSenha(String s)     { this.sSenha = s; }
    }
}
