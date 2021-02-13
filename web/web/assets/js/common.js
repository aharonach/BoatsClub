const ROOT_PATH = '/boatsclub';

async function ajaxRequest(url, method = "get", data = {}) {
    try {
        let params = {
            method: method,
        };

        if (method === 'post') {
            params.body = data;
        }

        const response = await fetch(url, params);

        return await response.json();
    } catch (e) {
        console.log(e);
    }
}

function getFormData(formName) {
    const loginForm = document.forms.namedItem(formName);
    const data = new URLSearchParams();
    for (const pair of new FormData(loginForm)) {
        data.append(pair[0], pair[1]);
    }
    return data;
}

function putContent(content) {
    const main = document.getElementById("main-content");
    main.innerHTML = '';
    main.appendChild(content);
}

function showAlert(type, content) {
    const feedback = document.getElementById('feedback');
    feedback.classList.remove('d-none');
    switch (type) {
        case 'success':
            feedback.classList.add('alert-success');
            break;
        case 'error':
            feedback.classList.add('alert-danger');
            break;
        case 'info':
            feedback.classList.add('alert-info');
            break;
    }
    feedback.innerHTML = content;
    setTimeout(function() {
        feedback.innerHTML = '';
        feedback.classList.add('d-none');
    }, 5000);
}

function createTableHeader(columns) {
    let table = document.createElement("table");
    table.className = 'table table-responsive';
    const thead = document.createElement("tr");
    for (const [key, value] of Object.entries(columns)) {
        let th = document.createElement("th");
        th.innerHTML = value;
        thead.appendChild(th);
    }
    table.appendChild(thead);
    return table;
}

function createTableRows(list, table, columns) {
    for(i = 0; i < list.length; i++) {
        let tr = document.createElement("tr");
        console.log(list[i]);
        for (const [key, value] of Object.entries(list[i])) {
            console.log(value);
            let td = document.createElement("td");
            td.innerHTML = value;
            tr.appendChild(td);
        }
        table.appendChild(tr);
    }
    return table;
}

// document.querySelectorAll('.nav-link').forEach(function(navLink) {
//    navLink.addEventListener("click", function(e) {
//        e.preventDefault();
//        ajaxRequest(navLink.href).then(function(response) {
//            console.log(response);
//            let table = createTableRows(response, createTableHeader(getBoatsColumns()), getBoatsColumns());
//            putContent(table);
//        });
//    })
// });