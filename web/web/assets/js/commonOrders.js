
document.querySelector('#orders-add').addEventListener("click", function(e) {
    e.preventDefault();
    prepareOptions(ordersFormFields).then((fields) => {
        const form = new Form({
            id: "add-order",
            method: "post",
            fields: fields,
            action: e.target.href,
        });
        putContent("Add New Order", form.getHtml());
        activityFieldChange(document.getElementById("wantedActivity"));
        submitForm("add-order", "orders");
    });
});



/**
 *
 * Handle Activities in order forms
 *
 */

const activityFieldEvent = e => {
    const element = e.target;
    if (element.tagName === 'SELECT' && element.id === 'wantedActivity') {
        activityFieldChange(element);
    }
};

document.addEventListener("change", activityFieldEvent);
document.addEventListener("click", activityFieldEvent);

const activityFieldRemove = () => {
    const selectEl = document.getElementById("wantedActivity"),
        activityTitle = document.getElementById('activityTitle'),
        activityStartTime = document.getElementById('activityStartTime'),
        activityEndTime = document.getElementById('activityEndTime');

    if (selectEl && !selectEl.querySelector('option')) {
        selectEl.closest('.form-group').remove();
        activityTitle.readOnly = activityStartTime.readOnly = activityEndTime.readOnly = false;
        activityTitle.required = activityStartTime.required = activityEndTime.required = true;
        return true;
    }
    return false;
}

const activityFieldChange = selectEl => {
    if (!selectEl) {
        return;
    }

    const selected = selectEl.value,
        activityTitle = document.getElementById('activityTitle'),
        activityStartTime = document.getElementById('activityStartTime'),
        activityEndTime = document.getElementById('activityEndTime');

    activityFieldRemove();

    if (selected) {
        ajaxRequest('activities', 'get', {id: selected}).then(response => {
            activityTitle.value = response.title;
            activityStartTime.value = formatTime(response.startTime);
            activityEndTime.value = formatTime(response.endTime);
            activityTitle.readOnly = activityStartTime.readOnly = activityEndTime.readOnly = true;
            activityTitle.required = activityStartTime.required = activityEndTime.required = false;
        });
    } else {
        activityTitle.value = activityStartTime.value = activityEndTime.value = "";
        activityTitle.readOnly = activityStartTime.readOnly = activityEndTime.readOnly = false;
        activityTitle.required = activityStartTime.required = activityEndTime.required = true;
    }
};