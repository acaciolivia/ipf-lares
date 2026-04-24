package com.ipfnoslares.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que mapeia a resposta da API ViaCEP.
 * Documentação: https://viacep.com.br
 *
 * Exemplo de resposta:
 * {
 *   "cep": "01001-000",
 *   "logradouro": "Praça da Sé",
 *   "complemento": "lado ímpar",
 *   "bairro": "Sé",
 *   "localidade": "São Paulo",
 *   "uf": "SP",
 *   "erro": true   <- presente apenas quando CEP não encontrado
 * }
 */
public class ViaCepResponseDTO {

    @JsonProperty("cep")
    private String sCep;

    @JsonProperty("logradouro")
    private String sLogradouro;

    @JsonProperty("complemento")
    private String sComplemento;

    @JsonProperty("bairro")
    private String sBairro;

    /** Cidade (campo "localidade" na resposta do ViaCEP) */
    @JsonProperty("localidade")
    private String sCidade;

    /** Estado (campo "uf" na resposta do ViaCEP) */
    @JsonProperty("uf")
    private String sEstado;

    /** Presente e igual a true quando o CEP não é encontrado */
    @JsonProperty("erro")
    private Boolean bErro;

    public ViaCepResponseDTO() {}

    // =========================================================
    // Getters e Setters
    // =========================================================

    public String getSCep()                       { return sCep; }
    public void setSCep(String sCep)              { this.sCep = sCep; }

    public String getSLogradouro()                { return sLogradouro; }
    public void setSLogradouro(String sLogradouro){ this.sLogradouro = sLogradouro; }

    public String getSComplemento()               { return sComplemento; }
    public void setSComplemento(String sComp)     { this.sComplemento = sComp; }

    public String getSBairro()                    { return sBairro; }
    public void setSBairro(String sBairro)        { this.sBairro = sBairro; }

    public String getSCidade()                    { return sCidade; }
    public void setSCidade(String sCidade)        { this.sCidade = sCidade; }

    public String getSEstado()                    { return sEstado; }
    public void setSEstado(String sEstado)        { this.sEstado = sEstado; }

    public Boolean getBErro()                     { return bErro; }
    public void setBErro(Boolean bErro)           { this.bErro = bErro; }

    public boolean isCepInvalido() {
        return Boolean.TRUE.equals(bErro);
    }
}
