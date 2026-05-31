package com.ipfnoslares.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de autenticação por senha (RF-004, RN-003).
 *
 * - A senha é armazenada como hash BCrypt na variável de ambiente APP_ADMIN_PASSWORD_HASH.
 * - Se a variável não for definida, usa um hash default (senha "igreja2026") apenas
 *   para facilitar o setup inicial. TROCAR no Railway logo após o primeiro deploy!
 *
 * - Tokens são UUIDs em memória com expiração configurável (default: 4 horas).
 *   Como o app roda em uma única instância, manter em memória é aceitável; ao
 *   reiniciar o container, todos precisam autenticar novamente.
 */
@Service
public class AuthService {

    private static final Logger oLogger = LoggerFactory.getLogger(AuthService.class);

    /** Hash padrão da senha "igreja2026" — substituir via env var em produção. */
    private static final String DEFAULT_HASH =
            "$2b$10$uDum4hDk0qTbZPbwISFJVexgqYirpg963TUfIWx3qlGmZTQAPQgMC";

    /** Duração da sessão em horas. */
    private static final long DURACAO_SESSAO_HORAS = 4L;

    private final BCryptPasswordEncoder oEncoder = new BCryptPasswordEncoder();
    private final ConcurrentHashMap<String, Instant> mTokens = new ConcurrentHashMap<>();

    @Value("${APP_ADMIN_PASSWORD_HASH:}")
    private String sHashConfigurado;

    /** Retorna o hash efetivo (variável de ambiente ou default). */
    private String obterHashAtivo() {
        if (sHashConfigurado != null && !sHashConfigurado.isBlank()) {
            return sHashConfigurado;
        }
        oLogger.warn("APP_ADMIN_PASSWORD_HASH não configurado — usando senha padrão. "
                + "Configure no Railway para produção!");
        return DEFAULT_HASH;
    }

    /**
     * Valida a senha informada. Se válida, gera e retorna um token de sessão.
     * Lança RuntimeException se inválida.
     */
    public String autenticar(String sSenha) {
        if (sSenha == null || sSenha.isBlank()) {
            throw new RuntimeException("Senha não informada.");
        }
        boolean bOk = oEncoder.matches(sSenha, obterHashAtivo());
        if (!bOk) {
            throw new RuntimeException("Senha incorreta.");
        }
        String sToken = UUID.randomUUID().toString();
        Instant dtExpiracao = Instant.now().plusSeconds(DURACAO_SESSAO_HORAS * 3600L);
        mTokens.put(sToken, dtExpiracao);
        limparExpirados();
        oLogger.info("Sessão autenticada (token={}... expira={})", sToken.substring(0, 8), dtExpiracao);
        return sToken;
    }

    /** True se o token existe e não está expirado. */
    public boolean tokenValido(String sToken) {
        if (sToken == null || sToken.isBlank()) return false;
        Instant dtExp = mTokens.get(sToken);
        if (dtExp == null) return false;
        if (Instant.now().isAfter(dtExp)) {
            mTokens.remove(sToken);
            return false;
        }
        return true;
    }

    /** Remove tokens expirados (chamado a cada autenticação). */
    private void limparExpirados() {
        Instant agora = Instant.now();
        mTokens.entrySet().removeIf(e -> agora.isAfter(e.getValue()));
    }

    /** Encerra uma sessão. */
    public void encerrar(String sToken) {
        if (sToken != null) mTokens.remove(sToken);
    }

    /** Duração configurada (para informar ao front). */
    public long getDuracaoSessaoHoras() {
        return DURACAO_SESSAO_HORAS;
    }
}
