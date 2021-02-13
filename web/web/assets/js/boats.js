function getBoatsColumns() {
    return {
        id: "Id",
        name: "Name",
        type: "Type",
        isCoastal: "is Coastal",
        isDisabled: "is Disabled",
        isPrivate: "is Private",
        isWide: "is Wide",
    };
}

function getBoatActions() {
    return `<span class="actions d-block"><a class="edit-link" href="#">Edit</a> | <a class="delete-link text-danger" href="#">Delete</a>`;
}

function boatRow(boat, showActions = false) {
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${boat.id} ${getBoatActions()}</span></td>`;
    } else {
        row += `<td>${boat.id}</td>`;
    }

    row += `<td>${boat.name}</td>
        <td>${boat.type}</td>
        <td>${boat.isCoastal}</td>
        <td>${boat.isDisabled}</td>
        <td>${boat.isPrivate}</td>
        <td>${boat.isWide}</td>
    </tr>`;

    return row;
}

document.querySelector('#boats').addEventListener("click", function(e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function(response) {
        let table = createTable('boats-list', getBoatsColumns(), response, boatRow, true );
        putContent("Boats", table);
    });
})
