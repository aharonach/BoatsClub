/**
 * field: {
 *     name: "field-name",
 *     label: "Field Label",
 *     type: "text",
 *     value: "value",
 *     required: false,
 *     options: [
 *         {
 *              label: "Four",
 *             value: "Four",
 *             selected: false,
 *         },
 *         {
 *             value: "Eight",
 *             selected: true,
 *         }
 *     ],
 * }
 */

function getFormFields(entity) {
    switch (entity) {
        case "boats":
            return boatsFormFields;
        case "activities":
            return activitiesFormFields;
        case "rowers":
            return rowersFormFields;
        case "orders":
            return ordersFormFields;
    }
}

function prepareFormFields(record, formFields) {
    // clone fields
    const newFormFields = JSON.parse(JSON.stringify(formFields));

    for (let field of newFormFields) {
        let value = record[field.id];
        if (value) {
            switch (field.type) {
                case "select":
                    if (field.multiple) {
                        field.options[0].selected = !field.options[0].value;
                        for (const option of field.options) {
                            for (const selectedValue of value) {
                                if (option.value === selectedValue) {
                                    option.selected = true;
                                }
                            }
                        }
                    } else {
                        for (const option of field.options) {
                            if (option.value === value) {
                                option.selected = true;
                            }
                        }
                    }
                    break;
                case "radio":
                    value = value.split(',');
                    for (const option of field.options) {
                        for (const selectedValue of value) {
                            if (option.value === selectedValue) {
                                option.selected = true;
                            }
                        }
                    }
                    break;
                case "checkbox":
                    field.selected = value;
                    break;
                case "password":
                    field.required = false;
                    break;
                case "time":
                    field.value = formatTime(value);
                    break;
                case "date":
                    field.value = formatDate(value, true);
                    break;
                default:
                    field.value = value;
                    break;
            }
        } else {
            switch (field.type) {
                // case "select":
                //     if (field.includeEmpty) {
                //         field.options.unshift({value: "", label: "Select...", selected: true});
                //     }
                //     break;
                case "password":
                    field.required = false;
                    break;
            }
        }
    }

    newFormFields.push({
        id: 'id',
        type: 'hidden',
        value: record.id,
    });

    return newFormFields;
}

async function prepareOptions(fields) {
    const newFormFields = JSON.parse(JSON.stringify(fields));
    for (const field of newFormFields) {
        if ((field.type === 'select' || field.type === 'radio') && field.options[0].ajax) {
            const valueField = field.options[0].valueField,
                labelField = field.options[0].labelField.split("+");

            const json = await ajaxRequest(field.options[0].ajax);
            field.options = [];

            for (const record of json) {
                let finalLabel = [];
                for (const label of labelField) {
                    finalLabel.push(record[label]);
                }

                finalLabel = finalLabel.join(": ");

                field.options.push({
                    value: record[valueField],
                    label: finalLabel,
                    selected: false
                });
            }

            if (!field.includeEmpty && field.options[0]) {
                field.options[0].selected = true;
            }
        }
    }
    return newFormFields;
}

class Form {
    constructor(args) {
        this.id = args.id;
        this.method = args.method;
        this.action = args.action;
        this.fields = args.fields;

        let onSubmit = args.onsubmit ? `onsubmit="${args.onsubmit}()"` : '';
        let submitLabel = args.submitLabel ? args.submitLabel : "Submit";

        let form = `<form id="${this.id}" name="${this.id}" method="${this.method}" action="${this.action}" ${onSubmit}>`;
        for (let field of this.fields) {
            switch (field.type) {
                case "select":
                    form += this.selectField(field);
                    break;
                case "checkbox":
                    form += this.checkboxField(field);
                    break;
                case "radio":
                    form += this.radioField(field);
                    break;
                case "radio-choices":
                    form += this.radioFieldMultipleChoice(field);
                    break;
                case "textarea":
                    form += this.textareaField(field);
                    break;
                default: // text, password, email, number, tel
                    form += this.defaultField(field);
                    break;
            }
        }
        form += this.submit(submitLabel);
        form += `</form>`;
        this.form = form;
    }

    selectField(args) {
        let field = this.label(args.id, args.label);
        field += `<select class="form-control" id="${args.id}" name="${args.id}" ${args.multiple ? "multiple" : ""}>`;
        // if (args.includeEmpty) {
        //     field += `<option value="" selected>Select...</option>`;
        // }
        for (let option of args.options) {
            field += `<option value="${option.value}" ${option.selected ? "selected" : ""}>${option.label}</option>`;
        }
        field += `</select>`;
        return this.formGroupWrap(field);
    }

    checkboxField(args) {
        let field = `<input type="checkbox" class="form-check-input" id="${args.id}" value="true" name="${args.id}" ${args.selected ? "checked" : ""}> <label class="form-check-label" for="${args.id}">${args.label}</label>`;
        return this.formGroupWrap(this.formCheckWrap(field));
    }

    radioField(args) {
        let field = this.label("", args.label) + `<div class="w-100"></div>`,
            i = 1;

        for (let option of args.options) {
            let radio = `<input type="radio" class="form-check-input" id="${args.id + "-" + option.value}" name="${args.id}" value="${option.value}" ${option.selected ? "checked" : ""} ${this.required(args)}> <label class="form-check-label" for="${args.id + "-" + option.value}">${option.label}</label>`;
            field += this.formCheckWrap(radio);
            i++;
        }
        return this.formGroupWrap(field);
    }

    radioFieldMultipleChoice(args) {
        let field = this.label("", args.label) + `<div class="w-100"></div>`,
            i = 1;

        for (let option of args.options) {
            let radio = `<input type="radio" class="form-check-input" id="${args.id + "-" + option.value}" name="action" value="${option.value}" ${option.selected ? "checked" : ""} ${this.required(args)}> <label class="form-check-label" for="${args.id + "-" + option.value}">${option.label}</label>`;
            field += this.formCheckWrap(radio);
            i++;
        }
        return this.formGroupWrap(field);
    }

    textareaField(args) {
        let field = this.label(args.id, args.label);
        const value = args.value ? args.value : '';
        field += `<textarea class="form-control" id="${args.id}" name="${args.id}" ${this.required(args)}>${value}</textarea>`;
        return this.formGroupWrap(field);
    }

    defaultField(args) {
        let field = args.label ? this.label(args.id, args.label) : '';
        const value = args.value ? args.value : '';
        field += `<input ${args.readonly ? "readonly" : ""} class="form-control" type="${args.type}" ${args.pattern ? "pattern=\"" + args.pattern + "\"" : ""} value="${value}" id="${args.id}" name="${args.id}" placeholder="${args.placeholder ? args.placeholder : ""}" ${this.required(args)}>`;
        return this.formGroupWrap(field);
    }

    label(id, text) {
        return `<label for="${id}">${text}</label>`;
    }

    formGroupWrap(html) {
        return `<div class="form-group">${html}</div>`;
    }

    formCheckWrap(html) {
        return `<div class="form-check form-check-inline">${html}</div>`;
    }

    required(args) {
        return typeof(args.required) !== 'undefined' && args.required ? "required" : "";
    }

    getHtml() {
        return this.form;
    }

    submit(label) {
        return `<button class="btn btn-primary" type="submit">${label}</button>`;
    }
}