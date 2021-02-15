
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
        isManager: "Is manager",
        notes: "Notes"
    };
}

function getRowersActions() {
    return `<span class="actions d-block"><a class="edit-link" href="#">Edit</a> | <a class="delete-link text-danger" href="#">Delete</a>`;
}

function localDateTimeReplacer(key,value)
{
    if (key=="nano") return undefined; //remove nano seconds.
    else return value;
}

function rowerRow(rower, showActions = false) {
    let row = `<tr>`;

    if (showActions) {
        row += `<td>${rower.id} ${getRowersActions()}</span></td>`;
    } else {
        row += `<td>${rower.id}</td>`;
    }

    let startTime = JSON.stringify(rower.joined.toString(), localDateTimeReplacer);
    let parsedStartTime = JSON.parse(startTime);
    console.log(parsedStartTime)
        row += `<td>${rower.name}</td>
        <td>${rower.age}</td>
        <td>${rower.emailAddress}</td>
        <td>${rower.phoneNumber}</td>
        <td>${rower.level}</td>
        <td>${rower.hasPrivateBoat}</td>
<!--        var jsonString = '{"some":"json"}';-->
<!--var jsonPretty = JSON.stringify(JSON.parse(jsonString),null,2); -->
        <td>${JSON.parse(JSON.stringify(rower.joined.toString(), localDateTimeReplacer))} </td>
        <td>${JSON.stringify(rower.expired, localDateTimeReplacer)}</td>

        <td>${rower.isManager}</td>
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