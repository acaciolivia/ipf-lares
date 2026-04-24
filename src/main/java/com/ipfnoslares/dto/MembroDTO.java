package com.ipfnoslares.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO para requisições de criação/atualização e para respostas da API de membros.
 *
 * @JsonAutoDetect desabilita a descoberta automática por getters/setters.
 * Combinado com @JsonProperty em cada campo, garante nomes corretos tanto na
 * serialização quanto na desserialização.
 */
@JsonAutoDetect(
        fieldVisibility   = JsonAutoDetect.Visibility.ANY,
        getterVisibility  = JsonAutoDetect.Visibility.NONE,
        setterVisibility  = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class MembroDTO {

    @JsonProperty("nId")
    private Long nId;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    @JsonProperty("sNome")
    private String sNome;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    @JsonProperty("sTelefone")
    private String sTelefone;

    @NotBlank(message = "A função na igreja é obrigatória")
    @Size(max = 50, message = "Função deve ter no máximo 50 caracteres")
    @JsonProperty("sFuncao")
    private String sFuncao;

    /** ID do endereço vinculado (FK) — pode ser null */
    @JsonProperty("nEnderecoId")
    private Long nEnderecoId;

    @JsonProperty("dtCriacao")
    private LocalDateTime dtCriacao;

    @JsonProperty("dtAtualizacao")
    private LocalDateTime dtAtualizacao;

    // =========================================================
    // Construtores
    // =========================================================

    public MembroDTO() {}

    public MembroDTO(Long nId, String sNome, String sTelefone, String sFuncao,
                     Long nEnderecoId,
                     LocalDateTime dtCriacao, LocalDateTime dtAtualizacao) {
        this.nId           = nId;
        this.sNome         = sNome;
        this.sTelefone     = sTelefone;
        this.sFuncao       = sFuncao;
        this.nEnderecoId   = nEnderecoId;
        this.dtCriacao     = dtCriacao;
        this.dtAtualizacao = dtAtualizacao;
    }

    // =========================================================
    // Getters e Setters
    // =========================================================

    public Long getNId()                          { return nId; }
    public void setNId(Long nId)                  { this.nId = nId; }

    public String getSNome()                      { return sNome; }
    public void setSNome(String sNome)            { this.sNome = sNome; }

    public String getSTelefone()                  { return sTelefone; }
    public void setSTelefone(String sTelefone)    { this.sTelefone = sTelefone; }

    public String getSFuncao()                    { return sFuncao; }
    public void setSFuncao(String sFuncao)        { this.sFuncao = sFuncao; }

    public Long getNEnderecoId()                  { return nEnderecoId; }
    public void setNEnderecoId(Long nEnderecoId)  { this.nEnderecoId = nEnderecoId; }

    public LocalDateTime getDtCriacao()           { return dtCriacao; }
    public void setDtCriacao(LocalDateTime dt)    { this.dtCriacao = dt; }

    public LocalDateTime getDtAtualizacao()       { return dtAtualizacao; }
    public void setDtAtualizacao(LocalDateTime dt){ this.dtAtualizacao = dt; }
}
