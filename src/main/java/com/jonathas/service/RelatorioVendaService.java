package com.jonathas.service;

import com.jonathas.model.report.RelatorioVendasPeriodoItem;
import com.jonathas.repository.report.RelatorioVendaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RelatorioVendaService {

    private final RelatorioVendaRepository relatorioVendaRepository;

    public RelatorioVendaService() {
        this.relatorioVendaRepository = new RelatorioVendaRepository();
    }

    public List<RelatorioVendasPeriodoItem> buscarVendasPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        validarPeriodo(dataInicial,dataFinal);
        return relatorioVendaRepository.buscarVendasPorPeriodo(dataInicial,dataFinal);
    }

    public BigDecimal calcularTotalVendidoNoPeriodo(List<RelatorioVendasPeriodoItem> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (RelatorioVendasPeriodoItem item : itens) {
            total = total.add(item.getValorTotal());
        }
        return total;
    }

    public int calcularQuantidadeVendas(List<RelatorioVendasPeriodoItem> itens) {
        return itens.size();
    }

    private void validarPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null) {
            throw new IllegalArgumentException("A data inicial não pode ser maior que a data final");
        }
    }

}
