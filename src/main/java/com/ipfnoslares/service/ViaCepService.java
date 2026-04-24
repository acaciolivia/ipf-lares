package com.ipfnoslares.service;

import com.ipfnoslares.dto.ViaCepResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Serviço responsável por consultar endereços na API ViaCEP.
 * URL base configurada em application.properties (viacep.url).
 */
@Service
public class ViaCepService {

    private static final Logger oLogger = LoggerFactory.getLogger(ViaCepService.class);

    private final RestTemplate oRestTemplate;

    @Value("${viacep.url}")
    private String sViaCepUrl;

    public ViaCepService(RestTemplate oRestTemplate) {
        this.oRestTemplate = oRestTemplate;
    }

    /**
     * Consulta o endereço completo a partir de um CEP.
     *
     * @param sCep CEP com ou sem traço (ex.: "01001-000" ou "01001000")
     * @return ViaCepResponseDTO preenchido, ou null se o CEP for inválido/não encontrado
     */
    public ViaCepResponseDTO buscarPorCep(String sCep) {
        String sCepLimpo = sCep.replaceAll("[^0-9]", "");

        if (sCepLimpo.length() != 8) {
            oLogger.warn("CEP com formato inválido: {}", sCep);
            return null;
        }

        String sUrl = String.format("%s/%s/json/", sViaCepUrl, sCepLimpo);
        oLogger.debug("Consultando ViaCEP: {}", sUrl);

        try {
            ViaCepResponseDTO oResposta = oRestTemplate.getForObject(sUrl, ViaCepResponseDTO.class);

            if (oResposta == null || oResposta.isCepInvalido()) {
                oLogger.warn("CEP não encontrado: {}", sCep);
                return null;
            }

            return oResposta;
        } catch (RestClientException oEx) {
            oLogger.error("Erro ao consultar ViaCEP para o CEP {}: {}", sCep, oEx.getMessage());
            return null;
        }
    }
}
