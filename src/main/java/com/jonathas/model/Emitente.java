package com.jonathas.model;

public class Emitente {

    private Long id;
    private String nome;
    private String documento;
    private String tipo;
    private Boolean ativo;

    public Emitente() {
    }

    public Emitente(Long id, String nome, String documento, String tipo, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    public Emitente(String nome, String documento, String tipo) {
        this.nome = nome;
        this.documento = documento;
        this.tipo = tipo;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTipo() {
        return tipo;
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

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Emitente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + documento + '\'' +
                ", tipo='" + tipo + '\'' +
                ", ativo=" + ativo +
                '}';
    }

}
