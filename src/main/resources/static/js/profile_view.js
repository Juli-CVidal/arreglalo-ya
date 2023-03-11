const reviewsContainer = new Swiper(".reviews__container", {
    slidesPerView: "auto",
    loop: false,
    centeredSlides: true,
    spaceBetween: 25,
    navigation: {
        nextEl: ".swiper-button-next",
        prevEl: ".swiper-button-prev",
    },
});

//EXPAND BTNS
const reviewBtns = document.querySelectorAll(".btn.btn-outline-dark");

{
    /* <div class="card swiper-slide rounded-5 w-75">
  <div class="rounded-top">
    <div class="row p-3 pb-0"> PARENT
      <div class="col-6">
        <div class="review__stars d-flex">
        </div>
      </div>
      <div class="col-6 d-flex align-items-start m-0 p-0"> PARENT
        <button class="btn btn-outline-dark d-block me-2">Expandir</button>   BUTTON
      </div>
    </div>
  </div>
  <article class="card-body"> NEXTELEMENTSIBLING
    Increíble el trabajo, muchas gracias por venir!
  </article>
  </div> */
}

reviewBtns?.forEach((button) => {
    button.addEventListener("click", () => {
        const article =
            button.parentElement.parentElement.parentElement.nextElementSibling;
        article.classList.toggle("expanded");
        button.innerText = "Expandir" == button.innerText ? "Contraer" : "Expandir";
    });
});

//SWEET ALERT
function addFormListeners() {
    const reportForm = document.getElementById("report");
    if (reportForm) {
        addListeners(reportForm)
    }
    document.querySelectorAll(".censor").forEach((form) => addListeners(form));
}

function launchAlert(event) {
    Swal.fire({
        title: "Estás seguro?",
        text: "Esta acción no se puede deshacer",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#d33",
        cancelButtonColor: "#3085d6",
        confirmButtonText: "Censurar",
        cancelButtonText: "Cancelar",
    }).then((result) => {
        if (result.isConfirmed) {
            event.target.submit();
        }
    });
}

function addListeners(form) {
    form.addEventListener("submit", (event) => {
        event.preventDefault();
        launchAlert(event);
    });
}


//RELATED TO WORKS
function askPrice(form) {
    const priceInput = document.createElement("input");
    priceInput.setAttribute("type", "hidden");
    priceInput.setAttribute("name", "price");
    priceInput.setAttribute("id", "price");
    form.appendChild(priceInput);

    const getPrice = () => {
        Swal.fire({
            title: 'Ingrese el precio',
            input: 'number',
            showCancelButton: true,
            confirmButtonText: 'Aceptar',
            cancelButtonText: 'Cancelar',
            inputValidator: (value) => {
                if (!value) {
                    return 'Ingrese un valor';
                }
                if (isNaN(value)) {
                    return 'Ingrese un valor numérico';
                }
                if (parseFloat(value) <= 0) {
                    return 'Ingrese un valor numérico y positivo';
                }
            }
        }).then((result) => {
            if (result.isConfirmed) {
                const value = parseFloat(result.value);
                priceInput.value = value;
                form.submit();
            }
        });
    }

    getPrice();
}


function addWorkListeners() {
    document.querySelectorAll(".work__select")?.forEach(select => {
        select.addEventListener("change", () => {
            const option = select.options[select.selectedIndex];
            const form = select.parentElement;
            form.setAttribute("action", `/work/${option.value.toLowerCase()}`)
            if (option.value == "setPrice") {
                askPrice(form);
                return
            }
            Swal.fire({
                title: `Seguro que quieres ${option.text.toLowerCase()}?`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Si',
                cancelButtonText: "Cancelar",
            }).then((result) => {
                if (result.isConfirmed) {
                    form.submit();
                }
            })
        })
    })
}


window.addEventListener("load", () => {
    addFormListeners();
    addWorkListeners();
})