package org.example.servicios.mongo;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.query.Query;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class URLODM {

	private static URLODM instance;
	private final Datastore datastore;

	public static URLODM getInstance(){
		if (instance == null){
			instance = new URLODM();
		}
		return instance;
	}
	private URLODM() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String URL_MONGODB = processBuilder.environment().get("URL_MONGO");
		String DB_NOMBRE = processBuilder.environment().get("DB_NOMBRE");

		datastore = Morphia.createDatastore(MongoClients.create(URL_MONGODB), DB_NOMBRE);
		datastore.getMapper().map(ShortURL.class);
		datastore.ensureIndexes();
	}

	public void guardarURL(ShortURL shortURL){
		datastore.save(shortURL);
	}

	public ShortURL buscarUrlByUrlLarga(String urlLarga){
		Query<ShortURL> shortURLS = datastore.find(ShortURL.class).filter("urlBase", urlLarga);

		return shortURLS.first();
	}

	public String buscarUrlByCodigo(String codigo){
		Query<ShortURL> url = datastore.find(ShortURL.class).filter("codigo", codigo);

		return url.first().getUrlBase();
	}

    public ShortURL buscarUrlByCodig(String codigo){
        Query<ShortURL> query = datastore.find(ShortURL.class).filter("codigo", codigo);
        ShortURL url = query.first();
        return url;
    }



    public void eliminarUrl(ShortURL url){

        datastore.delete(url);
    }

    public List<ShortURL> obtenerTodasLasUrl(){
        List<ShortURL> urls = new ArrayList<>();
        Iterator<ShortURL> iterator = datastore.find(ShortURL.class).iterator();
        while(iterator.hasNext()){
            urls.add(iterator.next());
        }
        return urls;
    }
}
