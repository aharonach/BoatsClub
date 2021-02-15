
function getOrdersColumns() {
    return {
        id: "Id",
        rowers: "Registered rowers",
        boatTypes: "Boat types",
        activityDate: "activityDate",
        activityStartTime: "Activity start time",
        activityEndTime: "Activity end time",
        registeredRower: "Added by"
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

    row += `<td>${order.id}</td>
        <td>${order.rowers}</td>
        <td>${order.boatTypes}</td>
        <td>${order.activityDate}</td>
        <td>${order.activityStartTime}</td>
        <td>${order.activityEndTime}</td>
        <td>${order.registeredRower}</td>
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