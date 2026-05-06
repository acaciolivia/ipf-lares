package com.ipfnoslares.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * DTO de resposta para a busca por endereços próximos.
 * Contém todos os campos do endereço, a lista de membros vinculados
 * e a distância calculada em quilômetros.
 *
 * @JsonAutoDetect + @JsonProperty: mesma estratégia dos demais DTOs
 * para evitar conflito entre getters em Hungarian notation e nomes JSON.
 */
@JsonAutoDetect(
        fieldVisibility    = JsonAutoDetect.Visibility.ANY,
        getterVisibility   = JsonAutoDetect.Visibility.NONE,
        setterVisibility   = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class EnderecoProximoDTO {

    @JsonProperty("nId")
    private Long nId;

    @JsonProperty("sCep")
    private String sCep;

    @JsonProperty("sLogradouro")
    private String sLogradouro;

    @JsonProperty("sNumero")
    private String sNumero;

    @JsonProperty("sComplemento")
    private String sComplemento;

    @JsonProperty("sBairro")
    private String sBairro;

    @JsonProperty("sCidade")
    private String sCidade;

    @JsonProperty("sEstado")
    private String sEstado;

    @JsonProperty("dLatitude")
    private Double dLatitude;

    @JsonProperty("dLongitude")
    private Double dLongitude;

    @JsonProperty("dDistanciaKm")
    private Double dDistanciaKm;

    /** Membros vinculados ao endereço (nome, telefone, função). */
    @JsonProperty("lMembros")
    private List<MembroDTO> lMembros;

    // =========================================================
    // Construtores
    // =========================================================

    public EnderecoProximoDTO() {}

    public EnderecoProximoDTO(Long nId, String sCep, String sLogradouro,
                               String sNumero, String sComplemento, String sBairro,
                               String sCidade, String sEstado,
                               Double dLatitude, Double dLongitude,
                               Double dDistanciaKm,
                               List<MembroDTO> lMembros) {
        this.nId            = nId;
        this.sCep           = sCep;
        this.sLogradouro    = sLogradouro;
        this.sNumero        = sNumero;
        this.sComplemento   = sComplemento;
        this.sBairro        = sBairro;
        this.sCidade        = sCidade;
        this.sEstado        = sEstado;
        this.dLatitude      = dLatitude;
        this.dLongitude     = dLongitude;
        this.dDistanciaKm   = dDistanciaKm;
        this.lMembros       = lMembros;
    }

    // =========================================================
    // Getters e Setters
    // =========================================================

    public Long getNId()                          { return nId; }
    public void setNId(Long nId)                  { this.nId = nId; }

    public String getSCep()                       { return sCep; }
    public void setSCep(String sCep)              { this.sCep = sCep; }

    public String getSLogradouro()                { return sLogradouro; }
    public void setSLogradouro(String sLogradouro){ this.sLogradouro = sLogradouro; }

    public String getSNumero()                    { return sNumero; }
    public void setSNumero(String sNumero)        { this.sNumero = sNumero; }

    public String getSComplemento()               { return sComplemento; }
    public void setSComplemento(String sComp)     { this.sComplemento = sComp; }

    public String getSBairro()                    { return sBairro; }
    public void setSBairro(String sBairro)        { this.sBairro = sBairro; }

    public String getSCidade()                    { return sCidade; }
    public void setSCidade(String sCidade)        { this.sCidade = sCidade; }

    public String getSEstado()                    { return sEstado; }
    public void setSEstado(String sEstado)        { this.sEstado = sEstado; }

    public Double getDLatitude()                  { return dLatitude; }
    public void setDLatitude(Double dLatitude)    { this.dLatitude = dLatitude; }

    public Double getDLongitude()                 { return dLongitude; }
    public void setDLongitude(Double dLongitude)  { this.dLongitude = dLongitude; }

    public Double getDDistanciaKm()               { return dDistanciaKm; }
    public void setDDistanciaKm(Double dDist)     { this.dDistanciaKm = dDist; }

    public List<MembroDTO> getLMembros()              { return lMembros; }
    public void setLMembros(List<MembroDTO> lMembros) { this.lMembros = lMembros; }
}
