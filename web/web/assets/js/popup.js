const popupContent = (id, title, content) => {
    return `
<div id="${id}" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-capitalize">${title}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-body">${content}</div>
        </div>
    </div>
</div>`;
};

const showPopup = (id, title, content) => {
    document.body.insertAdjacentHTML('beforeend', popupContent(id, title, content));

    document.body.classList.add('modal-open');

    let popup = document.getElementById(id);
    let container = popup.querySelector('.modal-content');

    popup.classList.add('show');

    popup.querySelector("button.close").addEventListener("click", () => {
        hidePopup(popup);
    });

    document.querySelector(".modal").addEventListener("click", (e) => {
        if (e.target !== popup && e.target !== container) return;
        hidePopup(popup);
    });
};

const hidePopup = (popup) => {
    popup.parentNode.removeChild(popup);
    document.body.classList.remove('modal-open');
}



