
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

function getOrderActions() {
    return `<span class="actions d-block"><a class="edit-link" href="#">Edit</a> | <a class="delete-link text-danger" href="#">Delete</a>`;
}

function orderRow(order, showActions = false) {
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${order.id} ${getOrderActions()}</span></td>`;
    } else {
        row += `<td>${order.id}</td>`;
    }

    row += `<td>${order.rowers}</td>
        <td>${order.boatTypes}</td>
        <td>${formatDate(order.activityDate)}</td>
        <td>${formatTime(order.activityStartTime) + ' - ' + formatTime(order.activityEndTime)}</td>
        <td>${order.registerRower}</td>
        <td>${order.approvedRequest? 'Approved':'appointed'}</td>
        <td>${order.boat}</td>
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