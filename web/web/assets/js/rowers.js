const rowersFormFields = [
    {
        id: "name",
        type: "text",
        label: "Name",
        required: true,
        value: "",
    },
    {
        id: "age",
        type: "number",
        label: "Age",
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
    },
    {
        id: "level",
        type: "radio",
        label: "Level",
        required: true,
        options: [
            {value: "BEGINNER", selected: true, label: "Beginner"},
            {value: "INTERMEDIATE", selected: false, label: "Intermediate"},
            {value: "ADVANCED", selected: false, label: "Advanced"},
        ],
    },
    {
        id: "hasPrivateBoat",
        type: "checkbox",
        label: "Has Private Boat",
        selected: false,
    },
    {
        id: "privateBoat",
        type: "select",
        options: [
            {ajax: "boats", valueField: "id", labelField: "name"}
        ],
        label: "Private Boat",
        required: false,
    },
    {
        id: "isManager",
        type: "checkbox",
        label: "Manager",
        selected: false,
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
        hasPrivateBoat: "Private Boat",
        joined: "Joined At",
        expired: "Membership Expired At",
        isManager: "Manager",
        notes: "Notes"
    };
}

function getRowersActions(rowerId) {
    return `<span class="actions d-block">${editLink("rowers", rowerId)} | ${deleteLink("rowers", rowerId)}</span>`;
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
        <td>${booleanFeather(rower.hasPrivateBoat)} ${rower.privateBoat ? editLink("boats", rower.privateBoat, rower.privateBoat) : ""}</td>
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
});

document.querySelector('#rowers-add').addEventListener("click", function(e) {
    e.preventDefault();
    prepareOptions(rowersFormFields).then(() => {
        const form = new Form({
            id: "add-rower",
            method: "post",
            fields: rowersFormFields,
            action: e.target.href,
        });
        putContent("Add New Rower", form.getHtml());
        submitForm("add-rower", "rowers");
    });
});