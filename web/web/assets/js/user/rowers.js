const rowersFormFields = [
    {
        id: "name",
        type: "text",
        label: "Name",
        required: true,
        value: "",
        disabled: true,
    },
    {
        id: "emailAddress",
        type: "email",
        label: "Email",
        required: true,
        disabled: true,
    },
    {
        id: "password",
        type: "password",
        label: "Password",
        required: true,
        disabled: true,
    },
    {
        id: "phoneNumber",
        type: "tel",
        label: "Phone Number",
        required: true,
        placeholder: "052-1111111",
        pattern: "[0-9]{3}-[0-9]{7}",
        disabled: true,
    },
];


document.querySelector('#my-profile').addEventListener("click", function(e) {
    e.preventDefault();
    // clone fields
    const newFormFields = JSON.parse(JSON.stringify(rowersFormFields));
    for(const field of newFormFields){
        field.disabled = false;
    }

    ajaxRequest(e.target.href).then(function(response) {
        response = response[0];
        const form = new Form({
            id: "my-profile-form",
            method: 'post',
            fields: prepareFormFields(response, newFormFields),
            action: e.target.href,
        })
        showPopup("my-profile-popup", "Edit My Profile", form.getHtml());
    });
});