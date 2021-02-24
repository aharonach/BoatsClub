document.querySelector('.nav-link-add-message').addEventListener("click", e=> {
    e.preventDefault();
    const form = new Form({
        id: "add-message",
        method: "post",
        fields: [
            {
                id: "message",
                type: "textarea",
                label: "Enter your global message to send to all rowers",
                required: true,
            },
        ],
        action: e.target.href,
    });
    putContent("Add Message", form.getHtml());
    submitForm("add-message", "");
});

/**
 * Delete global message
 */
document.addEventListener('click', e => {
    const el = e.target;
    if (el.tagName === 'BUTTON' && el.classList.contains("delete-message")) {
        e.preventDefault();
        ajaxRequest("notifications", "post", {delete: el.dataset.index}).then(() => {
            el.closest('div').remove();
        });
    }
});