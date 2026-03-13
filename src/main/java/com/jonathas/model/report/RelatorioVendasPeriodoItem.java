package com.jonathas.model.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RelatorioVendasPeriodoItem {

    private Long vendaId;
    private LocalDate dataVenda;
    private String clienteNome;
    private String status;
    private BigDecimal valorTotal;

    public RelatorioVendasPeriodoItem() {
    }

    public RelatorioVendasPeriodoItem(Long vendaId, LocalDate dataVenda, String clienteNome, String status, BigDecimal valorTotal) {
        this.vendaId = vendaId;
        this.dataVenda = dataVenda;
        this.clienteNome = clienteNome;
        this.status = status;
        this.valorTotal = valorTotal;
    }

    public Long getVendaId() {
        return vendaId;
    }

    public void setVendaId(Long vendaId) {
        this.vendaId = vendaId;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
