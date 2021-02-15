function getActivitiesColumns() {
    return {
        id: "Id",
        title: "Title",
        time: "Time",
        boatType: "Boat type",
    };
}
    function getActivitiesActions() {
        return `<span class="actions d-block"><a class="edit-link" href="#">Edit</a> | <a class="delete-link text-danger" href="#">Delete</a>`;
    }

    function activityRow(activity, showActions = false)
    {
        let row = `<tr>`;

        if (showActions) {
            row += `<td>${activity.id} ${getActivitiesActions()}</span></td>`;
        } else {
            row += `<td>${activity.id}</td>`;
        }

        row += `<td>${activity.title}</td>
        <td>${formatTime(activity.startTime) + ' - ' + formatTime(activity.endTime)}</td>
        <td>${activity.boatType? activity.boatType:''}</td>
        </tr>`;

        return row;
    }


    document.querySelector('#activities').addEventListener("click", function (e) {
        e.preventDefault();
        ajaxRequest(e.target.href).then(function (response) {
            console.log(response);
            let table = createTable('activities-list', getActivitiesColumns(), response, activityRow, true, 'activities');
            putContent("Activities", table);
        });
    })
