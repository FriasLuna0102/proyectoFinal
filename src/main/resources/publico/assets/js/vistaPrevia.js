document.addEventListener('DOMContentLoaded', function (event) {
    document.getElementById('previewButton').addEventListener('click', function () {
        var url = document.getElementById('urlBase').value;
        console.log(url);
        microlink('.link-preview', {url: url});
    });
});



    document.addEventListener("DOMContentLoaded", function (event){
    function obtenerURL() {
        return document.getElementById("urlBase").value; // Obtener el valor de la URL
    }

    // Obtener el enlace
    var link = document.getElementById("link");

    // Agregar un event listener para el evento click al enlace
    link.addEventListener("click", function(event) {
    // Prevenir el comportamiento predeterminado del enlace
    event.preventDefault();

    // Obtener la URL
    var url = obtenerURL();

    // Abrir la URL en una nueva ventana
    window.open(url, '_blank');
});
});
