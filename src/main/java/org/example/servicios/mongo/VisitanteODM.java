package org.example.servicios.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Visitante;

import java.util.Iterator;
import java.util.List;

public class VisitanteODM {
	private static VisitanteODM instance;
	private final Datastore datastore;

	public static VisitanteODM getInstance(){
		if (instance == null){
			instance = new VisitanteODM();
		}
		return instance;
	}
	private VisitanteODM() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String URL_MONGODB = processBuilder.environment().get("URL_MONGO");
		String DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");

		datastore = Morphia.createDatastore(MongoClients.create(URL_MONGODB), DB_NOMBRE);
		datastore.getMapper().map(Visitante.class);
		datastore.ensureIndexes();
	}

	public void guardarVisitante(Visitante visitante){
		datastore.save(visitante);
	}

	public Visitante buscarVisitanteById(String id){
		Query<Visitante> visitanteQuery = datastore.find(Visitante.class).filter("_id", id);

		return visitanteQuery.first();
	}

    public void eliminarUrl(ShortURL url){

        datastore.delete(url);
    }


    public void eliminarUrlDeVisitante(String visitanteId, String shortUrlIdToRemove) {
        // Obtener el visitante por su ID
        Visitante visitante = buscarVisitanteById(visitanteId);

        if (visitante != null) {
            // Obtener la lista de URLs del visitante
            List<ShortURL> urlList = visitante.getUrlList();

            // Iterar sobre la lista de URLs del visitante y eliminar el elemento deseado
            Iterator<ShortURL> iterator = urlList.iterator();
            while (iterator.hasNext()) {
                ShortURL url = iterator.next();
                if (url.getId().equals(shortUrlIdToRemove)) {
                    iterator.remove();
                    break; // Terminar el bucle después de eliminar el elemento
                }
            }

            // Guardar el visitante actualizado en la base de datos
            guardarVisitante(visitante);
        }
    }



}
