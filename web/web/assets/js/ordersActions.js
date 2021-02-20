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