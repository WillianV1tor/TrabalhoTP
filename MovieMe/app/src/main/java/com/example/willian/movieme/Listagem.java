package com.example.willian.movieme;

/**
 * Created by Willian on 30/03/2018.
 */

public class Listagem {
    private String nome;
    private String genero;
    private String diretor;
    private long faixa;
    private String ano;
    private long id;
    private static long idA = 0;

    public Listagem(String nome, String genero, String diretor, long faixa, String ano) {
        this.nome = nome;
        this.genero = genero;
        this.diretor = diretor;
        this.faixa = faixa;
        this.ano = ano;
        idA++;
        this.id = idA;
    }

    //construtor padrão
    public Listagem() {
        this(null, null, null, 0, null);
    }

    //getters
    public String getNome() {
        return nome;
    }

    public String getGenero() {
        return genero;
    }

    public String getDiretor() {
        return diretor;
    }

    public String getAno() {
        return ano;
    }

    public long getId(){ return this.id; }

    public long getFaixa() {
        return faixa;
    }

    //setters
    public void setAno(String ano) {
        this.ano = ano;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public void setFaixa(long faixa) {
        this.faixa = faixa;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(long id){this.id = id;}
}
