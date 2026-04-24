package com.ipfnoslares.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um endereço cadastrado no sistema.
 * Um endereço pode ter vários membros associados (relacionamento OneToMany).
 * Utiliza o padrão Builder para construção de instâncias.
 *
 * Convenção de variáveis (Hungarian notation):
 *   n  = numérico (Long, Integer)
 *   s  = String
 *   d  = double/Double
 *   l  = List
 *   dt = LocalDateTime
 */
@Entity
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long nId;

    @Column(name = "cep", nullable = false, length = 10)
    private String sCep;

    @Column(name = "logradouro", nullable = false, length = 255)
    private String sLogradouro;

    @Column(name = "numero", length = 20)
    private String sNumero;

    @Column(name = "complemento", length = 255)
    private String sComplemento;

    @Column(name = "bairro", length = 100)
    private String sBairro;

    @Column(name = "cidade", nullable = false, length = 100)
    private String sCidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String sEstado;

    @Column(name = "latitude")
    private Double dLatitude;

    @Column(name = "longitude")
    private Double dLongitude;

    @OneToMany(mappedBy = "oEndereco", fetch = FetchType.LAZY)
    private List<Membro> lMembros = new ArrayList<>();

    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dtCriacao;

    @Column(name = "dt_atualizacao")
    private LocalDateTime dtAtualizacao;

    // Construtor protegido exigido pelo JPA
    protected Endereco() {}

    // Construtor privado — uso exclusivo do Builder
    private Endereco(Builder oBuilder) {
        this.nId            = oBuilder.nId;
        this.sCep           = oBuilder.sCep;
        this.sLogradouro    = oBuilder.sLogradouro;
        this.sNumero        = oBuilder.sNumero;
        this.sComplemento   = oBuilder.sComplemento;
        this.sBairro        = oBuilder.sBairro;
        this.sCidade        = oBuilder.sCidade;
        this.sEstado        = oBuilder.sEstado;
        this.dLatitude      = oBuilder.dLatitude;
        this.dLongitude     = oBuilder.dLongitude;
        this.dtCriacao      = oBuilder.dtCriacao;
        this.dtAtualizacao  = oBuilder.dtAtualizacao;
    }

    // =========================================================
    // Getters
    // =========================================================

    public Long getNId()                       { return nId; }
    public String getSCep()                    { return sCep; }
    public String getSLogradouro()             { return sLogradouro; }
    public String getSNumero()                 { return sNumero; }
    public String getSComplemento()            { return sComplemento; }
    public String getSBairro()                 { return sBairro; }
    public String getSCidade()                 { return sCidade; }
    public String getSEstado()                 { return sEstado; }
    public Double getDLatitude()               { return dLatitude; }
    public Double getDLongitude()              { return dLongitude; }
    public List<Membro> getLMembros()          { return lMembros; }
    public LocalDateTime getDtCriacao()        { return dtCriacao; }
    public LocalDateTime getDtAtualizacao()    { return dtAtualizacao; }

    // =========================================================
    // Pré-persistência
    // =========================================================

    @PrePersist
    private void prePersist() {
        this.dtCriacao     = LocalDateTime.now();
        this.dtAtualizacao = LocalDateTime.now();
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
        private String sCep;
        private String sLogradouro;
        private String sNumero;
        private String sComplemento;
        private String sBairro;
        private String sCidade;
        private String sEstado;
        private Double dLatitude;
        private Double dLongitude;
        private LocalDateTime dtCriacao;
        private LocalDateTime dtAtualizacao;

        private Builder() {}

        public Builder nId(Long nId) {
            this.nId = nId;
            return this;
        }

        public Builder sCep(String sCep) {
            this.sCep = sCep;
            return this;
        }

        public Builder sLogradouro(String sLogradouro) {
            this.sLogradouro = sLogradouro;
            return this;
        }

        public Builder sNumero(String sNumero) {
            this.sNumero = sNumero;
            return this;
        }

        public Builder sComplemento(String sComplemento) {
            this.sComplemento = sComplemento;
            return this;
        }

        public Builder sBairro(String sBairro) {
            this.sBairro = sBairro;
            return this;
        }

        public Builder sCidade(String sCidade) {
            this.sCidade = sCidade;
            return this;
        }

        public Builder sEstado(String sEstado) {
            this.sEstado = sEstado;
            return this;
        }

        public Builder dLatitude(Double dLatitude) {
            this.dLatitude = dLatitude;
            return this;
        }

        public Builder dLongitude(Double dLongitude) {
            this.dLongitude = dLongitude;
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

        public Endereco build() {
            return new Endereco(this);
        }
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "nId=" + nId +
                ", sCep='" + sCep + '\'' +
                ", sLogradouro='" + sLogradouro + '\'' +
                ", sNumero='" + sNumero + '\'' +
                ", sCidade='" + sCidade + '\'' +
                ", sEstado='" + sEstado + '\'' +
                '}';
    }
}
