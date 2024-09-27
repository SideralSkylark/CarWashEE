const modal = document.getElementById("appointmentModal");
const newAppointmentBtn = document.getElementById("newAppointmentBtn");
const closeModalBtn = document.getElementById("closeModal");
const appointmentForm = document.getElementById("appointmentForm");
const appointmentTable = document.getElementById("appointmentTable").getElementsByTagName('tbody')[0];
let editingRow = null;

// Função para abrir o modal
newAppointmentBtn.addEventListener("click", function() {
    editingRow = null;  // Indica que é uma nova adição
    modal.style.display = "block";
    appointmentForm.reset();  // Limpa o formulário ao abrir o modal
});

// Função para fechar o modal
closeModalBtn.addEventListener("click", function() {
    modal.style.display = "none";
});

// Função para fechar o modal clicando fora dele
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
};

// Função para adicionar ou editar agendamento
appointmentForm.addEventListener("submit", function(e) {
    e.preventDefault();

    const descricao = document.getElementById("descricao").value;
    const data = document.getElementById("data").value;
    const servico = document.getElementById("servico").value;
    const plano = document.getElementById("plano").value;
    const preco = document.getElementById("preco").value;

    const precoNumero = parseFloat(preco.replace('R$', '').replace(',', '.')); // Remove 'R$' e substitui vírgula por ponto

    const dataAtualizada = {
        descricao: descricao,
        data: data,
        plano: plano,
        servico: servico,
        preco: precoNumero
    };

    if (editingRow) {
        // Editando um agendamento existente
        dataAtualizada.id = editingRow.dataset.id;
        fetch('editarAgendamento', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(dataAtualizada)
        })
            .then(response => response.ok ? location.reload() : alert('Erro ao editar agendamento!'))
            .catch(error => console.error('Erro:', error));

    } else {
        // Adicionando um novo agendamento
        fetch('adicionarAgendamento', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams(dataAtualizada)
        })
            .then(response => response.ok ? location.reload() : alert('Erro ao adicionar agendamento!'))
            .catch(error => console.error('Erro:', error));
    }

    modal.style.display = "none";
});

// Função para editar agendamentos
function editAppointment(button) {
    editingRow = button.parentElement.parentElement;
    const descricao = editingRow.cells[0].textContent;
    const data = editingRow.cells[1].textContent;
    const servico = editingRow.cells[2].textContent;
    const plano = editingRow.cells[3].textContent;
    const preco = editingRow.cells[4].textContent.replace('R$', '').trim();

    document.getElementById("descricao").value = descricao;
    document.getElementById("data").value = data;
    document.getElementById("servico").value = servico;
    document.getElementById("plano").value = plano;
    document.getElementById("preco").value = preco;

    modal.style.display = "block";
}

// Função para deletar agendamentos
function deleteAppointment(button) {
    if (confirm("Tem certeza que deseja cancelar o agendamento?")) {
        const row = button.parentElement.parentElement;
        const id = row.dataset.id;

        fetch('deletarAgendamento', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({id: id})
        })
            .then(response => response.ok ? row.remove() : alert('Erro ao deletar agendamento!'))
            .catch(error => console.error('Erro:', error));
    }
}

// Função para carregar agendamentos ao entrar na página
window.addEventListener("DOMContentLoaded", function() {
    fetch("obterAgendamentosAtivos")
        .then(response => response.json())
        .then(agendamentos => {
            console.log(agendamentos);
            appointmentTable.innerHTML = "";

            agendamentos.forEach(agendamento => {
                const newRow = appointmentTable.insertRow();
                newRow.setAttribute('data-id', agendamento.id); // Armazena o ID do agendamento

                const descricaoCell = newRow.insertCell(0);
                const dataCell = newRow.insertCell(1);
                const servicoCell = newRow.insertCell(2);
                const planoCell = newRow.insertCell(3);
                const precoCell = newRow.insertCell(4);
                const actionsCell = newRow.insertCell(5);

                descricaoCell.textContent = agendamento.descricao;
                dataCell.textContent = agendamento.data;
                servicoCell.textContent = agendamento.servico.descricao;
                planoCell.textContent = agendamento.servico.plano;
                precoCell.textContent = `R$ ${parseFloat(agendamento.servico.preco).toFixed(2)}`;

                actionsCell.innerHTML = `
                    <button class="edit-btn" onclick="editAppointment(this)">Editar</button>
                    <button class="delete-btn" onclick="deleteAppointment(this)">Cancelar</button>
                `;
            });
        })
        .catch(error => console.error('Erro ao carregar agendamentos:', error));
});

// Atualizar preço ao mudar serviço ou plano
function updatePreco() {
    const tipoServico = document.getElementById("servico").value;
    const plano = document.getElementById("plano").value;

    if (tipoServico && plano) {
        fetch(`obterPreco?tipoServico=${tipoServico}&plano=${plano}`)
            .then(response => response.json())
            .then(data => {
                if (data.preco) {
                    const preco = parseFloat(data.preco);
                    if (!isNaN(preco)) {
                        document.getElementById("preco").value = `MZ ${preco.toFixed(2)}`;
                    } else {
                        console.error('Preço inválido recebido:', data.preco);
                        document.getElementById("preco").value = "Preço inválido";
                    }
                } else {
                    console.error('Preço não disponível.');
                    document.getElementById("preco").value = "Preço não disponível";
                }
            })
            .catch(error => console.error('Erro ao buscar o preço:', error));
    }
}

document.getElementById("servico").addEventListener("change", updatePreco);
document.getElementById("plano").addEventListener("change", updatePreco);

// Atualizar lista de serviços ao mudar plano
document.getElementById("plano").addEventListener("change", function () {
    const plano = this.value;
    const tipoServicoSelect = document.getElementById("servico");

    if (plano) {
        fetch(`obterServicosPorPlano?plano=${plano}`)
            .then(response => response.json())
            .then(data => {
                tipoServicoSelect.innerHTML = "";

                data.forEach(servico => {
                    const option = document.createElement("option");
                    option.value = servico.tipoServico;
                    option.textContent = servico.descricao;
                    tipoServicoSelect.appendChild(option);
                });

                tipoServicoSelect.disabled = false;
            })
            .catch(error => console.error('Erro ao buscar os serviços:', error));
    }
});
