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
const worksCards = Array.from(document.querySelectorAll(".card")).slice(0, -1);

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