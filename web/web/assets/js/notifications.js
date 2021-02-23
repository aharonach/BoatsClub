const notification = (message, title= "", type = "primary", showDelete = false, index = 0) => {
    const deleteButton = showDelete ? `<button type="button" class="delete-message close" data-index="${index}" data-dismiss="alert" aria-label="Close">×</button>` : "";
    const h6 = title ? `<h6 class="alert-heading">${title}</h6>` : "";
    const notification = `<div class="admin-message alert-dismissible alert alert-${type}">${h6} ${message} ${deleteButton}</div>`;
    return notification;
}

const refreshAdminNotifications = notifications => {
    let html = `<h2 class="h5">Admin Messages</h2>`;

    for (let i = 0; i < notifications.length; i++) {
        let message = notifications[i];
        html += notification(message.message, "", "primary", getCookie("isAdmin") === "true", i);
    }

    html = `<div id="dashboard-list">${html}</div>`;

    putContent("Dashboard", html);
}

document.getElementById("openNotifications").addEventListener("click", e=>{
    e.preventDefault();
    const el = document.getElementById("notificationsBox");
    if (el.classList.contains("show")) {
        el.classList.remove("show");
    } else {
        el.classList.add("show");
    }
});

document.querySelectorAll(".nav-link-dashboard").forEach(navLink => {
    navLink.addEventListener("click", e => {
        e.preventDefault();
        ajaxRequest("notifications", "get", {admin: true}).then(refreshAdminNotifications);
    });
});

/**
 * Manual Notifications
 */
const manualNotifications = () => {
    if (!document.getElementById("dashboard-list")) {
        return;
    }

    ajaxRequest("notifications", "get", {admin: true}).then(refreshAdminNotifications);
};

manualNotifications();

setInterval(manualNotifications, 2500);

/**
 * Automatic Notifications
 */

const automaticNotifications = e => {
    ajaxRequest("notifications").then(response => {
        if (!response) {
            return;
        }

        if (response instanceof Array) {
            let html = "";
            const list = document.getElementById("notificationList");

            for (let message of response) {
                let text = message.message.split(";").join("<br />");
                html += notification(text, editLink("order", message.orderId, "Order #" + message.orderId), "secondary");
            }

            if (html) {
                list.insertAdjacentHTML("afterbegin", html);
            }

            document.querySelector("#openNotifications .badge").innerHTML = response.length ? response.length : "";
        }
    });
}

automaticNotifications();

setInterval( automaticNotifications, 2500);