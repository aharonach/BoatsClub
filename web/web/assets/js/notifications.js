document.getElementById("openNotifications").addEventListener("click", e=>{
   e.preventDefault();
   const el = document.getElementById("notificationsBox");
   if (el.classList.contains("show")) {
       el.classList.remove("show");
   } else {
       el.classList.add("show");
   }
});

const notification = (message) => {
    return `<div class="admin-message alert alert-primary">
                        
                        <p>${message}</p>
                    </div>`;
}

setInterval(e => {
    ajaxRequest("notifications").then(response => {
        if (!response) {
            return;
        }

        console.log(response);

        if (response instanceof Array) {
            let html = "";
            for (let message of response) {
                html += notification(message);
            }
            const list = document.getElementById("notificationList");

            if (html) {
                list.innerHTML = html;
            } else {
                list.innerHTML = "No notifications";
            }
            document.querySelector("#openNotifications .badge").innerHTML = response.length ? response.length : "";
        }
    });
}, 5000);