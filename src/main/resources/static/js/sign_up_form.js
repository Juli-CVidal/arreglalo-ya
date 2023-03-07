const ACCOUNT_SELECT = document.getElementById("account-select");
const previousType = document.getElementById("accountType");
const form = document.querySelector(".needs-validation");
const SUPPLIER_DIV = document.getElementById("supplier")
const SUPPLIER_FIELDS = SUPPLIER_DIV.cloneNode(true);
const CUSTOMER_DIV = document.getElementById("customer")
const CUSTOMER_FIELDS = CUSTOMER_DIV.cloneNode(true);

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



function enterFields(type) {
	if (type === 'SUPPLIER') {
		CUSTOMER_DIV.innerHTML = '';
		SUPPLIER_DIV.innerHTML = SUPPLIER_FIELDS.innerHTML;
	} else if (type === 'USER') {
		SUPPLIER_DIV.innerHTML = '';
		CUSTOMER_DIV.innerHTML = CUSTOMER_FIELDS.innerHTML;
	}
	form.setAttribute("action", "/signup/" + type.toLowerCase());
}


function detectSelect() {
	ACCOUNT_SELECT.addEventListener("change", (event) => {
		enterFields(event.target.value);
	});
}

function initFields() {
	SUPPLIER_DIV.classList.remove("d-none");
	CUSTOMER_DIV.classList.remove("d-none");
	SUPPLIER_DIV.innerHTML = "";
	CUSTOMER_DIV.innerHTML = "";

}

function checkPreviousType() {
	const element = window.location.href.split("/").slice(-1)[0];
	if (element) {
		enterFields(element.toUpperCase());
		ACCOUNT_SELECT.value = element.toUpperCase();
	}
}

function init() {
	initFields();
	checkPreviousType();
	detectSelect();
	addValidation();
}

init();