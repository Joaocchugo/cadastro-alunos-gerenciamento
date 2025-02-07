package com.example.gerenciamento_cadastro_alunos.controller;

import com.example.gerenciamento_cadastro_alunos.models.Aluno;
import com.example.gerenciamento_cadastro_alunos.models.XMLUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/aluno")
public class AlunoController {

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarAluno(@RequestParam String nome, @RequestParam int idade) {
        try {
            List<Aluno> alunos = XMLUtil.getAlunos();

            // Verifica se já existe um aluno com o mesmo nome
            if (alunos.stream().anyMatch(a -> a.getNome().equalsIgnoreCase(nome))) {
                return ResponseEntity.badRequest().body("Aluno já cadastrado!");
            }

            Aluno novoAluno = new Aluno(nome, idade, List.of());
            XMLUtil.salvarAluno(novoAluno);
            return ResponseEntity.ok("Aluno cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao cadastrar aluno.");
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarAluno(@RequestParam String nome) {
        try {
            List<Aluno> alunos = XMLUtil.getAlunos();
            Optional<Aluno> alunoEncontrado = alunos.stream()
                    .filter(a -> a.getNome().equalsIgnoreCase(nome))
                    .findFirst();

            if (alunoEncontrado.isPresent()) {
                return ResponseEntity.ok(alunoEncontrado.get());
            } else {
                return ResponseEntity.badRequest().body("Aluno não encontrado.");
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar aluno.");
        }
    }

    @PostMapping("/adicionarNota")
    public ResponseEntity<String> adicionarNota(@RequestParam String nome, @RequestParam double nota) {
        try {
            List<Aluno> alunos = XMLUtil.getAlunos();
            for (Aluno aluno : alunos) {
                if (aluno.getNome().equalsIgnoreCase(nome)) {
                    aluno.getNotas().add(nota);
                    XMLUtil.atualizarAlunos(alunos);
                    return ResponseEntity.ok("Nota adicionada com sucesso!");
                }
            }
            return ResponseEntity.badRequest().body("Aluno não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao adicionar nota.");
        }
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<String> deletarAluno(@RequestParam String nome) {
        try {
            List<Aluno> alunos = XMLUtil.getAlunos();
            List<Aluno> alunosFiltrados = alunos.stream()
                    .filter(a -> !a.getNome().equalsIgnoreCase(nome))
                    .collect(Collectors.toList());

            if (alunos.size() == alunosFiltrados.size()) {
                return ResponseEntity.badRequest().body("Aluno não encontrado.");
            }

            XMLUtil.atualizarAlunos(alunosFiltrados);
            return ResponseEntity.ok("Aluno deletado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar aluno.");
        }
    }
}
