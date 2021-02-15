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

class Form {
    constructor(id, method, fields, submitLabel = "Submit") {
        this.id = id;
        this.method = method;
        this.fields = fields;

        let form = `<form id="${this.id}" name="${this.id}" method="${this.method}">`;
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
                case "textarea":
                    form += this.textareaField(field);
                    break;
                default: // text, password, email, number
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
        field += `<select class="form-control" id="${args.id}" ${args.multiple ? "multiple" : ""}>`;
        for (let option of args.options) {
            field += `<option value="${option.value}" ${option.selected ? "selected" : ""}>${option.label}</option>`;
        }
        field += `</select>`;
        return this.formGroupWrap(field);
    }

    checkboxField(args) {
        let field = `<input type="checkbox" class="form-check-input" id="${args.id}" name="${args.id}"> <label class="form-check-label" for="${args.id}">${args.label}</label>`;
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

    textareaField(args) {
        let field = this.label(args.id, args.label);
        field += `<textarea class="form-control" id="${args.id}" name="${args.id}" ${this.required(args)}>${args.value}</textarea>`;
        return this.formGroupWrap(field);
    }

    defaultField(args) {
        let field = this.label(args.id, args.label);
        field += `<input class="form-control" type="${args.type}" value="${args.value}" id="${args.id}" name="${args.id}" ${this.required(args)}>`;
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