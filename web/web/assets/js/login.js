function login() {
    ajaxRequest('login', 'post', getFormData('login-form')).then( (response) => {
        if (response.status) {
            showAlert('success', response.value);
            setTimeout(function() {
                window.location = ROOT_PATH;
            }, 1000);
        } else {
            showAlert('error', response.error);
        }
    });
}

document.forms.namedItem('login-form').addEventListener("submit", (e) => {
    e.preventDefault();
    login();
});