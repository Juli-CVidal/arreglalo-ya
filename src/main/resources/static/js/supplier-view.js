//SUPPLIERS SLIDER
const supplierSwiper = new Swiper(".suppliers__container", {
	slidesPerView: "auto",
	loop: false,
	centeredSlides: true,
	spaceBetween: 10,
	navigation: {
		nextEl: ".swiper-button-next",
		prevEl: ".swiper-button-prev",
	},
	freeMode: true,
});

// ======== FILTERS ========
let lastFilter = "";
let match = false;
let FILTER_BUTTONS;
let ALL_SUPPLIERS;
let NAME_FILTER;
let CONTAINER;
let NO_RESULTS;

function checkIfResults(){
	NO_RESULTS = document.getElementById("no-results");
	match ? NO_RESULTS.classList.add("d-none") : NO_RESULTS.classList.remove("d-none");
}


function toggleButtonClass(buttonId){
	document.getElementById(buttonId).classList.toggle("btn-outline-dark");
	document.getElementById(buttonId).classList.toggle("btn-success");
}


function filterByProfession(filterProfession) {
	match = false;
	CONTAINER.innerHTML = "";
	CONTAINER.appendChild(NO_RESULTS)
	if (filterProfession === "todos") {
		ALL_SUPPLIERS.forEach(supplier => CONTAINER.appendChild(supplier));
		match = true;
		checkIfResults();
		return;
	}

	ALL_SUPPLIERS.forEach(supplier => {
		const profession = supplier.querySelector(".card-text").textContent.toLowerCase();
		if (filterProfession.toLowerCase() === profession) {
			CONTAINER.appendChild(supplier);
			match = true;
		}
	});
	checkIfResults();
}

function filterByName(filterName){
	match = false;
	const listToFilter = Array.from(CONTAINER.querySelectorAll(".supplier"))
	listToFilter.forEach(supplier =>{
		const name = supplier.querySelector(".card-title").textContent.toLowerCase();
		const found = filterName === "" || name.indexOf(filterName.toLowerCase()) !==  -1;
		supplier.style.display =  found ? "flex" : "none";

		if (found) match = true;
	})
	checkIfResults();
}

function handleButtonFilter(profession){
	if(lastFilter){
		toggleButtonClass(lastFilter);
	}
	lastFilter = profession;
	toggleButtonClass(profession);
	filterByProfession(profession);
}

function checkLocalStorage(){
	const lastProfession = localStorage.getItem("profession");
	if (lastProfession){
		console.log(lastProfession);
		localStorage.removeItem("profession");
		handleButtonFilter(lastProfession);
		lastFilter = lastProfession;
		filterByProfession(lastProfession);
	}
}
function initFields(){
	 FILTER_BUTTONS = document.querySelectorAll(".profession__filter");
	 ALL_SUPPLIERS = Array.from(document.querySelectorAll(".supplier"));
	 NAME_FILTER = document.getElementById("search-name");
	 CONTAINER = ALL_SUPPLIERS[0].parentElement;
	 CONTAINER.classList.remove("d-none");
	 NO_RESULTS = document.getElementById("no-results");
}


//CHECK IF USER IS LOGGED
function checkLogged(){
	document.querySelectorAll(".profile__link").forEach(button => button.addEventListener("click", (event)=>{
		if (!LOGGED) {
			event.preventDefault();
			showLoginMessage();
		}
	}))
}


function init(){
	initFields();
	NAME_FILTER.addEventListener("keyup", () => {
		const value = NAME_FILTER.value.toLowerCase();
		filterByName(value);
	})
	FILTER_BUTTONS.forEach(button => {
		button.addEventListener("click", () => {
			handleButtonFilter(button.id);
		})
	})
	checkLogged();
	checkLocalStorage();
}

init();