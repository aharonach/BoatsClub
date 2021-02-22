const rowersFormFields = [
    {
        id: "name",
        type: "text",
        label: "Name",
        required: true,
        value: "",
    },
    {
        id: "emailAddress",
        type: "email",
        label: "Email",
        required: true,
    },
    {
        id: "password",
        type: "password",
        label: "Password",
        required: true,
    },
    {
        id: "phoneNumber",
        type: "tel",
        label: "Phone Number",
        required: true,
        placeholder: "052-1111111",
        pattern: "[0-9]{3}-[0-9]{7}",
    },
    {
        id: "notes",
        type: "textarea",
        label: "Notes",
        required: false,
    },
];

function getRowersColumns() {
    return {
        id: "Id",
        name: "Name",
        age: "Age",
        emailAddress: "Email",
        phoneNumber: "Phone",
        level: "Level",
        joined: "Joined At",
        expired: "Membership Expired At",
        notes: "Notes"
    };
}

function getRowersActions(rowerId) {
    return `<span class="actions d-block">${editLink("rowers", rowerId)}</span>`;
}

function rowerRow(rower, showActions = false) {
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${rower.id} ${getRowersActions(rower.id)}</span></td>`;
    } else {
        row += `<td>${rower.id}</td>`;
    }

    row += `<td>${rower.name}</td>
        <td>${rower.age}</td>
        <td>${rower.emailAddress}</td>
        <td>${rower.phoneNumber}</td>
        <td>${rower.level}</td>
        <td>${formatDateTime(rower.joined)} </td>
        <td>${formatDateTime(rower.expired)}</td>
        <td>${rower.notes}</td>
    </tr>`;

    return row;
}

document.querySelector('#rowers').addEventListener("click", function(e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function(response) {
        let table = createTable('rowers-list', getRowersColumns(), response, rowerRow, true, 'rowers');
        putContent("My Profile", table);
    });
});