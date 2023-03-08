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

function createStars(score) {
    const roundedScore = (Math.round(score * 2) / 2).toString().split(".");
    const stars = [];
    for (let index = 0; index < roundedScore[0]; index++) {
        stars.push(`<i class="ri-star-fill"></i>`);
    }
    if (roundedScore[1] != undefined) {
        stars.push(`<i class="ri-star-half-fill"></i>`);
    }
    return stars.join("");
}

const starsContainer = document.querySelectorAll(".review__stars");
starsContainer.forEach((container) => {
    const score = parseFloat(container.getAttribute("data-score"));
    const stars = createStars(score);
    container.innerHTML = stars;
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

reviewBtns.forEach((button) => {
    button.addEventListener("click", () => {
        const article =
            button.parentElement.parentElement.parentElement.nextElementSibling;
        article.classList.toggle("expanded");
        button.innerText = "Expandir" == button.innerText ? "Contraer" : "Expandir";
    });
});

//SWEET ALERT
addListeners(document.getElementById("report"));
document.querySelectorAll(".censor").forEach((form) => addListeners(form));

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
