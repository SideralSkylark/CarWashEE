// Função para carregar produtos ao entrar na página
window.addEventListener("DOMContentLoaded", function() {
    fetch("obterProdutos")
        .then(response => response.json())
        .then(produtos => {
            const produtoTable = document.getElementById("produto_body");
            produtoTable.innerHTML = "";  // Limpa a tabela antes de preencher

            produtos.forEach(produto => {
                const newRow = produtoTable.insertRow();

                const tipoProdutoCell = newRow.insertCell(0);
                const quantiaCell = newRow.insertCell(1);
                const actionsCell = newRow.insertCell(2);

                tipoProdutoCell.textContent = produto.tipoProduto;
                quantiaCell.textContent = produto.quantia;

                actionsCell.innerHTML = `
                    <button class="edit-btn" onclick="abrirModalEditar('${produto.tipoProduto}', ${produto.quantia})">Editar</button>
                    <button class="delete-btn" onclick="deleteProduto('${produto.tipoProduto}')">Remover</button>
                `;
            });
        })
        .catch(error => console.error('Erro ao carregar produtos:', error));
});

// Função para abrir o modal de adição de produto
document.getElementById("newAppointmentBtn").addEventListener("click", function() {
    abrirModalAdicionar();
});

// Função para abrir o modal de adicionar produto
function abrirModalAdicionar() {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "block";  // Exibe o modal
}

// Função para abrir o modal de editar produto
function abrirModalEditar(tipoProduto, quantia) {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "block";  // Exibe o modal

    // Preencher os campos do formulário com os dados do produto
    document.getElementById("descricao").value = tipoProduto;
    document.getElementById("quantidade").value = quantia;

    // Alterar o comportamento do formulário para edição
    const form = document.getElementById("appointmentForm");
    form.onsubmit = function(event) {
        event.preventDefault();
        editarProduto(tipoProduto); // Chama a função de edição
    };
}

// Função para adicionar um novo produto
function adicionarProduto() {
    const tipoProduto = document.getElementById("descricao").value;
    const quantia = document.getElementById("quantidade").value;

    fetch("adicionarProduto", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ tipoProduto, quantia })
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            window.location.reload();
        })
        .catch(error => console.error('Erro ao adicionar produto:', error));

    fecharModal(); // Fechar modal após adicionar
}

// Função para editar um produto
function editarProduto(tipoProdutoAntigo) {
    const tipoProduto = document.getElementById("descricao").value;
    const novaQuantia = document.getElementById("quantidade").value;

    fetch("editarProduto", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ tipoProduto: tipoProdutoAntigo, novaQuantia })
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            window.location.reload();
        })
        .catch(error => console.error('Erro ao editar produto:', error));

    fecharModal(); // Fechar modal após editar
}

// Função para remover um produto
function deleteProduto(tipoProduto) {
    if (confirm("Você tem certeza que deseja remover o produto " + tipoProduto + "?")) {
        fetch("removerProduto", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ tipoProduto })
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                window.location.reload();
            })
            .catch(error => console.error('Erro ao remover produto:', error));
    }
}

// Função para fechar o modal
document.getElementById("closeModal").addEventListener("click", fecharModal);

function fecharModal() {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "none";  // Esconde o modal
    // Limpa o formulário
    document.getElementById("appointmentForm").reset();
    const form = document.getElementById("appointmentForm");
    form.onsubmit = function(event) {
        event.preventDefault(); // Não faz nada se for uma nova adição
        adicionarProduto();
    };
}
