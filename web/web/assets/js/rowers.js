
function getRowersColumns() {
    return {
        id: "Id",
        name: "Name",
        age: "Age",
        emailAddress: "EmailAddress",
        phoneNumber: "PhoneNumber",
        level: "Level",
        hasPrivateBoat: "Has private boat",
        joined: "Joined at",
        expired: "Membership expired at",
        isManager: "Manager",
        notes: "Notes"
    };
}

function getRowersActions() {
    return `<span class="actions d-block"><a class="edit-link" href="#">Edit</a> | <a class="delete-link text-danger" href="#">Delete</a>`;
}


function rowerRow(rower, showActions = false) {
    console.log(rower);
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${rower.id} ${getRowersActions()}</span></td>`;
    } else {
        row += `<td>${rower.id}</td>`;
    }
        row += `<td>${rower.name}</td>
        <td>${rower.age}</td>
        <td>${rower.emailAddress}</td>
        <td>${rower.phoneNumber}</td>
        <td>${rower.level}</td>
        <td>${booleanFeather(rower.hasPrivateBoat)}</td>
        <td>${formatDateTime(rower.joined)} </td>
        <td>${formatDateTime(rower.expired)}</td>
        <td>${booleanFeather(rower.isManager)}</td>
        <td>${rower.notes}</td>
    </tr>`;

    return row;
}

document.querySelector('#rowers').addEventListener("click", function(e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function(response) {
        let table = createTable('rowers-list', getRowersColumns(), response, rowerRow, true, 'rowers');
        putContent("Rowers", table);
    });
})