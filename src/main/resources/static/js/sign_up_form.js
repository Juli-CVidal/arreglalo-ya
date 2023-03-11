const form = document.querySelector(".needs-validation");

function validatePassword() {
	const password = document.getElementById("password");
	const repeat = document.getElementById("repeat");

	if (password.value.lenght < 8) {
		password.setCustomValidity(
			"La contraseña debe tener al menos ocho caracteres"
		);
		return false;
	}
	if (password.value !== repeat.value) {
		repeat.setCustomValidity("Las contraseñas no coinciden");
		return false;
	}

	password.setCustomValidity("");
	repeat.setCustomValidity("");
	return true;
}

function addValidation() {
	(() => {
		"use strict";
		form.addEventListener(
			"submit",
			(event) => {
				if (!form.checkValidity()) {
					event.preventDefault();
					event.stopPropagation();
				}
				if (!validatePassword()) {
					event.preventDefault();
				}

				form.classList.add("was-validated");
			},
			false
		);
	})();
}
window.addEventListener("load", () => addValidation());