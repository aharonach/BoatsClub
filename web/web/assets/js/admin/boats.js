const boatsFormFields = [
    {
        id: "name",
        type: "text",
        label: "Name",
        required: true,
        value: "",
    },
    {
        id: "type",
        type: "radio",
        label: "Boat Type",
        required: true,
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
        id: "isCoastal",
        type: "checkbox",
        label: "Coastal",
        selected: false,
    },
    {
        id: "isDisabled",
        type: "checkbox",
        label: "Disabled",
        selected: false,
    },
    {
        id: "isPrivate",
        type: "checkbox",
        label: "Private",
        selected: false,
    },
    {
        id: "isWide",
        type: "checkbox",
        label: "Wide",
        selected: false,
    },
];

function getBoatsColumns() {
    return {
        id: "Id",
        name: "Name",
        type: "Type",
        isWide: "Wide",
        isCoastal: "Coastal",
        isPrivate: "Private",
        isDisabled: "Disabled",
    };
}

function getBoatActions(boatId) {
    return `<span class="actions d-block">${editLink("boats", boatId)} | ${deleteLink("boats", boatId)}</span>`;
}

function boatRow(boat, showActions = false) {
    let row = `<tr data-id="${boat.id}">`;

    if (showActions) {
        row += `<td>${boat.id} ${getBoatActions(boat.id)}</span></td>`;
    } else {
        row += `<td>${boat.id}</td>`;
    }

    row += `<td>${boat.name}</td>
        <td>${boat.typeName}</td>
        <td>${booleanFeather(boat.isWide)}</td>
        <td>${booleanFeather(boat.isCoastal)}</td>
        <td>${booleanFeather(boat.isPrivate)}</td>
        <td>${booleanFeather(boat.isDisabled)}</td>
    </tr>`;

    return row;
}

document.querySelector('#boats').addEventListener("click", function(e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function(response) {
        let table = createTable('boats-list', getBoatsColumns(), response, boatRow, true , 'boats');
        putContent("Boats", table, toolbarImportExport("boats"));
    });
});

document.querySelector('#boats-add').addEventListener("click", function(e) {
    e.preventDefault();
    const form = new Form({
        id: "add-boat",
        method: "post",
        fields: boatsFormFields,
        action: e.target.href,
    });
    putContent("Add New Boat", form.getHtml(), toolbarImportExport("boats"));
    submitForm("add-boat", "boats");
});
