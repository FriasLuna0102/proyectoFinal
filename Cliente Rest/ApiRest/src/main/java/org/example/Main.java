package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static URL urlApi;
    private static URL urlApiCrear;

    private static String jwt;
    private static HttpRequest request;
    private static HttpResponse<String> response;
    private static final HttpClient cliente = HttpClient.newBuilder().build();

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        urlApi = new URI("https://proyectofinal.friasluna.me/list/").toURL();
        urlApiCrear = new URI("https://proyectofinal.friasluna.me/generar").toURL();
        jwt = autenticarUsuario("lol","lol");
        Scanner scanner = new Scanner(System.in);
        boolean seguir = true;

        if(jwt != null){
            while (seguir){
                System.out.println("1. Listado de URL de un Usuario");
                System.out.println("2. Crear un Registro de URL");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option){
                    case 1:
                        System.out.println("Ingrese el nombre del usuario:");
                        listarUrl(scanner.nextLine());
                        break;
                    case 2:
                        crearRegistroUrl();
                        break;

                    case 3:
                        seguir = false;
                }
            }
        }


    }



    public static void listarUrl(String nombre) throws IOException, InterruptedException, URISyntaxException {

        if(jwt != null){
            System.out.println(jwt);

            request = HttpRequest.newBuilder()
                    .uri(URI.create(urlApi.toURI() + nombre))
                    .build();

            response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        }
    }

    public static void crearRegistroUrl() throws URISyntaxException, IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        if(jwt != null){
            String nombre, urlBase;
            System.out.print("Nombre de Usuario: ");
            nombre = scanner.nextLine();
            System.out.print("URL a acortar: ");
            urlBase = scanner.nextLine();

            // Construir un objeto JSON con el nombre de usuario y la URL
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("nombre", nombre);
            jsonObject.addProperty("urlBase", urlBase);

            Gson gson = new Gson();

            // Enviar la solicitud POST con los datos JSON
            request = HttpRequest.newBuilder()
                    .uri(urlApiCrear.toURI())
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(jsonObject)))
                    .build();

            response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Respuesta del servidor: " + response.body());
        }

    }


    public static String autenticarUsuario(String nombre, String password) throws URISyntaxException, IOException, InterruptedException {
        // Construir un objeto JSON con el nombre de usuario y la contraseña
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", nombre);
        jsonObject.addProperty("password", password);

        Gson gson = new Gson();

        // Enviar la solicitud POST con los datos JSON
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://proyectofinal.friasluna.me/login"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(jsonObject)))
                .build();

        HttpClient cliente = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS) // Configurar el cliente para seguir las redirecciones
                .build();

        HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        // Extraer el JWT de las cookies de la respuesta
        HttpHeaders headers = response.headers();
        Optional<String> setCookieHeader = headers.firstValue("Set-Cookie");
        String jwt = null;
        if (setCookieHeader.isPresent()) {
            String cookies = setCookieHeader.get();
            for (String cookie : cookies.split("; ")) {
                if (cookie.startsWith("jwt=")) {
                    jwt = cookie.substring(4); // Extraer el valor del JWT
                    break;
                }
            }
        }

        if (jwt == null) {
            System.out.println("No se pudo obtener el JWT. Código de estado: " + response.statusCode());
        }

        return jwt;
    }








}