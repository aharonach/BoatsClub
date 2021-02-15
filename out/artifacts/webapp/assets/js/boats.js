const formFields = [
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
];

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
        let table = createTable('boats-list', getBoatsColumns(), response, boatRow, true , 'boats');
        putContent("Boats", table);
    });
});

document.querySelector('#boats-add').addEventListener("click", function(e) {
    e.preventDefault();
    const form = new Form("add-boat", "post", formFields);
    putContent("Add New Boat", form.getHtml());
});
