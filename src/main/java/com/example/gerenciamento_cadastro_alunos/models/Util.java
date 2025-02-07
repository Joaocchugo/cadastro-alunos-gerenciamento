package com.example.gerenciamento_cadastro_alunos.models;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Util {
    private static final String CAMINHO_XML = "src/main/resources/WEB-INF/alunos.xml";

    public static List<Aluno> getAlunos() throws Exception {
        List<Aluno> alunos = new ArrayList<>();
        File file = new File(CAMINHO_XML);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);

        NodeList lista = document.getElementsByTagName("aluno");

        for (int i = 0; i < lista.getLength(); i++) {
            Element alunoelemento = (Element) lista.item(i);
            String nome = alunoelemento.getElementsByTagName("nome").item(0).getTextContent();
            int idade = Integer.parseInt(alunoelemento.getElementsByTagName("idade").item(0).getTextContent());

            NodeList notasnodes = alunoelemento.getElementsByTagName("nota");
            List<Double> notas = new ArrayList<>();
            for (int j = 0; j < notasnodes.getLength(); j++) {
                notas.add(Double.parseDouble(notasnodes.item(j).getTextContent()));
            }

            alunos.add(new Aluno(nome, idade, notas));
        }
        return alunos;
    }

    public static void salvarAluno(Aluno aluno) throws Exception {
        File file = new File(CAMINHO_XML);
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        Element elemento = document.getDocumentElement();

        Element alunoelemento = document.createElement("aluno");
        Element nomelemento = document.createElement("nome");
        nomelemento.setTextContent(aluno.getNome());

        Element idadelemento = document.createElement("idade");
        idadelemento.setTextContent(String.valueOf(aluno.getIdade()));

        Element notasElement = document.createElement("notas");
        for (Double nota : aluno.getNotas()) {
            Element notaelement = document.createElement("nota");
            notaelement.setTextContent(String.valueOf(nota));
            notasElement.appendChild(notaelement);
        }

        alunoelemento.appendChild(nomelemento);
        alunoelemento.appendChild(idadelemento);
        alunoelemento.appendChild(notasElement);
        elemento.appendChild(alunoelemento);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(file));
    }

    public static void atualizarAlunos(List<Aluno> alunos) throws Exception {
        File file = new File(CAMINHO_XML);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element elemento = document.createElement("alunos");
        document.appendChild(elemento);

        for (Aluno aluno : alunos) {
            Element alunoelemento = document.createElement("aluno");

            Element nomelemento = document.createElement("nome");
            nomelemento.setTextContent(aluno.getNome());

            Element idadelemento = document.createElement("idade");
            idadelemento.setTextContent(String.valueOf(aluno.getIdade()));

            Element notasElement = document.createElement("notas");
            for (Double nota : aluno.getNotas()) {
                Element notaelement = document.createElement("nota");
                notaelement.setTextContent(String.valueOf(nota));
                notasElement.appendChild(notaelement);
            }

            alunoelemento.appendChild(nomelemento);
            alunoelemento.appendChild(idadelemento);
            alunoelemento.appendChild(notasElement);
            elemento.appendChild(alunoelemento);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult(file));
    }

}
