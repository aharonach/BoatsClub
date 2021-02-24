document.addEventListener("click", e => {
    const element = e.target;
    if (element.tagName === 'A' && element.classList.contains("import-link")) {
        e.preventDefault();
        const entity = element.dataset.entity;
        importForm(entity);
    }
});

const toolbarImportExport = entity => {
    return `<div class="btn-group text-capitalize mr-2">
        <a href="#" class="btn btn-sm btn-outline-secondary import-link" data-entity="${entity}">Import ${entity}</a>
        <a href="export?entity=${entity}" target="_blank" class="btn btn-sm btn-outline-secondary export-link" data-entity="${entity}">Export ${entity}</a>
    </div>`;
}

const importForm = entity => {
    const formId = "import-" + entity;
    let formFields = [
        {
            id: "entity",
            type: "hidden",
            value: entity,
            required: true,
        },
        {
            id: "uploadFile",
            label: "Select a XML file",
            type: "file",
            required: true,
        },
    ];

    if (entity !== 'activities') {
        formFields.push({
            id: "override-records",
            label: "Override records",
            type: "checkbox",
            checked: false,
        });
    }

    const form = new Form({
        id: formId,
        method: "post",
        action: "import",
        multipart: true,
        fields: formFields
    });

    showPopup("import-popup", "Import " + entity, form.getHtml() );
    submitForm(formId, entity, true);
};