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
        label: "Select Yourself + optional rowers",
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
        // registeredRower: "Added by",
        status: "Status",
        boat: "Boat appointed"
    };
}

function getOrderActions(orderId, approved) {
    return `<span class="actions d-block"> ${editLink("orders", orderId)}</span>`;
}

function orderRow(order, showActions = false) {
    console.log(order);
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${order.id} ${getOrderActions(order.id, order.approvedRequest)}</span></td>`;
    } else {
        row += `<td>${order.id}</td>`;
    }

    row += `<td>${editLinks("rowers", order.rowers)}</td>
        <td>${order.boatTypes}</td>
        <td>${order.activityTitle}</td>
        <td>${formatDate(order.activityDate)}</td>
        <td>${formatTime(order.activityStartTime) + ' - ' + formatTime(order.activityEndTime)}</td>
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
            putContent(el.dataset.title, table, "");
        });
    });
});



