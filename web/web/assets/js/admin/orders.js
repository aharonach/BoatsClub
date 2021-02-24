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
            {ajax: "rowers", valueField: "id", labelField: "id+name"}
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
        rowers: "Registered Rowers",
        boatTypes: "Boat Types",
        activityTitle: "Activity",
        activityDate: "Date",
        activityTime: "Time",
        registeredRower: "Added by",
        status: "Status",
        boat: "Boat appointed"
    };
}

function duplicateLink(orderId, label= "Duplicate") {
    return `<a class="duplicate-link" href="orders/duplicate" data-entity="orders" data-id="${orderId}">${label}</a>`;
}

function appointLink(orderId, boatTypes, label= "Appoint") {
    return `<a class="appoint-link" href="orders/appoint" data-entity="orders" data-boat-types="${boatTypes}" data-id="${orderId}">${label}</a>`;
}

function getOrderActions(order, approved) {
    return `<span class="actions d-block">${editLink("orders", order.id)} | ${duplicateLink(order.id)}${approved ? "" : " | " + appointLink(order.id, order.boatTypes)} | ${deleteLink("orders", order.id)}</span>`;
}

function orderRow(order, showActions = false) {
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${order.id} ${getOrderActions(order, order.approvedRequest)}</span></td>`;
    } else {
        row += `<td>${order.id}</td>`;
    }

    row += `<td>${order.rowers.length} Rowers<br>${editLinks("rowers", order.rowers)}</td>
        <td>${order.boatTypes}</td>
        <td>${order.activityTitle}</td>
        <td>${formatDate(order.activityDate)}</td>
        <td>${formatTime(order.activityStartTime) + ' - ' + formatTime(order.activityEndTime)}</td>
        <td>${editLink("rowers", order.registerRower, "Rower id " + order.registerRower)} at <br>${formatDateTime(order.registerDate)}</td>
        <td>${booleanFeather(order.approvedRequest)}</td>
        <td>${order.boat ? editLink("boats", order.boat, order.boat) : ""}</td>
    </tr>`;

    return row;
}

const mergeButton = () => {
    return `<button id="merge-orders-button" class="btn btn-secondary btn-sm">Merge Orders</button>`;
}

document.querySelectorAll('.orders-menu').forEach(link => {
    link.addEventListener("click", e => {
        e.preventDefault();
        const el = e.target;
        ajaxRequest(el.href).then(function(response) {
            let table = createTable('orders-list', getOrdersColumns(), response, orderRow, true, "orders");
            console.log(response);
            putContent(el.dataset.title, table, mergeButton());
            document.getElementById("merge-orders-button").addEventListener("click", e=>{
                prepareOptions(mergeOrdersFields).then((fields) => {
                    const form = new Form({
                        id: "merge-orders-form",
                        method: "post",
                        fields: fields,
                        action: "orders/merge",
                    });
                    showPopup("merge-orders-popup", "Merge Orders", form.getHtml());
                    submitForm("merge-orders-form","orders");
                });
            });
        });
    });
});