$(document).ready(function() {
    $('form').submit(function(event) {
        event.preventDefault(); // Evitar que el formulario se envíe automáticamente

        // Obtener el valor de la URL base del input
        var urlBase = $('#urlBase').val();

        // Enviar la solicitud POST al servidor
        $.post('/url/generar', { urlBase: urlBase }, function(data) {
            // Actualizar el valor del input de URL acortada
            $('#urlCorta').val(data);
        })
        .fail(function(error) {
            console.error('Error:', error);
        });
    });
});
