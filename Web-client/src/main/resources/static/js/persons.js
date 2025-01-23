/**
 * Переходим в режим редактирования записи.
 * То есть меняем тип полей на редактируемые.
 */
function editRow(id) {
    // Управление для полей
    const textTabNo = document.getElementById(`text-tabno-${id}`);
    const inputTabNo = document.getElementById(`input-tabno-${id}`);
    const textLastName = document.getElementById(`text-lastName-${id}`);
    const inputLastName = document.getElementById(`input-lastName-${id}`);
    const textFirstName = document.getElementById(`text-firstName-${id}`);
    const inputFirstName = document.getElementById(`input-firstName-${id}`);
    const textMiddleName = document.getElementById(`text-middleName-${id}`);
    const inputMiddleName = document.getElementById(`input-middleName-${id}`);
    const textBirthDate = document.getElementById(`text-birthDate-${id}`);
    const inputBirthDate = document.getElementById(`input-birthDate-${id}`);
    const textType = document.getElementById(`text-type-${id}`);
    const selectType = document.getElementById(`select-type-${id}`);

    // Управление кнопками
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textTabNo && inputTabNo && textLastName && inputLastName && textType && selectType && editButton && saveButton && deleteButton && cancelButton) {
        // Переключение полей
        textTabNo.style.display = 'none';
        inputTabNo.style.display = 'block';
        textLastName.style.display = 'none';
        inputLastName.style.display = 'block';
        textFirstName.style.display = 'none';
        inputFirstName.style.display = 'block';
        textMiddleName.style.display = 'none';
        inputMiddleName.style.display = 'block';
        textBirthDate.style.display = 'none';
        inputBirthDate.style.display = 'block';
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
    console.log(`Begin saving row ${id}`);
    // Управление для полей
    const textTabNo = document.getElementById(`text-tabno-${id}`);
    const inputTabNo = document.getElementById(`input-tabno-${id}`);
    const textLastName = document.getElementById(`text-lastName-${id}`);
    const inputLastName = document.getElementById(`input-lastName-${id}`);
    const textFirstName = document.getElementById(`text-firstName-${id}`);
    const inputFirstName = document.getElementById(`input-firstName-${id}`);
    const textMiddleName = document.getElementById(`text-middleName-${id}`);
    const inputMiddleName = document.getElementById(`input-middleName-${id}`);
    const textBirthDate = document.getElementById(`text-birthDate-${id}`);
    const inputBirthDate = document.getElementById(`input-birthDate-${id}`);
    const textType = document.getElementById(`text-type-${id}`);
    const selectType = document.getElementById(`select-type-${id}`);

    // Управление кнопками
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textTabNo && inputTabNo && textLastName && inputLastName && textType && selectType && editButton && saveButton && deleteButton && cancelButton) {
        console.log(`Do saving row ${id}`);
        textTabNo.textContent = inputTabNo.value;
        textLastName.textContent = inputLastName.value;
        textFirstName.textContent = inputFirstName.value;
        textMiddleName.textContent = inputMiddleName.value;
        textBirthDate.textContent = inputBirthDate.value;
        textType.textContent = selectType.options[selectType.selectedIndex].text;

        // Скрытие полей ввода, возврат к отображению текста
        textTabNo.style.display = 'block';
        inputTabNo.style.display = 'none';
        textLastName.style.display = 'block';
        inputLastName.style.display = 'none';
        textFirstName.style.display = 'block';
        inputFirstName.style.display = 'none';
        textMiddleName.style.display = 'block';
        inputMiddleName.style.display = 'none';
        textBirthDate.style.display = 'block';
        inputBirthDate.style.display = 'none';
        textType.style.display = 'block';
        selectType.style.display = 'none';
        // Обновление видимости кнопок
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        deleteButton.style.display = 'inline-block';
        cancelButton.style.display = 'none';

        // Создание объекта для отправки на сервер
        const updatePerson = {
            tabNo: inputTabNo.value,
            firstName: inputFirstName.value,
            lastName: inputLastName.value,
            middleName: inputMiddleName.value,
            birthDate: inputBirthDate.value,
            gender: selectType.value // Здесь используется значение атрибута value из <select>
            // gender: selectType.options[selectType.selectedIndex].gender
        };
        // Отправляем PUT-запрос на сервер
        fetch(`/api/web-service/person/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
            },
            body: JSON.stringify(updatePerson)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to update');
                }
                window.location.reload();
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка изменения данных сотрудника. Попробуйте снова.');
            });
        // console.log(`Saving row ${id}`);
    }

}

/**
 * Отмена редактирования записи.
 */
function cancelEdit(id) {
    // Элементы полей таблицы
    const textTabNo = document.getElementById(`text-tabno-${id}`);
    const inputTabNo = document.getElementById(`input-tabno-${id}`);
    const textLastName = document.getElementById(`text-lastName-${id}`);
    const inputLastName = document.getElementById(`input-lastName-${id}`);
    const textFirstName = document.getElementById(`text-firstName-${id}`);
    const inputFirstName = document.getElementById(`input-firstName-${id}`);
    const textMiddleName = document.getElementById(`text-middleName-${id}`);
    const inputMiddleName = document.getElementById(`input-middleName-${id}`);
    const textBirthDate = document.getElementById(`text-birthDate-${id}`);
    const inputBirthDate = document.getElementById(`input-birthDate-${id}`);
    const textType = document.getElementById(`text-type-${id}`);
    const selectType = document.getElementById(`select-type-${id}`);
    // Кнопки управления
    const editButton = document.getElementById(`edit-btn-${id}`);
    const saveButton = document.getElementById(`save-btn-${id}`);
    const deleteButton = document.getElementById(`delete-btn-${id}`);
    const cancelButton = document.getElementById(`cancel-btn-${id}`);

    if (textTabNo && inputTabNo && textLastName && inputLastName && textType && selectType && editButton && saveButton && deleteButton && cancelButton) {
        // Отмена изменений
        inputTabNo.value = textTabNo.textContent;
        inputLastName.value = textLastName.textContent;
        inputFirstName.value = textFirstName.textContent;
        inputMiddleName.value = textMiddleName.textContent;
        inputBirthDate.value = textBirthDate.textContent;

        // Отмена изменений для поля "gender"
        for (let i = 0; i < selectType.options.length; i++) {
            if (selectType.options[i].text === textType.textContent) {
                selectType.selectedIndex = i;
                break;
            }
        }
        // Скрытие полей редактирования, отображение текстовых представлений
        textTabNo.style.display = 'block';
        inputTabNo.style.display = 'none';
        textLastName.style.display = 'block';
        inputLastName.style.display = 'none';
        textFirstName.style.display = 'block';
        inputFirstName.style.display = 'none';
        textMiddleName.style.display = 'block';
        inputMiddleName.style.display = 'none';
        textBirthDate.style.display = 'block';
        inputBirthDate.style.display = 'none';
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
 * Добавление новой записи.
 */
function addNewPerson() {
    const newTabNoInput = document.getElementById('new-tabNo');
    const newLastNameInput = document.getElementById('new-lastName');
    const newFirstNameInput = document.getElementById('new-firstName');
    const newMiddleNameInput = document.getElementById('new-middleName');
    const newBirthDateInput = document.getElementById('new-birthDate');
    const newGenderInput = document.getElementById('new-gender');

    const tabNoInput = newTabNoInput.value.trim();
    const lastNameInput = newLastNameInput.value.trim();
    const firstNameInput = newFirstNameInput.value.trim();
    const middleNameInput = newMiddleNameInput.value.trim();
    const birthDateInput = newBirthDateInput.value.trim();
    const genderInput = newGenderInput.value.trim();

    if (!tabNoInput || !lastNameInput || !firstNameInput || !genderInput) {
        alert('Please enter all details.');
        return;
    }
    // Формируем объект для отправки на сервер
    const newPerson = {
        tabNo: tabNoInput,
        firstName: firstNameInput,
        lastName: lastNameInput,
        middleName: middleNameInput,
        birthDate: birthDateInput,
        gender: genderInput
    };

    // Отправляем POST-запрос
    fetch('/api/web-service/person/new', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + sessionStorage.getItem('jwtToken')
        },
        body: JSON.stringify(newPerson)
    })
        .then(response => {
            if (response.ok) {
                window.location.reload();
                // return response.json(); // Предполагаем, что сервер возвращает JSON с созданным объектом
            } else {
                throw new Error('Ошибка добавления сотрудника.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Ошибка добавления сотрудника. Попробуйте снова.');
        });
}


/**
 * Удаление записи.
 */
function deleteRow(tabNo) {
    if (confirm('Вы уверены что хотите удалить сотрудника?')) {
        // Отправляем запрос на сервер для удаления записи
        fetch(`/api/web-service/person/delete/${tabNo}`, {
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
                    throw new Error('Ошибка удаления сотрудника');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Ошибка удаления сотрудника. Попробуйте снова.');
            });
    }
    // для отладки
    console.log(`Deleting row with ID: ${tabNo}`);
}

