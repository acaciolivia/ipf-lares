package com.ipfnoslares.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa um membro da igreja cadastrado no sistema.
 * Cada membro pertence a um endereço (relacionamento ManyToOne).
 * Utiliza o padrão Builder para construção de instâncias.
 *
 * Convenção de variáveis (Hungarian notation):
 *   n  = numérico (Long, Integer)
 *   s  = String
 *   o  = Objeto
 *   dt = LocalDateTime
 */
@Entity
@Table(name = "membros")
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long nId;

    @Column(name = "nome", nullable = false, length = 150)
    private String sNome;

    @Column(name = "telefone", length = 20)
    private String sTelefone;

    @Column(name = "funcao", nullable = false, length = 50)
    private String sFuncao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_id")
    private Endereco oEndereco;

    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dtCriacao;

    @Column(name = "dt_atualizacao")
    private LocalDateTime dtAtualizacao;

    // Construtor protegido exigido pelo JPA
    protected Membro() {}

    // Construtor privado — uso exclusivo do Builder
    private Membro(Builder oBuilder) {
        this.nId           = oBuilder.nId;
        this.sNome         = oBuilder.sNome;
        this.sTelefone     = oBuilder.sTelefone;
        this.sFuncao       = oBuilder.sFuncao;
        this.oEndereco     = oBuilder.oEndereco;
        this.dtCriacao     = oBuilder.dtCriacao;
        this.dtAtualizacao = oBuilder.dtAtualizacao;
    }

    // =========================================================
    // Getters
    // =========================================================

    public Long getNId()                      { return nId; }
    public String getSNome()                  { return sNome; }
    public String getSTelefone()              { return sTelefone; }
    public String getSFuncao()                { return sFuncao; }
    public Endereco getOEndereco()            { return oEndereco; }
    public LocalDateTime getDtCriacao()       { return dtCriacao; }
    public LocalDateTime getDtAtualizacao()   { return dtAtualizacao; }

    // =========================================================
    // Pré-persistência
    // =========================================================

    @PrePersist
    private void prePersist() {
        this.dtCriacao     = LocalDateTime.now();
        this.dtAtualizacao = LocalDateTime.now();
        if (this.sFuncao == null || this.sFuncao.isBlank()) {
            this.sFuncao = "Membro";
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.dtAtualizacao = LocalDateTime.now();
    }

    // =========================================================
    // Builder
    // =========================================================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long nId;
        private String sNome;
        private String sTelefone;
        private String sFuncao;
        private Endereco oEndereco;
        private LocalDateTime dtCriacao;
        private LocalDateTime dtAtualizacao;

        private Builder() {}

        public Builder nId(Long nId) {
            this.nId = nId;
            return this;
        }

        public Builder sNome(String sNome) {
            this.sNome = sNome;
            return this;
        }

        public Builder sTelefone(String sTelefone) {
            this.sTelefone = sTelefone;
            return this;
        }

        public Builder sFuncao(String sFuncao) {
            this.sFuncao = sFuncao;
            return this;
        }

        public Builder oEndereco(Endereco oEndereco) {
            this.oEndereco = oEndereco;
            return this;
        }

        public Builder dtCriacao(LocalDateTime dtCriacao) {
            this.dtCriacao = dtCriacao;
            return this;
        }

        public Builder dtAtualizacao(LocalDateTime dtAtualizacao) {
            this.dtAtualizacao = dtAtualizacao;
            return this;
        }

        public Membro build() {
            return new Membro(this);
        }
    }

    @Override
    public String toString() {
        return "Membro{" +
                "nId=" + nId +
                ", sNome='" + sNome + '\'' +
                ", sFuncao='" + sFuncao + '\'' +
                ", nEnderecoId=" + (oEndereco != null ? oEndereco.getNId() : "null") +
                '}';
    }
}
