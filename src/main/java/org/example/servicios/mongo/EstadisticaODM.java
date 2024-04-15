package org.example.servicios.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EstadisticaODM {
	private static EstadisticaODM instance;
	private final Datastore datastore;

	public static EstadisticaODM getInstance(){
		if (instance == null){
			instance = new EstadisticaODM();
		}
		return instance;
	}
	private EstadisticaODM() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String URL_MONGODB = processBuilder.environment().get("URL_MONGO");
		String DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");

		datastore = Morphia.createDatastore(MongoClients.create(URL_MONGODB), DB_NOMBRE);
		datastore.getMapper().map(EstadisticaURL.class);
		datastore.ensureIndexes();
	}

	public void guardarEstadistica(EstadisticaURL estadisticaURL){
		datastore.save(estadisticaURL);
	}

	public EstadisticaURL buscarEstadisticaByCodigoOfUrl(String codigo){
		List<EstadisticaURL> list = datastore.find(EstadisticaURL.class).iterator().toList();
		EstadisticaURL estadistica = null;

		for (EstadisticaURL estadisticaURL: list){
			if (estadisticaURL.getShortURL().getCodigo().equals(codigo)){
				estadistica = estadisticaURL;
			}
		}

		return estadistica;
	}

    public List<EstadisticaURL> obtenerTodasLasEstadisticas(){
        List<EstadisticaURL> estadisticaURLS = new ArrayList<>();
        Iterator<EstadisticaURL> iterator = datastore.find(EstadisticaURL.class).iterator();
        while(iterator.hasNext()){
            estadisticaURLS.add(iterator.next());
        }
        return estadisticaURLS;
    }

}
