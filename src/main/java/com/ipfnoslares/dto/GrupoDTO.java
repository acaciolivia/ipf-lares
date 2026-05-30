package com.ipfnoslares.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de requisição/resposta para Grupos/Células.
 *
 * Inclui:
 *  - nLiderId  : ID do membro líder (obrigatório, RN-002)
 *  - sNomeLider: nome do líder (somente leitura, preenchido na resposta)
 *  - lMembros  : lista de membros vinculados (preenchida na resposta de detalhe)
 */
@JsonAutoDetect(
        fieldVisibility    = JsonAutoDetect.Visibility.ANY,
        getterVisibility   = JsonAutoDetect.Visibility.NONE,
        setterVisibility   = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class GrupoDTO {

    @JsonProperty("nId")
    private Long nId;

    @NotBlank(message = "O nome do grupo é obrigatório")
    @Size(max = 120, message = "Nome do grupo deve ter no máximo 120 caracteres")
    @JsonProperty("sNome")
    private String sNome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @JsonProperty("sDescricao")
    private String sDescricao;

    @NotNull(message = "O líder do grupo é obrigatório")
    @JsonProperty("nLiderId")
    private Long nLiderId;

    @JsonProperty("sNomeLider")
    private String sNomeLider;

    @JsonProperty("nQuantidadeMembros")
    private Integer nQuantidadeMembros;

    @JsonProperty("lMembros")
    private List<MembroDTO> lMembros;

    @JsonProperty("dtCriacao")
    private LocalDateTime dtCriacao;

    @JsonProperty("dtAtualizacao")
    private LocalDateTime dtAtualizacao;

    public GrupoDTO() {}

    public GrupoDTO(Long nId, String sNome, String sDescricao,
                    Long nLiderId, String sNomeLider,
                    Integer nQuantidadeMembros, List<MembroDTO> lMembros,
                    LocalDateTime dtCriacao, LocalDateTime dtAtualizacao) {
        this.nId                = nId;
        this.sNome              = sNome;
        this.sDescricao         = sDescricao;
        this.nLiderId           = nLiderId;
        this.sNomeLider         = sNomeLider;
        this.nQuantidadeMembros = nQuantidadeMembros;
        this.lMembros           = lMembros;
        this.dtCriacao          = dtCriacao;
        this.dtAtualizacao      = dtAtualizacao;
    }

    // Getters / Setters
    public Long getNId()                              { return nId; }
    public void setNId(Long n)                        { this.nId = n; }

    public String getSNome()                          { return sNome; }
    public void setSNome(String s)                    { this.sNome = s; }

    public String getSDescricao()                     { return sDescricao; }
    public void setSDescricao(String s)               { this.sDescricao = s; }

    public Long getNLiderId()                         { return nLiderId; }
    public void setNLiderId(Long n)                   { this.nLiderId = n; }

    public String getSNomeLider()                     { return sNomeLider; }
    public void setSNomeLider(String s)               { this.sNomeLider = s; }

    public Integer getNQuantidadeMembros()            { return nQuantidadeMembros; }
    public void setNQuantidadeMembros(Integer n)      { this.nQuantidadeMembros = n; }

    public List<MembroDTO> getLMembros()              { return lMembros; }
    public void setLMembros(List<MembroDTO> l)        { this.lMembros = l; }

    public LocalDateTime getDtCriacao()               { return dtCriacao; }
    public void setDtCriacao(LocalDateTime dt)        { this.dtCriacao = dt; }

    public LocalDateTime getDtAtualizacao()           { return dtAtualizacao; }
    public void setDtAtualizacao(LocalDateTime dt)    { this.dtAtualizacao = dt; }
}
