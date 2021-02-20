const ordersFormFields = [
    {
        id: "boatTypes",
        type: "select",
        label: "Boat Type",
        required: true,
        multiple: true,
        options: [
            {value: "Single", selected: true, label: "Single"},
            {value: "Double", selected: false, label: "Double"},
            {value: "Coxed_Double", selected: false, label: "Coxed Double"},
            {value: "Pair", selected: false, label: "Pair"},
            {value: "Coxed_Pair", selected: false, label: "Coxed Pair"},
            {value: "Four", selected: false, label: "Four"},
            {value: "Coxed_Four", selected: false, label: "Coxed Four"},
            {value: "Quad", selected: false, label: "Quad"},
            {value: "Coxed_Quad", selected: false, label: "Coxed Quad"},
            {value: "Octuple", selected: false, label: "Octuple"},
            {value: "Eight", selected: false, label: "Eight"},
        ],
    },
    {
        id: "rowers",
        type: "select",
        options: [
            {ajax: "rowers", valueField: "id", labelField: "name"}
        ],
        label: "Select rowers",
        required: true,
        multiple: true,
    },
    {
        id: "activityDate",
        type: "date",
        label: "Practice day (Later than today)",
        value: "",
    },
    {
        id: "wantedActivity",
        type: "select",
        options: [
            {ajax: "activities", valueField: "id", labelField: "title"}
        ],
        label: "Select activity",
        required: true,
        // includeEmpty: true,
    },
    {
        id: "activityTitle",
        type: "text",
        label: "Activity Title",
        required: false,
        readonly: true,
    },
    {
        id: "activityStartTime",
        type: "time",
        label: "Start Time",
        required: false,
        readonly: true,
    },
    {
        id: "activityEndTime",
        type: "time",
        label: "End Time",
        required: false,
        readonly: true,
    },
];

function getOrdersColumns() {
    return {
        id: "Id",
        rowers: "Registered rowers id's",
        boatTypes: "Boat types",
        activityDate: "Activity date",
        activityTime: "Activity time",
        registeredRower: "Added by",
        status: "Status",
        boat: "Boat appointed"
    };
}

function duplicateLink(orderId, label= "Duplicate") {
    return `<a class="duplicate-link" href="orders/duplicate" data-entity="orders" data-id="${orderId}">${label}</a>`;
}

function appointLink(orderId, label= "Appoint") {
    return `<a class="appoint-link" href="orders/appoint" data-entity="orders" data-id="${orderId}">${label}</a>`;
}

function getOrderActions(orderId, approved) {
    return `<span class="actions d-block">${editLink("orders", orderId)} | ${duplicateLink(orderId)}${approved ? "" : " | "+appointLink(orderId)} | ${deleteLink("orders", orderId)}</span>`;
}

function orderRow(order, showActions = false) {
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${order.id} ${getOrderActions(order.id, order.approvedRequest)}</span></td>`;
    } else {
        row += `<td>${order.id}</td>`;
    }

    row += `<td>${editLinks("rowers", order.rowers)}</td>
        <td>${order.boatTypes}</td>
        <td>${formatDate(order.activityDate)}</td>
        <td>${formatTime(order.activityStartTime) + ' - ' + formatTime(order.activityEndTime)}</td>
        <td>${editLink("rowers", order.registerRower, order.registerRower)}</td>
        <td>${booleanFeather(order.approvedRequest)}</td>
        <td>${order.boat ? editLink("boats", order.boat, order.boat) : ""}</td>
    </tr>`;

    return row;
}

document.querySelectorAll('.orders-menu').forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault();
        const el = e.target;
        ajaxRequest(el.href).then(function(response) {
            let table = createTable('orders-list', getOrdersColumns(), response, orderRow, true, "orders");
            console.log(response);
            putContent(el.dataset.title, table);
        });
    });
});

document.querySelector('#orders-add').addEventListener("click", function(e) {
    e.preventDefault();
    prepareOptions(ordersFormFields).then((fields) => {
        const form = new Form({
            id: "add-order",
            method: "post",
            fields: fields,
            action: e.target.href,
        });
        putContent("Add New Order", form.getHtml());
        activityFieldChange(document.getElementById("wantedActivity"));
        submitForm("add-order", "orders");
    });
});

/**
 *
 * Handle Activities in order forms
 *
 */

const activityFieldEvent = e => {
    const element = e.target;
    if (element.tagName === 'SELECT' && element.id === 'wantedActivity') {
        activityFieldChange(element);
    }
};

document.addEventListener("change", activityFieldEvent);
document.addEventListener("click", activityFieldEvent);

const activityFieldRemove = () => {
    const selectEl = document.getElementById("wantedActivity"),
        activityTitle = document.getElementById('activityTitle'),
        activityStartTime = document.getElementById('activityStartTime'),
        activityEndTime = document.getElementById('activityEndTime');

    if (!selectEl.querySelector('option')) {
        selectEl.closest('.form-group').remove();
        activityTitle.readOnly = activityStartTime.readOnly = activityEndTime.readOnly = false;
        activityTitle.required = activityStartTime.required = activityEndTime.required = true;
        return true;
    }
    return false;
}

const activityFieldChange = selectEl => {
    if (!selectEl) {
        return;
    }

    const selected = selectEl.value,
        activityTitle = document.getElementById('activityTitle'),
        activityStartTime = document.getElementById('activityStartTime'),
        activityEndTime = document.getElementById('activityEndTime');

    activityFieldRemove();

    if (selected) {
        ajaxRequest('activities', 'get', {id: selected}).then(response => {
            activityTitle.value = response.title;
            activityStartTime.value = formatTime(response.startTime);
            activityEndTime.value = formatTime(response.endTime);
            activityTitle.readOnly = activityStartTime.readOnly = activityEndTime.readOnly = true;
            activityTitle.required = activityStartTime.required = activityEndTime.required = false;
        });
    } else {
        activityTitle.value = activityStartTime.value = activityEndTime.value = "";
        activityTitle.readOnly = activityStartTime.readOnly = activityEndTime.readOnly = false;
        activityTitle.required = activityStartTime.required = activityEndTime.required = true;
    }
};