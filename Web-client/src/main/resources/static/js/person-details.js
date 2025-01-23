// Отправка запроса о привязке/отвязке телефона
function sendPhoneRequest(url) {
    fetch(url, {
        method: 'POST',
    })
        .then((response) => {
            if (!response.ok) {
                // Попытка прочитать тело ответа
                return response.text().then((text) => {
                    const errorMessage = text ? JSON.parse(text).message : "Ошибка при обработке запроса";
                    throw new Error(errorMessage);
                });
            }
            return response; // Если всё в порядке, продолжаем
        })
        .then(() => {
            location.reload(); // Перезагрузка страницы для обновления данных
        })
        .catch((error) => {
            console.error("Ошибка при выполнении запроса:", error);
            alert(error.message);
        });
}

/*
    Отвязка телефона от сотрудника.
 */
function deletePhone(phoneId) {
    const personId = document.getElementById("personId").value; // Получаем personId из скрытого поля
    const url = `/api/web-service/person/${personId}/unlink-phone/${phoneId}`;
    sendPhoneRequest(url);
}

/************************************************************************
 Функции для работы с таблицей адресов.
 ************************************************************************/
function addAddress() {
    const personId = document.getElementById("personId").value;
    // Получение значений из полей ввода
    const address = {
        street: document.getElementById('street').value,
        city: document.getElementById('city').value,
        postalCode: document.getElementById('postalCode').value,
        description: document.getElementById('description').value
    };
    // Отправка данных на сервер
    fetch(`/api/web-service/person/add/address/${personId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(address)
    })
        .then((response) => {
            if (!response.ok) {
                // Попытка прочитать тело ответа
                return response.text().then((text) => {
                    const errorMessage = text ? JSON.parse(text).message : "Ошибка при обработке запроса";
                    throw new Error(errorMessage);
                });
            }
            return response; // Если всё в порядке, продолжаем
        })
        .then(() => {
            location.reload(); // Перезагрузка страницы для обновления данных
        })
        .catch((error) => {
            console.error("Ошибка при выполнении запроса:", error);
            alert(error.message);
        });
}

function deleteAddress(item) {
    const personId = document.getElementById("personId").value;
    const address = {
        postalCode: item.getAttribute('data-postal-code'),
        city: item.getAttribute('data-city'),
        street: item.getAttribute('data-street'),
        description: item.getAttribute('data-description')
    };
    // console.log('begin delete Address: ', address);
    fetch(`/api/web-service/person/remove/address/${personId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(address)
    })
        .then((response) => {
            if (!response.ok) {
                // Попытка прочитать тело ответа
                return response.text().then((text) => {
                    const errorMessage = text ? JSON.parse(text).message : "Ошибка при обработке запроса";
                    throw new Error(errorMessage);
                });
            }
            return response; // Если всё в порядке, продолжаем
        })
        .then(() => {
            location.reload(); // Перезагрузка страницы для обновления данных
        })
        .catch((error) => {
            console.error("Ошибка при выполнении запроса:", error);
            alert(error.message); // Выводим сообщение пользователю
        });

}

/************************************************************************
 Функции для работы с таблицей графиков работы.
 ************************************************************************/
function addSchedule() {
    const personId = document.getElementById("personId").value;
    const scheduleId = document.getElementById('availableSchedules').value;
    const url = `/api/web-service/person/${personId}/link/schedule/${scheduleId}`;
    sendPhoneRequest(url);
}

function deleteSchedule(scheduleId) {
    const personId = document.getElementById("personId").value;
    const url = `/api/web-service/person/${personId}/unlink/schedule/${scheduleId}`;
    sendPhoneRequest(url);
}


/**
 * Функции для привязки/отвязки телефонов сотруднику.
 */
document.addEventListener("DOMContentLoaded", () => {
    let phoneTypeDescriptions = {};
    fetch("/api/web-service/phone/types")
        .then((response) => response.json())
        .then((data) => {
            phoneTypeDescriptions = data;
        })
        .catch((error) => console.error("Ошибка при загрузке типов телефонов:", error));

    const phoneSearchInput = document.getElementById("phoneSearch");
    const phoneSuggestions = document.getElementById("phoneSuggestions");
    const personId = document.getElementById("personId").value; // Получаем personId из скрытого поля
    let debounceTimer;

    phoneSearchInput.addEventListener("input", (event) => {
        const query = event.target.value.trim();

        // Очищаем старые подсказки
        phoneSuggestions.innerHTML = "";

        if (!query) return;

        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            fetch(`/api/web-service/phone/search?query=${encodeURIComponent(query)}`)
                .then((response) => response.json())
                .then((phones) => {
                    phoneSuggestions.innerHTML = "";

                    phones.slice(0, 10).forEach((phone) => {
                        const li = document.createElement("li");
                        li.textContent = `${phone.number} (${phoneTypeDescriptions[phone.type] || "Нет типа"})`;
                        // li.textContent = `${phone.number} (${phone.type || "Нет типа"})`;
                        li.dataset.phoneId = phone.id;

                        li.addEventListener("click", () => {
                            addPhone(phone.id);
                        });

                        phoneSuggestions.appendChild(li);
                    });
                })
                .catch((error) => console.error("Ошибка при получении телефонов:", error));
        }, 300); // debounce 300ms
    });

    function addPhone(phoneId) {
        const url = `/api/web-service/person/${personId}/link-phone/${phoneId}`;
        sendPhoneRequest(url);
    }
});
