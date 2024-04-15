$.ajax({
    url: '/estadistica/info/dias',
    method: 'GET',
    dataType: 'json',
    success: function(data) {
        // Usar los datos en el gráfico de Chart.js
        const ctx = document.getElementById('graficoDias');

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Lunes', 'Martes', 'Miercoles', 'Jueves', 'Viernes', 'Sabado', 'Domingo'],
                datasets: [{
                    label: 'Cantidad total de accesos por dia',
                    data: Object.values(data), // Usar los valores del objeto JSON como datos
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
    },
    error: function(jqXHR, textStatus, errorThrown) {
        console.error(errorThrown); // Manejar errores adecuadamente
    }
});
