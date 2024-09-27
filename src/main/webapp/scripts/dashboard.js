document.querySelectorAll('.details-btn').forEach(button => {
    button.addEventListener('click', function() {
        const usuarioId = this.getAttribute('data-id');

        // Realiza a requisição AJAX para buscar os agendamentos
        fetch(`detalhesAgendamento?usuarioId=${usuarioId}`)
            .then(response => response.text())
            .then(html => {
                document.querySelector('.modal-body').innerHTML = html;
            })
            .catch(err => console.error(err));
    });
});
