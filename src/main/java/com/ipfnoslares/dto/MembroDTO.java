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
        fieldVisibility    = JsonAutoDetect.Visibility.ANY,
        getterVisibility   = JsonAutoDetect.Visibility.NONE,
        setterVisibility   = JsonAutoDetect.Visibility.NONE,
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

    /** Indica se o telefone informado é WhatsApp. */
    @JsonProperty("bWhatsapp")
    private boolean bWhatsapp;

    /** Indica se a pessoa autorizou ser contactada pela igreja. */
    @JsonProperty("bAceitaContato")
    private boolean bAceitaContato;

    /** Indica se o membro esta desigrejado (afastado). */
    @JsonProperty("bDesigrejado")
    private boolean bDesigrejado;

    /** ID do endereço vinculado (FK) — pode ser null */
    @JsonProperty("nEnderecoId")
    private Long nEnderecoId;

    /** ID do grupo/célula ao qual o membro pertence — pode ser null (RN-001). */
    @JsonProperty("nGrupoId")
    private Long nGrupoId;

    /** Nome do grupo (somente leitura, preenchido na resposta). */
    @JsonProperty("sNomeGrupo")
    private String sNomeGrupo;

    /** Nome do líder do grupo (somente leitura, preenchido na resposta). */
    @JsonProperty("sNomeLiderGrupo")
    private String sNomeLiderGrupo;

    @JsonProperty("dtCriacao")
    private LocalDateTime dtCriacao;

    @JsonProperty("dtAtualizacao")
    private LocalDateTime dtAtualizacao;

    // =========================================================
    // Construtores
    // =========================================================

    public MembroDTO() {}

    public MembroDTO(Long nId, String sNome, String sTelefone, String sFuncao,
                     boolean bWhatsapp, boolean bAceitaContato, boolean bDesigrejado,
                     Long nEnderecoId,
                     LocalDateTime dtCriacao, LocalDateTime dtAtualizacao) {
        this(nId, sNome, sTelefone, sFuncao, bWhatsapp, bAceitaContato, bDesigrejado,
             nEnderecoId, null, null, null, dtCriacao, dtAtualizacao);
    }

    public MembroDTO(Long nId, String sNome, String sTelefone, String sFuncao,
                     boolean bWhatsapp, boolean bAceitaContato, boolean bDesigrejado,
                     Long nEnderecoId, Long nGrupoId, String sNomeGrupo, String sNomeLiderGrupo,
                     LocalDateTime dtCriacao, LocalDateTime dtAtualizacao) {
        this.nId              = nId;
        this.sNome            = sNome;
        this.sTelefone        = sTelefone;
        this.sFuncao          = sFuncao;
        this.bWhatsapp        = bWhatsapp;
        this.bAceitaContato   = bAceitaContato;
        this.bDesigrejado     = bDesigrejado;
        this.nEnderecoId      = nEnderecoId;
        this.nGrupoId         = nGrupoId;
        this.sNomeGrupo       = sNomeGrupo;
        this.sNomeLiderGrupo  = sNomeLiderGrupo;
        this.dtCriacao        = dtCriacao;
        this.dtAtualizacao    = dtAtualizacao;
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

    public boolean isBWhatsapp()                  { return bWhatsapp; }
    public void setBWhatsapp(boolean bWhatsapp)   { this.bWhatsapp = bWhatsapp; }

    public boolean isBAceitaContato()             { return bAceitaContato; }
    public void setBAceitaContato(boolean b)      { this.bAceitaContato = b; }

    public boolean isBDesigrejado()               { return bDesigrejado; }
    public void setBDesigrejado(boolean b)        { this.bDesigrejado = b; }

    public Long getNEnderecoId()                  { return nEnderecoId; }
    public void setNEnderecoId(Long nEnderecoId)  { this.nEnderecoId = nEnderecoId; }

    public Long getNGrupoId()                     { return nGrupoId; }
    public void setNGrupoId(Long nGrupoId)        { this.nGrupoId = nGrupoId; }

    public String getSNomeGrupo()                 { return sNomeGrupo; }
    public void setSNomeGrupo(String s)           { this.sNomeGrupo = s; }

    public String getSNomeLiderGrupo()            { return sNomeLiderGrupo; }
    public void setSNomeLiderGrupo(String s)      { this.sNomeLiderGrupo = s; }

    public LocalDateTime getDtCriacao()           { return dtCriacao; }
    public void setDtCriacao(LocalDateTime dt)    { this.dtCriacao = dt; }

    public LocalDateTime getDtAtualizacao()       { return dtAtualizacao; }
    public void setDtAtualizacao(LocalDateTime dt){ this.dtAtualizacao = dt; }
}
