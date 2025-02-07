package com.example.gerenciamento_cadastro_alunos.models;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {
    private static final String XML_FILE_PATH = "src/main/resources/WEB-INF/alunos.xml";

    public static List<Aluno> getAlunos() throws Exception {
        List<Aluno> alunos = new ArrayList<>();
        File file = new File(XML_FILE_PATH);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

        NodeList alunoNodes = document.getElementsByTagName("aluno");

        for (int i = 0; i < alunoNodes.getLength(); i++) {
            Element alunoElement = (Element) alunoNodes.item(i);
            String nome = alunoElement.getElementsByTagName("nome").item(0).getTextContent();
            int idade = Integer.parseInt(alunoElement.getElementsByTagName("idade").item(0).getTextContent());

            NodeList notasNodes = alunoElement.getElementsByTagName("nota");
            List<Double> notas = new ArrayList<>();
            for (int j = 0; j < notasNodes.getLength(); j++) {
                notas.add(Double.parseDouble(notasNodes.item(j).getTextContent()));
            }

            alunos.add(new Aluno(nome, idade, notas));
        }
        return alunos;
    }

    public static void salvarAluno(Aluno aluno) throws Exception {
        File file = new File(XML_FILE_PATH);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        Element root = document.getDocumentElement();

        Element alunoElement = document.createElement("aluno");
        Element nomeElement = document.createElement("nome");
        nomeElement.setTextContent(aluno.getNome());

        Element idadeElement = document.createElement("idade");
        idadeElement.setTextContent(String.valueOf(aluno.getIdade()));

        Element notasElement = document.createElement("notas");
        for (Double nota : aluno.getNotas()) {
            Element notaElement = document.createElement("nota");
            notaElement.setTextContent(String.valueOf(nota));
            notasElement.appendChild(notaElement);
        }

        alunoElement.appendChild(nomeElement);
        alunoElement.appendChild(idadeElement);
        alunoElement.appendChild(notasElement);
        root.appendChild(alunoElement);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(file));
    }

    public static void atualizarAlunos(List<Aluno> alunos) throws Exception {
        File file = new File(XML_FILE_PATH);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element root = document.createElement("alunos");
        document.appendChild(root);

        for (Aluno aluno : alunos) {
            Element alunoElement = document.createElement("aluno");

            Element nomeElement = document.createElement("nome");
            nomeElement.setTextContent(aluno.getNome());

            Element idadeElement = document.createElement("idade");
            idadeElement.setTextContent(String.valueOf(aluno.getIdade()));

            Element notasElement = document.createElement("notas");
            for (Double nota : aluno.getNotas()) {
                Element notaElement = document.createElement("nota");
                notaElement.setTextContent(String.valueOf(nota));
                notasElement.appendChild(notaElement);
            }

            alunoElement.appendChild(nomeElement);
            alunoElement.appendChild(idadeElement);
            alunoElement.appendChild(notasElement);
            root.appendChild(alunoElement);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(file));
    }

}
