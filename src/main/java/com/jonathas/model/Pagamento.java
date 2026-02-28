package com.jonathas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento {

    private Long id;
    private Long vendaId;
    private LocalDateTime dataPagamento;
    private BigDecimal valorPago;

    public Pagamento() {
    }

    public Pagamento(Long vendaId, LocalDateTime dataPagamento, BigDecimal valorPago) {
        this.vendaId = vendaId;
        this.dataPagamento = dataPagamento;
        this.valorPago = valorPago;
    }

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

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }
}
