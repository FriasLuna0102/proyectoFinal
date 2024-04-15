package org.example.servicios.grpc;

import io.grpc.stub.StreamObserver;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.grpc.org.example.url_service.UrlServiceGrpc;
import org.example.grpc.org.example.url_service.UrlServiceOuterClass;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;

import java.util.Map;

public class UrlCreateService extends UrlServiceGrpc.UrlServiceImplBase {

	@Override
	public void createUrl(UrlServiceOuterClass.UrlCreateRequest request, StreamObserver<UrlServiceOuterClass.UrlCreateResponse> responseStreamObserver){
		String username = request.getUsername();
		String urlRequest = request.getUrlBase();
		Usuario usuario = UsuarioODM.getInstance().buscarUsuarioByUsername(username);
		ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(urlRequest);
		EstadisticaURL estadisticaUrl = new EstadisticaURL();

		// Manejar logica en caso de que usuario no exista
		if (usuario == null){
			System.out.println("Usuario es nulo");
		}
		// Manejar logica en caso de la url
		if (shortURL == null){
			System.out.println("Short url es nula");
			shortURL = new ShortURL(urlRequest, null);
			URLODM.getInstance().guardarURL(shortURL);
			EstadisticaURL estadisticaURL = new EstadisticaURL(shortURL);
			EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
			usuario.getUrlList().add(shortURL);
			UsuarioODM.getInstance().guardarUsuario(usuario);
		}else {

			System.out.println("Short url no es nula");

		}



		// Construir la respuesta
		UrlServiceOuterClass.UrlCreateResponse.Builder responseBuilder = UrlServiceOuterClass.UrlCreateResponse.newBuilder()
				.setShortUrl(UrlServiceImpl.convertirUrl(shortURL))
				.setEstadisticaUrl(convertirEstadisticaVacia(estadisticaUrl, shortURL));

		responseStreamObserver.onNext(responseBuilder.build());
		responseStreamObserver.onCompleted();
	}

	private UrlServiceOuterClass.EstadisticaURL convertirEstadisticaVacia(EstadisticaURL estadisticaURL, ShortURL shortURL){

		UrlServiceOuterClass.EstadisticaURL.Builder builder = UrlServiceOuterClass.EstadisticaURL
				.newBuilder()
				.setId(String.valueOf(estadisticaURL.getId()))
				.setCantidadAccesos(estadisticaURL.getCantidadAccesos());

		builder.setShortURL(UrlServiceImpl.convertirUrl(shortURL));

		return builder.build();
	}
}
