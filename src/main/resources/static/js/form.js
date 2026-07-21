const availableContainer = document.getElementById('available-drivers');
const selectedContainer = document.getElementById('selected-drivers');
const raceForm = document.getElementById('race-form');
const hiddenInputsContainer = document.createElement("div");

document.querySelectorAll('.dual-listbox').forEach(listbox => {
    listbox.addEventListener('click', function(e) {
        const btn = e.target.closest('.btn-card-action');
        if (!btn) return;

        const card = btn.closest('.driver-card');

        if (btn.classList.contains('btn-add')) {
            btn.textContent = '-';
            btn.classList.remove('btn-add');
            btn.classList.add('btn-remove');
            btn.title = "Remover";
            selectedContainer.appendChild(card);
        } else if (btn.classList.contains('btn-remove')) {
            btn.textContent = '+';
            btn.classList.remove('btn-remove');
            btn.classList.add('btn-add');
            btn.title = "Adicionar";
            availableContainer.appendChild(card);
        }
    });
});

raceForm.addEventListener('submit', function(e) {
    hiddenInputsContainer.innerHTML = '';
    const selectedCards = selectedContainer.querySelectorAll('.driver-card');
                
    if (selectedCards.length === 0) {
        e.preventDefault();
        alert('Selecione pelo menos um piloto para a corrida!');
        return;
    }

    selectedCards.forEach(card => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'driversId';
        input.value = card.getAttribute('data-id');
        hiddenInputsContainer.appendChild(input);
    });

    raceForm.appendChild(hiddenInputsContainer);
});