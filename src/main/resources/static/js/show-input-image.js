const preview = document.querySelector(".preview");
//const inputLabel = document.querySelector(".image__label");
const inputImage = document.querySelector(".imageFile");

function addListener(){
   inputImage
        .addEventListener("change", event =>{
            preview.src= URL.createObjectURL(event.target.files[0])
        })
}

window.addEventListener("load", () => addListener())

