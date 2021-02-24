const activitiesFormFields = [
    {
        id: "title",
        type: "text",
        label: "Title",
        required: true,
        value: "",
    },
    {
        id: "startTime",
        type: "time",
        label: "Start",
        value: "",
        pattern: "([1]?[0-9]|2[0-3]):[0-5][0-9]",
    },
    {
        id: "endTime",
        type: "time",
        label: "Finish",
        value: "",
        pattern: "([1]?[0-9]|2[0-3]):[0-5][0-9]",
    },
    {
        id: "boatType",
        type: "radio",
        label: "Boat type (Optional)",
        required: false,
        options: [
            {value: "-1", selected: true, label: "None"},
            {value: "Single", selected: false, label: "Single"},
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
];

function getActivitiesColumns() {
    return {
        id: "Id",
        title: "Title",
        time: "Time",
        boatType: "Boat type",
    };
}
function getActivitiesActions(activityId) {
    return `<span class="actions d-block">${editLink("activities", activityId)} | ${deleteLink("activities", activityId)}</span>`;
}

function activityRow(activity, showActions = false)
{
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${activity.id} ${getActivitiesActions(activity.id)}</span></td>`;
    } else {
        row += `<td>${activity.id}</td>`;
    }

    row += `<td>${activity.title}</td>
        <td>${formatTime(activity.startTime) + ' - ' + formatTime(activity.endTime)}</td>
        <td>${activity.boatType ? activity.boatType : ''}</td>
        </tr>`;

    return row;
}


document.querySelector('#activities').addEventListener("click", function (e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function (response) {
        let table = createTable('activities-list', getActivitiesColumns(), response, activityRow, true, 'activities');
        putContent("Activities", table, toolbarImportExport("activities"));
    });
})

document.querySelector('#activities-add').addEventListener("click", function(e) {
    e.preventDefault();
    const form = new Form({
        id: "add-activity",
        method: "post",
        fields: activitiesFormFields,
        action: e.target.href,
    });
    putContent("Add New activity", form.getHtml(), toolbarImportExport("activities"));
    submitForm("add-activity", "activities");
});
