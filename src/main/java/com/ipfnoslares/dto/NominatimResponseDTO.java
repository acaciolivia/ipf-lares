package com.ipfnoslares.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que mapeia um item da resposta da API Nominatim (OpenStreetMap).
 * A API retorna um array — usamos o primeiro resultado.
 * Documentação: https://nominatim.org/release-docs/develop/api/Search/
 *
 * Exemplo de resposta:
 * [
 *   {
 *     "lat": "-23.5505199",
 *     "lon": "-46.6333094",
 *     "display_name": "São Paulo, ..."
 *   }
 * ]
 */
public class NominatimResponseDTO {

    /** Latitude como String (formato retornado pela API) */
    @JsonProperty("lat")
    private String sLat;

    /** Longitude como String (formato retornado pela API) */
    @JsonProperty("lon")
    private String sLon;

    @JsonProperty("display_name")
    private String sDisplayName;

    public NominatimResponseDTO() {}

    // =========================================================
    // Getters e Setters
    // =========================================================

    public String getSLat()                   { return sLat; }
    public void setSLat(String sLat)          { this.sLat = sLat; }

    public String getSLon()                   { return sLon; }
    public void setSLon(String sLon)          { this.sLon = sLon; }

    public String getSDisplayName()           { return sDisplayName; }
    public void setSDisplayName(String sName) { this.sDisplayName = sName; }

    /** Converte a latitude para Double */
    public Double getDLatitude() {
        if (sLat == null || sLat.isBlank()) return null;
        return Double.parseDouble(sLat);
    }

    /** Converte a longitude para Double */
    public Double getDLongitude() {
        if (sLon == null || sLon.isBlank()) return null;
        return Double.parseDouble(sLon);
    }
}
