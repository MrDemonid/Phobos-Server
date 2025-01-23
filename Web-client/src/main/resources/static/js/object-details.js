// Отправка запроса о привязке/отвязке телефона
function sendObjectRequest(url) {
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

/************************************************************************
 Функции для работы с таблицей графиков работы.
 ************************************************************************/
function addScheduleToObject() {
    const objectId = document.getElementById("objectId").value;
    const scheduleId = document.getElementById('availableSchedules').value;
    const url = `/api/web-service/object/${objectId}/link/schedule/${scheduleId}`;
    sendObjectRequest(url);
}

function deleteScheduleFromObject(scheduleId) {
    const objectId = document.getElementById("objectId").value;
    const url = `/api/web-service/object/${objectId}/unlink/schedule/${scheduleId}`;
    sendObjectRequest(url);
}

/************************************************************************
 Функции для работы с телефонами объектов.
 ************************************************************************/

/*
    Отвязка телефона от объекта.
 */
function deletePhoneFromObject(phoneId) {
    const objectId = document.getElementById("objectId").value;
    const url = `/api/web-service/object/${objectId}/unlink-phone/${phoneId}`;
    sendObjectRequest(url);
}

document.addEventListener("DOMContentLoaded", () => {
    let phoneTypeDescriptions = {};
    fetch("/api/web-service/phone/types")
        .then((response) => response.json())
        .then((data) => {
            phoneTypeDescriptions = data;
        })
        .catch((error) => console.error("Ошибка при загрузке типов телефонов:", error));

    const phoneSearchInput = document.getElementById("phoneObjectsSearch");
    const phoneSuggestions = document.getElementById("phoneObjectSuggestions");
    const objectId = document.getElementById("objectId").value;
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
        const url = `/api/web-service/object/${objectId}/link-phone/${phoneId}`;
        sendObjectRequest(url);
    }
});


/************************************************************************
 Функции для работы с ответственными лицами объектов.
 ************************************************************************/

/*
    Отвязка ответственного лица от объекта.
 */
function deletePersonFromObject(tabNo) {
    const objectId = document.getElementById("objectId").value;
    const url = `/api/web-service/object/${objectId}/unlink-person/${tabNo}`;
    sendObjectRequest(url);
}

document.addEventListener("DOMContentLoaded", () => {
    const phoneSearchInput = document.getElementById("personObjectSearch");
    const phoneSuggestions = document.getElementById("personObjectSuggestions");
    const objectId = document.getElementById("objectId").value;
    let debounceTimer;

    phoneSearchInput.addEventListener("input", (event) => {
        const query = event.target.value.trim();

        // Очищаем старые подсказки
        phoneSuggestions.innerHTML = "";

        if (!query) return;

        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            fetch(`/api/web-service/person/search?query=${encodeURIComponent(query)}`)
                .then((response) => response.json())
                .then((persons) => {
                    phoneSuggestions.innerHTML = "";

                    persons.slice(0, 10).forEach((person) => {
                        const li = document.createElement("li");
                        li.textContent = `${person.lastName} ${person.firstName} ${person.middleName}`;
                        li.dataset.tabNo = person.tabNo;

                        li.addEventListener("click", () => {
                            addPersonToObject(person.tabNo);
                        });

                        phoneSuggestions.appendChild(li);
                    });
                })
                .catch((error) => console.error("Ошибка при получении телефонов:", error));
        }, 300); // debounce 300ms
    });

    function addPersonToObject(tabNo) {
        const url = `/api/web-service/object/${objectId}/link-person/${tabNo}`;
        sendObjectRequest(url);
    }
});
