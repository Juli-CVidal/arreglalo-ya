document.querySelector(".imageFile")
    .addEventListener("change", event =>{
    const preview = document.getElementById("preview");
    preview.src= URL.createObjectURL(event.target.files[0])
})



