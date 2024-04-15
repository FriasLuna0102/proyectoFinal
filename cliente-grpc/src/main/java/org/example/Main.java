package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.grpc.org.example.url_service.UrlServiceGrpc;
import org.example.grpc.org.example.url_service.UrlServiceOuterClass;

import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		String host = "proyectofinal.friasluna.me";
		int puerto = 50051;

		ManagedChannel channel = ManagedChannelBuilder
				.forAddress(host, puerto)
				.usePlaintext()
				.build();

		UrlServiceGrpc.UrlServiceBlockingStub urlInterfaz = UrlServiceGrpc.newBlockingStub(channel);


		String username = "";
		Scanner scanner = new Scanner(System.in);
		int opcion = 0;
		boolean continuar = true;
		while (continuar){
			System.out.println("1.Listar Urls de un usuario");
			System.out.println("2.Crear url corta para un usuario");
			System.out.println("3.Salir");
			System.out.print("Seleccione: ");
			opcion = scanner.nextInt();

			switch (opcion){
				case 1:
					System.out.print("Username: ");
					username = scanner.next();
					listarUrls(urlInterfaz, username);
					break;
				case 2:
					System.out.print("Username: ");
					username = scanner.next();
					System.out.print("Url: ");
					String url = scanner.next();
					crearUrl(urlInterfaz, username, url);
					break;
				case 3:
					continuar = false;
			}
		}
	}

	public static void crearUrl(UrlServiceGrpc.UrlServiceBlockingStub urlInterfaz, String username, String url){

		UrlServiceOuterClass.UrlCreateResponse urlCreateResponse = urlInterfaz
				.createUrl(UrlServiceOuterClass.UrlCreateRequest
						.newBuilder()
						.setUsername(username)
						.setUrlBase(url)
						.build());


	}

	public static void listarUrls(UrlServiceGrpc.UrlServiceBlockingStub urlInterfaz, String username){

		UrlServiceOuterClass.UrlListResponse urlListResponse = urlInterfaz
				.listUrls(UrlServiceOuterClass.UrlListRequest
						.newBuilder()
						.setUsername(username)
						.build());

		imprimirUrlResponse(urlListResponse);
	}
	
	public static void imprimirUrlResponse(UrlServiceOuterClass.UrlListResponse urlListResponse){
		int count = urlListResponse.getUrlListList().size();
		List<UrlServiceOuterClass.EstadisticaURL> estadisticaURLList = urlListResponse.getEstadisticaUrlListList();
		List<UrlServiceOuterClass.ShortURL> urlList = urlListResponse.getUrlListList();

		for (int i = 0; i < count; i++){
			System.out.println("Url #"+(i+1));
			System.out.println("Codigo Url: " + urlList.get(i).getCodigo());
			System.out.println("Url Base: " + urlList.get(i).getUrlBase() );
			System.out.println("Url Corta: " + urlList.get(i).getUrlCorta());
			System.out.println("Cantidad de accesos: " + estadisticaURLList.get(i).getCantidadAccesos());
			System.out.println("Cantidad de direcciones ip: " + estadisticaURLList.get(i).getDireccionesIPCount());
			System.out.println("Cantidad de plataformas: " + estadisticaURLList.get(i).getPlataformasSOCount());
			System.out.println("\n");
		}
	}
}