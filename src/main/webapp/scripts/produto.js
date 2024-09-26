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

                // Criação dos botões Adicionar e Remover para cada produto
                actionsCell.innerHTML = `
                    <button class="edit-btn" onclick="abrirModalEditar('${produto.tipoProduto}', '${produto.quantia}')">Editar</button>
                    <button class="delete-btn" onclick="abrirModalRemover('${produto.tipoProduto}')">Remover</button>
                `;
            });
        })
        .catch(error => console.error('Erro ao carregar produtos:', error));
});

// Função para abrir o modal de confirmação de remoção
function abrirModalRemover(tipoProduto) {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "block";  // Exibe o modal

    // Preencher o campo do formulário com o nome do produto a ser removido
    document.getElementById("descricao").value = tipoProduto;
    document.getElementById("quantidade").value = "1";  // Define a quantidade a ser removida como 1 por padrão

    // Alterar o comportamento do formulário para remoção
    const form = document.getElementById("appointmentForm");
    form.onsubmit = function(event) {
        event.preventDefault();
        removerProduto(tipoProduto);  // Chama a função para remover o produto
    };
}

// Função para remover um produto
function removerProduto(tipoProduto) {
    const quantidade = document.getElementById("quantidade").value;

    fetch("atualizarProduto", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ tipoProduto, valor: -parseInt(quantidade) })  // Envia o tipo do produto e diminui a quantidade
    })
        .then(response => response.json())
        .then(data => {
            console.log("Produto removido com sucesso", data);
            window.location.reload();  // Recarrega a página após a remoção
        })
        .catch(error => console.error('Erro ao remover produto:', error));

    fecharModal(); // Fechar modal após remover
}

// Função para abrir o modal de adição de produto
document.getElementById("newAppointmentBtn").addEventListener("click", function() {
    abrirModalAdicionar();
});

function abrirModalAdicionar() {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "block";  // Exibe o modal

    // Limpar campos do formulário
    document.getElementById("descricao").value = "";
    document.getElementById("quantidade").value = "";

    // Alterar o comportamento do formulário para adição
    const form = document.getElementById("appointmentForm");
    form.onsubmit = function(event) {
        event.preventDefault();

        const tipoProduto = document.getElementById("descricao").value;
        const quantidade = document.getElementById("quantidade").value;

        adicionarProduto(tipoProduto, quantidade);  // Chama a função para adicionar produto
    };
}

// Função para abrir o modal de editar produto
function abrirModalEditar(tipoProduto, quantia) {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "block";

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

// Função para adicionar um novo produto ao banco de dados
function adicionarProduto(tipoProduto, quantidade) {
    console.log("tipo: " + tipoProduto + " , quantidade: " + quantidade);
    fetch("atualizarProduto", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ tipoProduto, valor: parseInt(quantidade) })  // Envia o tipo do produto e a quantidade para adicionar
    })
        .then(response => response.json())
        .then(data => {
            console.log("Produto adicionado com sucesso", data);
            window.location.reload();
        })
        .catch(error => console.error('Erro ao adicionar produto:', error));

    fecharModal(); // Fechar modal após adicionar
}

// Função para editar um produto existente no banco de dados
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
            console.log("Produto editado com sucesso", data);
            window.location.reload(); // Recarrega a página após a edição
        })
        .catch(error => console.error('Erro ao editar produto:', error));

    fecharModal(); // Fechar modal após editar
}

// Função para atualizar a quantidade de um produto
function atualizarQuantidade(tipoProduto, valor) {
    console.log(tipoProduto + ", " + valor);
    fetch("atualizarProduto", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ tipoProduto, valor })  // Envia o tipo do produto e o valor para incrementar ou decrementar
    })
        .then(response => response.json())
        .then(data => {
            console.log("Produto atualizado com sucesso", data);
            window.location.reload();  // Recarrega a página após a atualização
        })
        .catch(error => console.error('Erro ao atualizar produto:', error));
}

// Função para fechar o modal
document.getElementById("closeModal").addEventListener("click", fecharModal);

function fecharModal() {
    const modal = document.getElementById("appointmentModal");
    modal.style.display = "none";  // Esconde o modal
    // Limpa o formulário
    document.getElementById("appointmentForm").reset();
}