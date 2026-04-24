package com.ipfnoslares.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para requisições de criação/atualização e para respostas da API de endereços.
 *
 * @JsonAutoDetect desabilita a descoberta automática por getters/setters.
 * Isso elimina o conflito entre getSCep() → "SCep" (JavaBeans) e @JsonProperty("sCep").
 * Combinado com @JsonProperty em cada campo, garante nomes corretos tanto na
 * serialização (resposta) quanto na desserialização (requisição).
 */
@JsonAutoDetect(
        fieldVisibility   = JsonAutoDetect.Visibility.ANY,
        getterVisibility  = JsonAutoDetect.Visibility.NONE,
        setterVisibility  = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class EnderecoDTO {

    @JsonProperty("nId")
    private Long nId;

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido. Formato esperado: 00000-000")
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

    /** Membros vinculados a este endereço (somente na resposta) */
    @JsonProperty("lMembros")
    private List<MembroDTO> lMembros;

    @JsonProperty("dtCriacao")
    private LocalDateTime dtCriacao;

    @JsonProperty("dtAtualizacao")
    private LocalDateTime dtAtualizacao;

    // =========================================================
    // Construtores
    // =========================================================

    public EnderecoDTO() {}

    public EnderecoDTO(Long nId, String sCep, String sLogradouro, String sNumero,
                       String sComplemento, String sBairro, String sCidade,
                       String sEstado, Double dLatitude, Double dLongitude,
                       List<MembroDTO> lMembros,
                       LocalDateTime dtCriacao, LocalDateTime dtAtualizacao) {
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
        this.lMembros       = lMembros;
        this.dtCriacao      = dtCriacao;
        this.dtAtualizacao  = dtAtualizacao;
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

    public List<MembroDTO> getLMembros()           { return lMembros; }
    public void setLMembros(List<MembroDTO> l)     { this.lMembros = l; }

    public LocalDateTime getDtCriacao()           { return dtCriacao; }
    public void setDtCriacao(LocalDateTime dt)    { this.dtCriacao = dt; }

    public LocalDateTime getDtAtualizacao()       { return dtAtualizacao; }
    public void setDtAtualizacao(LocalDateTime dt){ this.dtAtualizacao = dt; }
}
