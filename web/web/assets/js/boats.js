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

document.querySelector('#boats').addEventListener("click", function(e) {
    e.preventDefault();
    ajaxRequest(e.target.href).then(function(response) {
        console.log(response);
        let table = createTableRows(response, createTableHeader(getBoatsColumns()), getBoatsColumns());
        putContent(table);
    });
})
