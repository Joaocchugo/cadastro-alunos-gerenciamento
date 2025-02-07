package com.example.gerenciamento_cadastro_alunos.models;


import java.util.List;

public class Aluno {
    private String nome;
    private int idade;
    private List<Double> notas;

    public Aluno(String nome, int idade, List<Double> notas) {
        this.nome = nome;
        this.idade = idade;
        this.notas = notas;
    }

    public String getNome() {
        return nome;
    }

    public int getIdade() {
        return idade;
    }

    public List<Double> getNotas() {
        return notas;
    }

    public double getMedia() {
        return notas.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
