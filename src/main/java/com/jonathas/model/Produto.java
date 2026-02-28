package com.jonathas.model;

import java.math.BigDecimal;

public class Produto {

    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private Integer quantidade;
    private BigDecimal precoPadrao;
    private BigDecimal custoUnitario;

    public Produto() {
    }

    public Produto(Long id, String nome, String descricao, Boolean ativo, Integer quantidade, BigDecimal precoPadrao, BigDecimal custoUnitario) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoPadrao = precoPadrao;
        this.custoUnitario = custoUnitario;
    }

    public Produto(String nome, String descricao, Integer quantidade, BigDecimal precoPadrao, BigDecimal custoUnitario) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true;
        this.quantidade = quantidade;
        this.precoPadrao = precoPadrao;
        this.custoUnitario = custoUnitario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoPadrao() {
        return precoPadrao;
    }

    public void setPrecoPadrao(BigDecimal precoPadrao) {
        this.precoPadrao = precoPadrao;
    }

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ativo=" + ativo + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }


}
