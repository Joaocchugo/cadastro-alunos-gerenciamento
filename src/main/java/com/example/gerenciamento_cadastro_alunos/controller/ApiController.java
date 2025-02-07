package com.example.gerenciamento_cadastro_alunos.controller;


import com.example.gerenciamento_cadastro_alunos.models.Aluno;
import com.example.gerenciamento_cadastro_alunos.models.XMLUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/alunos")
    public List<Aluno> getAlunos() throws Exception {
        return XMLUtil.getAlunos();
    }

    @PostMapping("/cadastrar")
    public String cadastrarAluno(@RequestParam String nome, @RequestParam int idade) throws Exception {
        XMLUtil.salvarAluno(new Aluno(nome, idade, List.of()));
        return "Aluno cadastrado!";
    }
}
