$.ajax({
    url: '/estadistica/info/horas',
    method: 'GET',
    dataType: 'json',
    success: function(data) {

        const ctx2 = document.getElementById('graficoHoras');

        new Chart(ctx2, {
            type: 'bar',
            data: {

                datasets: [{
                    label: 'Cantidad total de accesos por hora',
                    data: data,
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: false
                    }
                }
            }
        });
        console.log(Object.values(data))
    },
    error: function(jqXHR, textStatus, errorThrown) {
        console.error(errorThrown); // Manejar errores adecuadamente
    }
});
