const form = document.querySelector(".needs-validation");

async function validatePassword() {
    const password = document.getElementById("password");
    const confirm = document.getElementById("confirm");

    const data = {
        password: password.value,
        confirm: confirm.value
    };

    const response = await fetch('/user/check-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    const match = await response.json();

    if (!match) {
        confirm.setCustomValidity('Las contraseñas no coinciden');
        return false;
    }

    password.setCustomValidity('');
    confirm.setCustomValidity('');
    return true;
}

async function addValidation() {
    "use strict";
    form.addEventListener(
        "submit",
        async (event) => {
            event.preventDefault();
            event.stopPropagation();

            if (form.checkValidity()) {
                if (await validatePassword()) {
                    form.classList.add("was-validated");
                    form.submit();
                }
            } else {
                form.classList.add("was-validated");
            }
        },
        false
    );
}
window.addEventListener("load", async () => addValidation());