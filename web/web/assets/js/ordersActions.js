const mergeOrdersFields = [
    {
        id: "order1",
        type: "select",
        label: "Order #1",
        options: [
            {ajax: "orders", valueField: "id", labelField: "id"}
        ],
    },
    {
        id: "order2",
        type: "select",
        label: "Order #2",
        options: [
            {ajax: "orders", valueField: "id", labelField: "id"}
        ],
    },
];

const appointOrderFields = [
    {
        id: "boat",
        type: "select",
        label: "Select a boat to appoint",
        options: [
            {ajax: "boats?non-private-non-disabled", valueField: "id", labelField: "name+typeName"}
        ],
    },
];

document.addEventListener('click', event => {
    const el = event.target;
    if (el.tagName === 'A' && el.href.includes('/appoint')) {
        event.preventDefault();
        const entity = el.dataset.entity,
            entityId = el.dataset.id;

        prepareOptions(appointOrderFields).then(fields => {
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

