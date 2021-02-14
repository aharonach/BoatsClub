const ROOT_PATH = '/boatsclub';

async function ajaxRequest(url, method = 'get', data = {}) {
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

function putContent(title, content) {
    const mainEl = document.getElementById("main-content");
    mainEl.innerHTML = '';
    if (title) {
        const titleEl = document.getElementById("main-title");
        titleEl.innerHTML = title;
    }
    if (typeof(content) === 'string') {
        mainEl.innerHTML = content;
    } else {
        mainEl.appendChild(content);
    }
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

function createTable(id, columns, list, createRowFunction, showActions = false, menuItem) {
    if(list === undefined)
        putContent('No ' + menuItem + ' found.', '')
return `<div class="table-responsive"><table id="${id}" class="table table-hover"><thead>${tableHeader(columns)}</thead><tbody>${tableRows(list, createRowFunction, showActions)}</tbody></table></div>`;
}

function tableHeader(columns) {
    let header = `<tr>`;
    for (const [key, value] of Object.entries(columns)) {
        header += `<th>${value}</th>`;
    }
    header += `<tr>`;
    return header;
}

function tableRows(list, createRowFunction, showActions) {
    let rows = '';
    for(let i = 0;i < list.length; i++) {
        rows += createRowFunction(list[i], showActions);
    }
    return rows;
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