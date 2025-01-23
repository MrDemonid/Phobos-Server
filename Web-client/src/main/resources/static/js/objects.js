/**
 * Добавление нового объекта.
 */
function addNewObject() {
    const idInput = document.getElementById("new-objectId").value.trim();
    const newDescriptionInput = document.getElementById("new-description").value.trim();
    const newAddressInput = document.getElementById("new-address").value.trim();

    if (!idInput) {
        alert('Введите номер объекта.');
        return;
    }

    // создаем новый объект
    const newObject = {
        id: idInput,
        description: newDescriptionInput,
        address: newAddressInput
    };

    // Отправляем POST-запрос
    fetch('/api/web-service/object/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
        },
        body: JSON.stringify(newObject)
    })
        .then(response => {
            if (response.ok) {
                window.location.reload();
                // alert('Объект успешно добавлен!');
            } else {
                throw new Error('Ошибка добавления объекта');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert('Ошибка добавления объекта. Попробуйте снова.');
        });
}


/**
 * Переходим в режим редактирования записи.
 * То есть меняем тип полей на редактируемые.
 */
function editRowObject(id) {
    // Управление для полей
    const textId = document.getElementById(`text-objectId-${id}`);
    const inputId = document.getElementById(`input-objectId-${id}`);
    const textDescription = document.getElementById(`text-description-${id}`);
    const inputDescription = document.getElementById(`input-description-${id}`);
    const textAddress = document.getElementById(`text-address-${id}`);
    const inputAddress = document.getElementById(`input-address-${id}`);

    // Управление кнопками
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textId && inputId && textDescription && inputDescription && textAddress && inputAddress && editButton && saveButton && deleteButton && cancelButton) {
        // Переключение полей
        textId.style.display = 'none';
        inputId.style.display = 'block';
        textDescription.style.display = 'none';
        inputDescription.style.display = 'block';
        textAddress.style.display = 'none';
        inputAddress.style.display = 'block';
        // Управление кнопками
        editButton.style.display = 'none';
        saveButton.style.display = 'inline-block';
        deleteButton.style.display = 'none';
        cancelButton.style.display = 'inline-block';
    }
    // console.log(`Edit row with ID: ${id}`);
}


/**
 * Отмена редактирования записи.
 */
function cancelEditObject(id) {
    // Элементы полей таблицы
    const textId = document.getElementById(`text-objectId-${id}`);
    const inputId = document.getElementById(`input-objectId-${id}`);
    const textDescription = document.getElementById(`text-description-${id}`);
    const inputDescription = document.getElementById(`input-description-${id}`);
    const textAddress = document.getElementById(`text-address-${id}`);
    const inputAddress = document.getElementById(`input-address-${id}`);
    // Кнопки управления
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textId && inputId && textDescription && inputDescription && textAddress && inputAddress && editButton && saveButton && deleteButton && cancelButton) {
        // Отмена изменений
        inputId.value = textId.textContent;
        inputDescription.value = textDescription.textContent;
        inputAddress.value = textAddress.textContent;
        // Скрытие полей редактирования, отображение текстовых представлений
        textId.style.display = 'block';
        inputId.style.display = 'none';
        textDescription.style.display = 'block';
        inputDescription.style.display = 'none';
        textAddress.style.display = 'block';
        inputAddress.style.display = 'none';

        // Управление видимостью кнопок
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        deleteButton.style.display = 'inline-block';
        cancelButton.style.display = 'none';
    }
}


/**
 * Сохранение измененной записи.
 */
function saveRowObject(id) {
    // Управление для полей
    const textId = document.getElementById(`text-objectId-${id}`);
    const inputId = document.getElementById(`input-objectId-${id}`);
    const textDescription = document.getElementById(`text-description-${id}`);
    const inputDescription = document.getElementById(`input-description-${id}`);
    const textAddress = document.getElementById(`text-address-${id}`);
    const inputAddress = document.getElementById(`input-address-${id}`);
    // Управление кнопками
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textId && inputId && editButton && saveButton && deleteButton && cancelButton) {
        textId.textContent = inputId.value;
        textDescription.textContent = inputDescription.value;
        textAddress.value = inputAddress.value;
        // Скрытие полей ввода, возврат к отображению текста
        textId.style.display = 'block';
        inputId.style.display = 'none';
        textDescription.style.display = 'block';
        inputDescription.style.display = 'none';
        textAddress.style.display = 'block';
        inputAddress.style.display = 'none';
        // Обновление видимости кнопок
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        deleteButton.style.display = 'inline-block';
        cancelButton.style.display = 'none';

        // Формируем объект для отправки на сервер
        const updateObject = {
            id: inputId.value,
            description: inputDescription.value,
            address: inputAddress.value
        };
        // Отправляем PUT-запрос на сервер
        fetch(`/api/web-service/object/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify(updateObject)
        })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    throw new Error('Ошибка обновления данных объекта');
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Ошибка обновления данных объекта. Попробуйте снова.');
            });
    }
}

/**
 * Удаление записи.
 */
function deleteRowObject(id) {
    if (confirm('Вы уверены, что хотите удалить объект?')) {
        // Отправляем запрос на сервер для удаления записи
        fetch(`/api/web-service/object/delete/${id}`, {
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
                    throw new Error('Ошибка удаления объекта');
                }
            })
            .catch(error => {
                console.error('Ошибка:', error);
                alert('Ошибка удаления объекта. Попробуйте снова.');
            });
    }
}

