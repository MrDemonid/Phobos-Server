/**
 * Переходим в режим редактирования записи.
 */
function editRow(id) {
    const text = document.getElementById(`text-${id}`);
    const input = document.getElementById(`input-${id}`);
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (text && input && editButton && saveButton && deleteButton && cancelButton) {
        text.style.display = 'none';
        input.style.display = 'block';
        editButton.style.display = 'none';
        saveButton.style.display = 'inline-block';
        deleteButton.style.display = 'none';
        cancelButton.style.display = 'inline-block';
    }
}

/**
 * Сохранение измененной записи.
 */
function saveRow(id) {
    const input = document.getElementById(`input-${id}`);
    const text = document.getElementById(`text-${id}`);
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (input && text && editButton && saveButton && deleteButton && cancelButton) {
        text.textContent = input.value;

        text.style.display = 'block';
        input.style.display = 'none';
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        deleteButton.style.display = 'inline-block';
        cancelButton.style.display = 'none';

        // Отправляем обновленное значение на сервер
        const updatedSchedule = {
            id: id,
            scheduleDetails: input.value
        };
        fetch(`/api/web-service/work-schedule/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify(updatedSchedule)
        }).then(response => {
            if (!response.ok) {
                throw new Error('Ошибка обновления данных.');
            }
        })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка обновления данных. Попробуйте снова.')
            });
    }
}

/**
 * Отмена редактирования записи.
 */
function cancelEdit(id) {
    const text = document.getElementById(`text-${id}`);
    const input = document.getElementById(`input-${id}`);
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (text && input && editButton && saveButton && deleteButton && cancelButton) {
        input.value = text.textContent;

        text.style.display = 'block';
        input.style.display = 'none';
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        deleteButton.style.display = 'inline-block';
        cancelButton.style.display = 'none';
    }
}

/**
 * Удаление записи.
 */
function deleteRow(id) {
    if (confirm('Вы уверены что хотите удалить режим работы?')) {
        // Отправляем запрос на сервер для удаления записи
        fetch(`/api/web-service/work-schedule/delete/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            }
        })
            .then(response => {
                if (response.ok) {
                    // Перезагружаем страницу, чтобы отобразить обновленный список
                    window.location.reload();
                } else {
                    throw new Error('Ошибка удаления.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка удаления. Попробуйте снова.')
            });
    }
}

/**
 * Добавление новой записи.
 */
function addNewSchedule() {
    const newScheduleInput = document.getElementById('new-schedule');
    const scheduleDetails = newScheduleInput.value.trim();

    if (!scheduleDetails) {
        alert('Пожалуйста, введите данные.');
        return;
    }
    // Формируем строку для отправки на сервер
    const newSchedule = {
        scheduleDetails: scheduleDetails
    };
    // Отправляем POST-запрос
    fetch('/api/web-service/work-schedule/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
        },
        body: JSON.stringify(newSchedule)
    }).then(response => {
        if (response.ok) {
            return response.json(); // Предполагаем, что сервер возвращает JSON с созданным объектом
        } else {
            throw new Error('Ошибка добавления.');
        }
    })
        .then(createdSchedule => {
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ошибка добавления. Попробуйте снова.');
        });
}