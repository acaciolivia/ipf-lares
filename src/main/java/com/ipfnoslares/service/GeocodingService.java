package com.ipfnoslares.service;

import com.ipfnoslares.dto.NominatimResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Serviço responsável por geocodificar endereços usando a API Nominatim (OpenStreetMap).
 * Converte endereços textuais em coordenadas geográficas (latitude/longitude).
 *
 * ATENÇÃO: O Nominatim possui limite de 1 requisição/segundo. Para produção,
 * considere uma API paga (Google Maps, HERE, etc.) ou cache dos resultados.
 */
@Service
public class GeocodingService {

    private static final Logger oLogger = LoggerFactory.getLogger(GeocodingService.class);

    /** Raio médio da Terra em quilômetros */
    private static final double D_RAIO_TERRA_KM = 6371.0;

    private final RestTemplate oRestTemplate;

    @Value("${nominatim.url}")
    private String sNominatimUrl;

    public GeocodingService(RestTemplate oRestTemplate) {
        this.oRestTemplate = oRestTemplate;
    }

    /**
     * Obtém as coordenadas geográficas de um endereço completo.
     *
     * @param sLogradouro Logradouro
     * @param sNumero     Número
     * @param sBairro     Bairro
     * @param sCidade     Cidade
     * @param sEstado     Estado (sigla)
     * @return Array com [latitude, longitude], ou null se não encontrado
     */
    public Double[] geocodificar(String sLogradouro, String sNumero,
                                  String sBairro, String sCidade, String sEstado) {
        String sEndereco = montarEnderecoCompleto(sLogradouro, sNumero, sBairro, sCidade, sEstado);
        return geocodificarEndereco(sEndereco);
    }

    /**
     * Geocodifica diretamente pelo CEP, sem depender do ViaCEP.
     * O Nominatim resolve CEPs brasileiros no formato "XXXXXXXX, Brasil".
     * Elimina uma chamada de rede extra e evita falhas quando o ViaCEP
     * está inacessível.
     *
     * @param sCep CEP com ou sem traço
     * @return Array com [latitude, longitude], ou null se não encontrado
     */
    public Double[] geocodificarPorCep(String sCep) {
        String sCepLimpo = sCep.replaceAll("[^0-9]", "");
        String sEndereco = String.format("%s, Brasil", sCepLimpo);
        return geocodificarEndereco(sEndereco);
    }

    /**
     * Calcula a distância em quilômetros entre dois pontos geográficos
     * usando a fórmula de Haversine.
     *
     * @param dLat1 Latitude do ponto 1
     * @param dLon1 Longitude do ponto 1
     * @param dLat2 Latitude do ponto 2
     * @param dLon2 Longitude do ponto 2
     * @return Distância em quilômetros
     */
    public double calcularDistanciaKm(double dLat1, double dLon1,
                                       double dLat2, double dLon2) {
        double dDeltaLat = Math.toRadians(dLat2 - dLat1);
        double dDeltaLon = Math.toRadians(dLon2 - dLon1);

        double dA = Math.sin(dDeltaLat / 2) * Math.sin(dDeltaLat / 2)
                + Math.cos(Math.toRadians(dLat1)) * Math.cos(Math.toRadians(dLat2))
                * Math.sin(dDeltaLon / 2) * Math.sin(dDeltaLon / 2);

        double dC = 2 * Math.atan2(Math.sqrt(dA), Math.sqrt(1 - dA));

        return D_RAIO_TERRA_KM * dC;
    }

    // =========================================================
    // Métodos privados
    // =========================================================

    private Double[] geocodificarEndereco(String sEndereco) {
        oLogger.debug("Geocodificando endereço: {}", sEndereco);

        try {
            URI oUri = UriComponentsBuilder.fromHttpUrl(sNominatimUrl)
                    .queryParam("q", sEndereco)
                    .queryParam("format", "json")
                    .queryParam("limit", "1")
                    .queryParam("countrycodes", "br")
                    .build()
                    .encode()
                    .toUri();

            NominatimResponseDTO[] aResultados = oRestTemplate.getForObject(
                    oUri, NominatimResponseDTO[].class);

            if (aResultados == null || aResultados.length == 0) {
                oLogger.warn("Nenhuma coordenada encontrada para: {}", sEndereco);
                return null;
            }

            NominatimResponseDTO oResultado = aResultados[0];
            oLogger.debug("Coordenadas encontradas: lat={}, lon={}",
                    oResultado.getSLat(), oResultado.getSLon());

            return new Double[]{ oResultado.getDLatitude(), oResultado.getDLongitude() };

        } catch (RestClientException oEx) {
            oLogger.error("Erro ao geocodificar '{}': {}", sEndereco, oEx.getMessage());
            return null;
        }
    }

    private String montarEnderecoCompleto(String sLogradouro, String sNumero,
                                           String sBairro, String sCidade, String sEstado) {
        StringBuilder oSb = new StringBuilder();
        if (sLogradouro != null && !sLogradouro.isBlank()) oSb.append(sLogradouro);
        if (sNumero     != null && !sNumero.isBlank())     oSb.append(", ").append(sNumero);
        if (sBairro     != null && !sBairro.isBlank())     oSb.append(", ").append(sBairro);
        if (sCidade     != null && !sCidade.isBlank())     oSb.append(", ").append(sCidade);
        if (sEstado     != null && !sEstado.isBlank())     oSb.append(", ").append(sEstado);
        oSb.append(", Brasil");
        return oSb.toString();
    }
}
