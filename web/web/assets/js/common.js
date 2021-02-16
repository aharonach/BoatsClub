const ROOT_PATH = '/boatsclub';

async function ajaxRequest(url, method = 'get', data = false) {
    try {
        let params = {
            method: method,
        };

        if (data) {
            data = data instanceof FormData ? formDataToSearchParams(data) : objectToSearchParams(data);
            if (method === 'post' || method === 'delete') {
                params.body = data;
            } else {
                url += '?' + data.toString();
            }
        }

        console.log(data.toString());
        console.log(params);

        const response = await fetch(url, params);

        return await response.json();
    } catch (e) {
        console.log(e);
        showAlert("error", e.message);
    }
}

function objectToSearchParams(obj) {
    const data = new URLSearchParams();
    for (const pair of Object.entries(obj)) {
        data.append(pair[0], pair[1]);
    }
    return data;
}

function formDataToSearchParams(obj) {
    const data = new URLSearchParams();
    for (const pair of obj) {
        data.append(pair[0], pair[1]);
    }
    return data;
}

function getFormData(formName) {
    const loginForm = document.forms.namedItem(formName);
    return new FormData(loginForm);
}

function putContent(title, content) {
    if (title) {
        const titleEl = document.getElementById("main-title");
        titleEl.innerHTML = title;
    }

    const mainEl = document.getElementById("main-content");
    mainEl.innerHTML = '';

    if (typeof(content) === 'string') {
        mainEl.innerHTML = content;
    } else {
        mainEl.appendChild(content);
    }

    // create icons if they are presented
    feather.replace();
}

function showAlert(type, content) {
    const feedback = document.getElementById('feedback');
    feedback.classList.remove('d-none');
    switch (type) {
        case 'success':
            feedback.classList.add('alert-success');
            feedback.classList.remove('alert-danger');
            feedback.classList.remove('alert-info');
            break;
        case 'error':
            feedback.classList.add('alert-danger');
            feedback.classList.remove('alert-success');
            feedback.classList.remove('alert-info');
            content = "Error: " + content;
            break;
        case 'info':
            feedback.classList.add('alert-info');
            feedback.classList.remove('alert-danger');
            feedback.classList.remove('alert-success');
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

function formatDate(object) {
    return object.day + '/' + object.month + '/' + object.year;
}

function formatTime(object) {
    return object.hour + ':' + object.minute;
}

function booleanFeather(object) {
    if (object.toString() === 'true')
        return `<span class="text-success" data-feather="check"></span>`;
    else if (object.toString() === 'false')
        return `<span class="text-danger" data-feather="x"></span>`;
    else
        return object;
}

function formatDateTime(object) {
    return formatDate(object.date) + ', ' + formatTime(object.time);
}

const editLink = (entity, id, label = "Edit") => {
    return `<a class="edit-link" href="${entity}/edit" data-entity="${entity}" data-id="${id}">${label}</a>`;
}

const deleteLink = (entity, id, label = "Delete") => {
    return `<a class="delete-link text-danger" href="${entity}/delete" data-entity="${entity}" data-id="${id}">${label}</a>`;
}

/**
 * Dropdown menu - open sub menu on click
 */
document.querySelectorAll('.main-nav-link').forEach(navLink => {
   navLink.addEventListener("click", e => {
       e.preventDefault();
       if (navLink.classList.contains('active')) {
           navLink.classList.remove('active');
       } else {
           navLink.classList.add('active');
       }
   });
});


/**
 * Edit record with popup
 */
document.addEventListener('click', event => {
    const el = event.target;
    if (el.tagName === 'A' && el.href.includes('/edit')) {
        event.preventDefault();
        let entity = el.dataset.entity,
            formFields = getFormFields(entity),
            entityId = el.dataset.id;

        ajaxRequest(entity, 'get', { id: entityId }).then(record => {
            const formName = 'edit-' + entity + '-' + entityId;
            const form = new Form({
                id: formName,
                method: 'post',
                fields: prepareFormFields(record, formFields),
                action: el.href,
            });
            showPopup("edit-entity", "Edit " + entity + " ID " + entityId, form.getHtml() );
            submitForm(formName, entity);
        });
    }
});

const submitForm = (formId, entity) => {
    const form = document.getElementById(formId);
    form.addEventListener("submit", event => {
        event.preventDefault();
        ajaxRequest(form.action, form.method, new FormData(form)).then(response => {
            // Close opened popups
            const popups = document.getElementsByClassName('modal');
            Array.prototype.forEach.call(popups, function(el) {
                hidePopup(el);
            });

            if (response) {
                if (response.status) {
                    showAlert("success", response.value);

                    // update entity list if it's present
                    if (document.getElementById(entity + "-list")) {
                        document.getElementById(entity).click();
                    }
                } else {
                    showAlert("error", response.error);
                }
            }
        });
    })
};