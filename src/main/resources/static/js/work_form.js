function addValidation() {
    (() => {
        "use strict";

        document.querySelector(".needs-validation").addEventListener(
            "submit",
            (event) => {
                if (!event.target.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                event.target.classList.add("was-validated");
            },
            false
        );
    })();
}


window.addEventListener("load", () => addValidation());