/**
 * Переходим в режим редактирования записи.
 */
function editRow(id) {
    // Управление для поля "number"
    const textNumber = document.getElementById(`text-number-${id}`);
    const inputNumber = document.getElementById(`input-number-${id}`);
    // Управление для поля "description"
    const textDescription = document.getElementById(`text-description-${id}`);
    const inputDescription = document.getElementById(`input-description-${id}`);
    // Управление для поля "type"
    const textType = document.getElementById(`text-type-${id}`);
    const selectType = document.getElementById(`select-type-${id}`);
    // Управление кнопками
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textNumber && inputNumber && textType && selectType && editButton && saveButton && deleteButton && cancelButton) {
        // Переключение для поля "number"
        textNumber.style.display = 'none';
        inputNumber.style.display = 'block';
        // Переключение для поля "description"
        textDescription.style.display = 'none';
        inputDescription.style.display = 'block';
        // Переключение для поля "type"
        textType.style.display = 'none';
        selectType.style.display = 'block';
        // Управление кнопками
        editButton.style.display = 'none';
        saveButton.style.display = 'inline-block';
        deleteButton.style.display = 'none';
        cancelButton.style.display = 'inline-block';
    }
    // console.log(`Edit row with ID: ${id}`);
}

/**
 * Сохранение измененной записи.
 */
function saveRow(id) {
    // Элементы для поля "number"
    const inputNumber = document.getElementById(`input-number-${id}`);
    const textNumber = document.getElementById(`text-number-${id}`);

    // Элементы для поля "number"
    const inputDescription = document.getElementById(`input-description-${id}`);
    const textDescription = document.getElementById(`text-description-${id}`);

    // Элементы для поля "type"
    const selectType = document.getElementById(`select-type-${id}`);
    const textType = document.getElementById(`text-type-${id}`);

    // Кнопки управления
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (
        inputNumber && textNumber &&
        selectType && textType &&
        editButton && saveButton && deleteButton && cancelButton
    ) {
        // Обновление данных на клиенте
        textNumber.textContent = inputNumber.value;
        textDescription.textContent = inputDescription.value;
        textType.textContent = selectType.options[selectType.selectedIndex].text;

        // Скрытие полей ввода, возврат к отображению текста
        textNumber.style.display = 'block';
        inputNumber.style.display = 'none';
        textDescription.style.display = 'block';
        inputDescription.style.display = 'none';
        textType.style.display = 'block';
        selectType.style.display = 'none';

        // Обновление видимости кнопок
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        deleteButton.style.display = 'inline-block';
        cancelButton.style.display = 'none';

        // Создание объекта для отправки на сервер
        const updatedPhone = {
            id: id,
            number: inputNumber.value,
            description: inputDescription.value,
            type: selectType.value // Здесь используется значение атрибута value из <select>
        };

        // Отправляем PUT-запрос на сервер
        fetch(`/api/web-service/phone/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify(updatedPhone)
        })
            .then(response => {
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
    // Элементы для поля "number"
    const textNumber = document.getElementById(`text-number-${id}`);
    const inputNumber = document.getElementById(`input-number-${id}`);

    // Элементы для поля "description"
    const textDescription = document.getElementById(`text-description-${id}`);
    const inputDescription = document.getElementById(`input-description-${id}`);

    // Элементы для поля "type"
    const textType = document.getElementById(`text-type-${id}`);
    const selectType = document.getElementById(`select-type-${id}`);

    // Кнопки управления
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textNumber && inputNumber && textType && selectType && editButton && saveButton && deleteButton && cancelButton) {
        // Отмена изменений для поля "number"
        inputNumber.value = textNumber.textContent;
        inputDescription.value = textDescription.textContent;

        // Отмена изменений для поля "type"
        for (let i = 0; i < selectType.options.length; i++) {
            if (selectType.options[i].text === textType.textContent) {
                selectType.selectedIndex = i;
                break;
            }
        }

        // Скрытие полей редактирования, отображение текстовых представлений
        textNumber.style.display = 'block';
        inputNumber.style.display = 'none';
        textDescription.style.display = 'block';
        inputDescription.style.display = 'none';
        textType.style.display = 'block';
        selectType.style.display = 'none';
        // Управление видимостью кнопок
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
    if (confirm('Вы уверены что хотите удалить телефон?')) {
        // Отправляем запрос на сервер для удаления записи
        fetch(`/api/web-service/phone/delete/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken') // если используется JWT
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
                alert('Ошибка удаления. Попробуйте снова.');
            });
    }
}

/**
 * Добавление новой записи.
 */
function addNewPhone() {
    const newNumberInput = document.getElementById('new-number');
    const newDescriptionInput = document.getElementById('new-description');
    const newTypeInput = document.getElementById('new-type');
    const phoneNumber = newNumberInput.value.trim();
    const descriptionInput = newDescriptionInput.value.trim();
    const phoneType = newTypeInput.value.trim();

    if (!phoneNumber || !phoneType) {
        alert('Заполните поля номера телефона и его тип.');
        return;
    }

    // Формируем объект для отправки на сервер
    const newPhone = {
        number: phoneNumber,
        type: phoneType,
        description: descriptionInput
    };

    // Отправляем POST-запрос
    fetch('/api/web-service/phone/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
        },
        body: JSON.stringify(newPhone)
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // Предполагаем, что сервер возвращает JSON с созданным объектом
            } else {
                throw new Error('Ошибка добавления телефона.');
            }
        })
        .then(createdPhone => {
            window.location.reload();
            // Очищаем поля ввода
            newNumberInput.value = '';
            newDescriptionInput.value = '';
            newTypeInput.value = '';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ошибка добавления телефона. Попробуйте снова.');
        });
}