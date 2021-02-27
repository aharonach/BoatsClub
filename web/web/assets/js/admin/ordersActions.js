const mergeOrdersFields = [
    {
        id: "order1",
        type: "select",
        label: "Order #1",
        options: [
            {ajax: "orders", valueField: "id", labelField: "id"}
        ],
        required: true,
    },
    {
        id: "order2",
        type: "select",
        label: "Order #2",
        options: [
            {ajax: "orders", valueField: "id", labelField: "id"}
        ],
        required: true,
    },
];

const appointOrderFields = [
    {
        id: "boat",
        type: "select",
        label: "Select a boat to appoint",
        options: [
            {ajax: "boats?filterBy=types&types=", valueField: "id", labelField: "name+typeName"}
        ],
        required: true,
    },
];

document.addEventListener('click', event => {
    const el = event.target;
    if (el.tagName === 'A' && el.href.includes('/appoint')) {
        event.preventDefault();
        const entity = el.dataset.entity,
            entityId = el.dataset.id,
            boatTypes = el.dataset.boatTypes;

        const newFormFields = JSON.parse(JSON.stringify(appointOrderFields));
        newFormFields[0].options[0].ajax += boatTypes;

        prepareOptions(newFormFields).then(fields => {
            const form = new Form({
                id: "appoint-order",
                method: 'post',
                fields: prepareFormFields({id: entityId}, fields),
                action: el.href,
            });
            showPopup("appoint-order-popup","Appoint Order ID " + entityId, form.getHtml() );
            submitForm("appoint-order", entity);
        });
    }
});

