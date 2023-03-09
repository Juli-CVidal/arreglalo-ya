function showSuccess() {
    Swal.fire({
        title: 'Estás seguro?',
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Si, censurar'
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire(
                'Censurado',
                'El usuario ha sido reportado con éxito',
                '!!'
            )
        }
    })
}

document.querySelector(".swal2-confirm swal2-styled").addEventListener("click", window.location.href="http://localhost:8080/");


