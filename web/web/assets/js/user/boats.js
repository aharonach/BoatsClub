const boatsFormFields = [
    {
        id: "name",
        type: "text",
        label: "Name",
        required: true,
        value: "",
        disabled: true,
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
        disabled: true,
    },
    {
        id: "isCoastal",
        type: "checkbox",
        label: "Coastal",
        selected: false,
        disabled: true,
    },
    {
        id: "isWide",
        type: "checkbox",
        label: "Wide",
        selected: false,
        disabled: true,
    },
];