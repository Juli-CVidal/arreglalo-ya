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