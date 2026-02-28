package com.jonathas.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {

    private Long id;
    private LocalDateTime dataVenda;
    private List<ItemVenda> itens = new ArrayList<>();

    public Venda() {
        this.dataVenda = LocalDateTime.now();
    }

    public Venda(Long id, LocalDateTime dataVenda) {
        this.id = id;
        this.dataVenda = dataVenda;
    }

    // create sale in console
    public Venda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    // Usefull rules
    public void adicionarItem(ItemVenda item) {
        if (item == null) return;
        this.itens.add(item);
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            if (item != null) {
                total = total.add(item.getSubtotal());
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // Getter n Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", dataVenda=" + dataVenda +
                ", itens=" + itens +
                ", total=" + getTotal() +
                '}';
    }
}