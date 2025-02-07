document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('cadastroForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const nome = document.getElementById('nome').value;
        const idade = document.getElementById('idade').value;

        await fetch('/api/cadastrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: new URLSearchParams({ nome, idade })
        });

        carregarAlunos();
    });

    async function carregarAlunos() {
        const response = await fetch('/api/alunos');
        const alunos = await response.json();
        const lista = document.getElementById('listaAlunos');
        lista.innerHTML = '';

        alunos.forEach(aluno => {
            const li = document.createElement('li');
            li.textContent = `${aluno.nome} - ${aluno.idade} anos`;
            lista.appendChild(li);
        });
    }

    carregarAlunos();
});
