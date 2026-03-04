package com.jonathas.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ItemVenda {

    private Long id;
    private Long vendaId;
    private Long produtoId;
    private int quantidade;
    private BigDecimal valorUnitario;
    private String produtoNome;

    public ItemVenda() {
    }

    public ItemVenda(Long produtoId, int quantidade, BigDecimal valorUnitario) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    // Subtotal = quant * valorUni
    public BigDecimal getSubtotal() {
        if (valorUnitario == null) {
            return BigDecimal.ZERO;
        }
        return valorUnitario
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Getters n Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    // Equals/hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemVenda)) return false;
        ItemVenda itemVenda = (ItemVenda) o;
        return Objects.equals(id, itemVenda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ItemVenda{" +
                "id=" + id +
                ", vendaId=" + vendaId +
                ", produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
