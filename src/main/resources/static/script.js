document.addEventListener('DOMContentLoaded', () => {
    carregarAlunos();

    document.getElementById('cadastroForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const nome = document.getElementById('nome').value;
        const idade = document.getElementById('idade').value;

        await fetch('/app/aluno/cadastrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ nome, idade })
        });

        carregarAlunos();
    });

    document.getElementById('buscarAlunoBtn').addEventListener('click', async () => {
        const nome = document.getElementById('buscarNome').value;
        const response = await fetch(`/app/aluno/buscar?nome=${encodeURIComponent(nome)}`);
        const resultado = document.getElementById('resultadoBusca');

        if (response.ok) {
            const aluno = await response.json();
            resultado.innerHTML = `<p>Nome: ${aluno.nome} <br> Idade: ${aluno.idade} <br> Média: ${aluno.media.toFixed(2)}</p>`;
        } else {
            resultado.innerHTML = `<p>Não encontrado, tente novamente</p>`;
        }
    });

    document.getElementById('deletarAlunoBtn').addEventListener('click', async () => {
        const nome = document.getElementById('deletarNome').value;
        const response = await fetch(`/app/aluno/deletar?nome=${encodeURIComponent(nome)}`, {
            method: 'DELETE'
        });

        const resultado = document.getElementById('resultadoDelecao');
        if (response.ok) {
            resultado.innerHTML = `<p>Removido</p>`;
            carregarAlunos();
        } else {
            resultado.innerHTML = `<p>Erro ao remover</p>`;
        }
    });

    async function carregarAlunos() {
        const response = await fetch('/app/aluno/listar');
        const alunos = await response.json();
        const lista = document.getElementById('listaAlunos');
        lista.innerHTML = '';

        alunos.forEach(aluno => {
            const li = document.createElement('li');
            li.innerHTML = `${aluno.nome} - ${aluno.idade} anos`;

            const botaoNota = document.createElement('button');
            botaoNota.textContent = "Adicionar Nota";
            botaoNota.addEventListener('click', () => abrirFormularioNotas(aluno.nome));

            li.appendChild(botaoNota);
            lista.appendChild(li);
        });
    }

    function abrirFormularioNotas(nome) {
        document.getElementById('formNotas').style.display = 'block';
        document.getElementById('alunoSelecionado').innerText = `Aluno: ${nome}`;
        document.getElementById('salvarNotasBtn').onclick = async () => {
            const notas = [
                document.getElementById('nota1').value,
                document.getElementById('nota2').value,
                document.getElementById('nota3').value,
                document.getElementById('nota4').value
            ];

            await fetch(`/app/aluno/adicionarNota?nome=${encodeURIComponent(nome)}&notas=${notas.join(',')}`, {
                method: 'POST'
            });

            document.getElementById('formNotas').style.display = 'none';
            carregarAlunos();
        };

        document.getElementById('cancelarNotasBtn').onclick = () => {
            document.getElementById('formNotas').style.display = 'none';
        };
    }
});
