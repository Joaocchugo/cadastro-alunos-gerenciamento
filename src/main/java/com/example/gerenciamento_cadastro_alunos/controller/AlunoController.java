package com.example.gerenciamento_cadastro_alunos.controller;

import com.example.gerenciamento_cadastro_alunos.models.Aluno;
import com.example.gerenciamento_cadastro_alunos.models.Util;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/aluno")
public class AlunoController {

    /** Retorna um um objeto */
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarAluno(@RequestParam String nome, @RequestParam int idade) throws Exception {
        List<Aluno> alunos = Util.getAlunos();

        if (alunos.stream().anyMatch(a -> a.getNome().equalsIgnoreCase(nome))) {
            return ResponseEntity.badRequest().body("Já existe um " + nome + " cadastrado");
        }

        Util.salvarAluno(new Aluno(nome, idade, List.of()));
        return ResponseEntity.ok("Cadastrado");
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAluno(@RequestParam String nome) throws Exception {
        List<Aluno> alunos = Util.getAlunos();
        Optional<Aluno> alunoEncontrado = alunos.stream()
                .filter(a -> a.getNome().equalsIgnoreCase(nome))
                .findFirst();

        if (alunoEncontrado.isPresent()) {
            return ResponseEntity.ok(alunoEncontrado.get());
        } else {
            return ResponseEntity.badRequest().body("Não encontrado");
        }
    }

    @PostMapping("/adicionarNota")
    public ResponseEntity<String> adicionarNota(@RequestParam String nome, @RequestParam String notas) throws Exception {
        List<Double> novaNotas = List.of(notas.split(",")).stream().map(Double::parseDouble).collect(Collectors.toList());
        List<Aluno> alunos = Util.getAlunos();

        for (Aluno aluno : alunos) {
            if (aluno.getNome().equalsIgnoreCase(nome)) {
                aluno.getNotas().addAll(novaNotas);
                Util.atualizarAlunos(alunos);
                return ResponseEntity.ok("Adicionadas");
            }
        }
        return ResponseEntity.badRequest().body("Não encontrado");
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<String> deletarAluno(@RequestParam String nome) throws Exception {
        List<Aluno> alunos = Util.getAlunos();
        List<Aluno> filtrados = alunos.stream().filter(a -> !a.getNome().equalsIgnoreCase(nome)).collect(Collectors.toList());

        if (alunos.size() == filtrados.size()) {
            return ResponseEntity.badRequest().body("Não encontrado");
        }

        Util.atualizarAlunos(filtrados);
        return ResponseEntity.ok("Removido");
    }

    @GetMapping("/listar")
    public List<Aluno> listarAlunos() throws Exception {
        return Util.getAlunos();
    }
}