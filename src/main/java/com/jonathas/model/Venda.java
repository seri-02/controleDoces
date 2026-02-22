package com.jonathas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venda {

    private Long id;
    private Produto produto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private LocalDateTime dataVenda;

    public Venda() {
    }

    public Venda(Long id, Produto produto, Integer quantidade, BigDecimal valorUnitario, LocalDateTime dataVenda) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.dataVenda = dataVenda;
    }

    public Venda(Produto produto, Integer quantidade, BigDecimal valorUnitario, LocalDateTime dataVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.dataVenda = dataVenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", produto='" + produto + '\'' +
                ", quantidade='" + quantidade + '\'' +
                ", valorTotal='" + valorUnitario + '\'' +
                ", dataVenda='" + dataVenda +
                '}';
    }

}
