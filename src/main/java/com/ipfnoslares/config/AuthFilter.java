package com.ipfnoslares.config;

import com.ipfnoslares.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que protege os endpoints de mutação (POST/PUT/DELETE) das APIs de
 * gerenciamento. GETs continuam públicos para a Listagem funcionar sem login.
 *
 * Rotas protegidas (qualquer método que não seja GET):
 *  - /api/enderecos/**
 *  - /api/membros/**
 *  - /api/grupos/**
 *
 * Rotas SEMPRE públicas:
 *  - /api/auth/** (login, logout, verificar)
 *  - qualquer GET nos recursos (consulta de listagem/proximidade)
 *  - /api/enderecos/cep/** e /api/enderecos/proximos (consulta)
 */
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final AuthService oAuthService;

    public AuthFilter(AuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     FilterChain chain) throws ServletException, IOException {
        String sPath   = req.getRequestURI();
        String sMetodo = req.getMethod();

        if (precisaAutenticacao(sPath, sMetodo)) {
            String sToken = req.getHeader("X-Auth-Token");
            if (!oAuthService.tokenValido(sToken)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write(
                        "{\"erro\":\"Sessao expirada ou nao autenticado. Faca login novamente.\"}");
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    /**
     * Determina se a requisição precisa de autenticação.
     * Regra: apenas mutações (POST/PUT/DELETE/PATCH) em /api/enderecos, /api/membros e /api/grupos.
     */
    private boolean precisaAutenticacao(String sPath, String sMetodo) {
        if ("GET".equalsIgnoreCase(sMetodo) || "OPTIONS".equalsIgnoreCase(sMetodo)) {
            return false;
        }
        if (sPath == null) return false;
        if (sPath.startsWith("/api/auth")) return false;

        return sPath.startsWith("/api/enderecos")
                || sPath.startsWith("/api/membros")
                || sPath.startsWith("/api/grupos");
    }
}
