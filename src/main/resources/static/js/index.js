// HERO SLIDE
let heroSwiper = new Swiper(".hero__swiper", {
    effect: "fade",
    speed: 750,
    coverflowEffect: {
        rotate: 30,
        slideShadows: false,
    },
    loop: true,
    autoplay: {
        delay: 5_000, //five seconds
        disableOnInteraction: false,
    },
    slidesPerView: 1,
});

//WORKS SEARCH
const worksSwiper = new Swiper(".works__container", {
    slidesPerView: "auto",
    spaceBetween: 10,
    freeMode: true,
});

const noResults = document.getElementById("no-results");
const searchInput = document.getElementById("search-work");

//The last card is shown when search returns no results
const worksCards = Array.from(document.querySelectorAll(".supplier")).slice(0, -1);

searchInput.addEventListener("keyup", (event) => {
    let results = false;
    const currentSearch = event.target.value.toLowerCase();
    worksCards.forEach((card) => {
        const h1 = card.querySelector("h1");
        const match = h1.innerText.toLowerCase().startsWith(currentSearch);
        card.style.display = match ? "flex" : "none";
        if (match) {
            results = true;
        }
    });
    noResults.style.display = results ? "none" : "flex";
    worksSwiper.slideTo(0, 100);
});


//SUPPLIER SECTION
const supplierSlider = new Swiper(".suppliers__container", {
    slidesPerView: "auto",
    spaceBetween: 10,
    freeMode: true,
});


//CHECK IF USER IS LOGGED
function showLoginMessage(){
    Swal.fire({
        title: 'Espera!',
        html: "<p>No estás logueado<br>Si quieres ver más, inicia sesión</p>",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        confirmButtonText: "Entendido!",
        cancelButtonColor: '#555',
        cancelButtonText: "Volver",
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "/login"
        }
    })
}

const LOGGED = document.getElementById("logged");
const PROFILE_LINKS = document.querySelectorAll(".profile__link");

PROFILE_LINKS.forEach(link => link.addEventListener("click", event => {
    if (!LOGGED){
        event.preventDefault();
        showLoginMessage();
    }
}));