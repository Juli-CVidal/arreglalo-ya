const radios = Array.from(document.querySelectorAll(".rate"));
const inputScore = document.querySelector(".score");

function changeStarClass(index,className){
    radios[index].classList = className;
}

inputScore.addEventListener("keyup", (event) => {
    radios.forEach(radio => radio.classList = "ri-star-line")
    const value = parseFloat(inputScore.value);
    const scoreIndex = parseInt(value);
    const halfStar = value.toString().split(".")[1] !== undefined;

    for (let index = 0; index <scoreIndex; index++) {
        changeStarClass(index,"ri-star-fill")
    }
    if (halfStar){
        changeStarClass(scoreIndex,"ri-star-half-fill");
    }
})
