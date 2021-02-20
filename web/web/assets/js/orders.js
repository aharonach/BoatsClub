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
        id: "wantedActivity",
        type: "select",
        options: [
            {ajax: "activities", valueField: "id", labelField: "title"}
        ],
        label: "Select activity",
        required: true,
    },
    {
        id: "activityDate",
        type: "date",
        label: "Practice day (Later than today)",
        value: "",
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

document.querySelector('#orders').addEventListener("click", function(e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function(response) {
        let table = createTable('orders-list', getOrdersColumns(), response, orderRow, true, "orders");
        console.log(response);
        putContent("Orders", table);
    });
})

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
        submitForm("add-order", "orders");
    });
});