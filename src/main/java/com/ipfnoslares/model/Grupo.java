package com.ipfnoslares.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um grupo ou célula da igreja.
 *
 * Regras de negócio:
 *  - RN-001: Um membro pode estar vinculado a no máximo UM grupo (FK em Membro).
 *  - RN-002: Cada grupo possui EXATAMENTE UM líder (NOT NULL).
 *
 * Convenção Hungarian:
 *   n = numérico, s = string, b = boolean, o = objeto, l = lista, dt = data
 */
@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long nId;

    @Column(name = "nome", nullable = false, length = 120)
    private String sNome;

    @Column(name = "descricao", length = 500)
    private String sDescricao;

    /** Líder do grupo — obrigatório (RN-002). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lider_id", nullable = false)
    private Membro oLider;

    /** Membros vinculados ao grupo. Mapeado pelo lado inverso (Membro.oGrupo). */
    @OneToMany(mappedBy = "oGrupo", fetch = FetchType.LAZY)
    private List<Membro> lMembros = new ArrayList<>();

    @Column(name = "dt_criacao", updatable = false)
    private LocalDateTime dtCriacao;

    @Column(name = "dt_atualizacao")
    private LocalDateTime dtAtualizacao;

    // ─────────────────────────────────────────────────────────
    // Construtores
    // ─────────────────────────────────────────────────────────
    protected Grupo() {}

    private Grupo(Builder oBuilder) {
        this.nId           = oBuilder.nId;
        this.sNome         = oBuilder.sNome;
        this.sDescricao    = oBuilder.sDescricao;
        this.oLider        = oBuilder.oLider;
        this.dtCriacao     = oBuilder.dtCriacao;
        this.dtAtualizacao = oBuilder.dtAtualizacao;
    }

    // ─────────────────────────────────────────────────────────
    // Pré-persistência
    // ─────────────────────────────────────────────────────────
    @PrePersist
    private void prePersist() {
        this.dtCriacao     = LocalDateTime.now();
        this.dtAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.dtAtualizacao = LocalDateTime.now();
    }

    // ─────────────────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────────────────
    public Long getNId()                    { return nId; }
    public String getSNome()                { return sNome; }
    public String getSDescricao()           { return sDescricao; }
    public Membro getOLider()               { return oLider; }
    public List<Membro> getLMembros()       { return lMembros; }
    public LocalDateTime getDtCriacao()     { return dtCriacao; }
    public LocalDateTime getDtAtualizacao() { return dtAtualizacao; }

    // ─────────────────────────────────────────────────────────
    // Builder
    // ─────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long nId;
        private String sNome;
        private String sDescricao;
        private Membro oLider;
        private LocalDateTime dtCriacao;
        private LocalDateTime dtAtualizacao;

        public Builder nId(Long n)                    { this.nId = n; return this; }
        public Builder sNome(String s)                { this.sNome = s; return this; }
        public Builder sDescricao(String s)           { this.sDescricao = s; return this; }
        public Builder oLider(Membro o)               { this.oLider = o; return this; }
        public Builder dtCriacao(LocalDateTime dt)    { this.dtCriacao = dt; return this; }
        public Builder dtAtualizacao(LocalDateTime dt){ this.dtAtualizacao = dt; return this; }

        public Grupo build() { return new Grupo(this); }
    }
}
