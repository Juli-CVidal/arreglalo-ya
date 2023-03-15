function showLoginMessage() {
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