package com.jonathas;

import com.jonathas.model.Emitente;
import com.jonathas.repository.EmitenteRepository;

public class Main {
    public static void main(String[] args) {

        EmitenteRepository emitenteRepository = new EmitenteRepository();

        Emitente emitente = new Emitente();
        emitente.setNome("Fornecedor Teste");
        emitente.setDocumento("12345678900");
        emitente.setTipo("FORNECEDOR");
        emitente.setAtivo(true);

        emitenteRepository.salvar(emitente);

        System.out.println("Emitente salvo com ID: " + emitente.getId());

        emitenteRepository.listarTodos().forEach(System.out::println);

    }
}
