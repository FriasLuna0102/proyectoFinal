package org.example.controladores;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.RestObject;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.REST.ServicesRest;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;
import org.example.servicios.mongo.VisitanteODM;
import org.example.utils.ControladorClass;

import java.util.List;
import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.after;
import static io.javalin.apibuilder.ApiBuilder.*;

public class ApiControlador extends ControladorClass {
    public ApiControlador(Javalin app) {
        super(app);
    }


    @Override
    public void aplicarRutas() {


          app.after("/list",ctx -> {
              ctx.header("Content-Type", "application/json");
          });


        app.get("/list/{nombre}", context -> {
            String nombre = context.queryParam("nombre");
            if (nombre == null || nombre.isEmpty()) {
                // Si el parámetro está ausente o es una cadena vacía, devolver un error 400 Bad Request
                context.status(400).result("El parámetro 'nombre' es requerido.");
                return;
            }
            System.out.println("Entroo");
            List<ShortURL> lista = ServicesRest.getInstacia().listUrlConEstadisticaParaUnUsuario(nombre);
            if (lista == null) {
                // Si el usuario no existe, devolver un error 404 Not Found
                context.status(404).result("No se encontró el usuario.");
                return;
            }
            context.json(lista);
        });



        app.post("/generar", context -> {
            JsonObject requestBody = new JsonParser().parse(context.body()).getAsJsonObject();
            String nombre = requestBody.get("nombre").getAsString();
            String urlBase = requestBody.get("urlBase").getAsString();

            // Verificar si el usuario existe
            Usuario usuario = UsuarioODM.getInstance().buscarUsuarioByNombre(nombre);
            //System.out.println(usuario.getNombre());
            if (usuario == null) {
                context.status(404).result("El usuario no existe."); // Devolver un error 404 Not Found
                return;
            }

            // Si el usuario existe, continuar con la lógica para acortar la URL
            ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(urlBase);

            if (shortURL == null) {
                shortURL = new ShortURL(urlBase, null);
                URLODM.getInstance().guardarURL(shortURL);
                usuario.getUrlList().add(shortURL); // Agregar la URL al usuario
                UsuarioODM.getInstance().guardarUsuario(usuario); // Guardar el usuario actualizado
                EstadisticaURL estadisticaURL = new EstadisticaURL(shortURL);
                EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
            }else{

                context.status(404).result("La URL ya existe.");
//                RestObject restObject = new RestObject();
//                restObject.setUrlBase(urlBase);
//                restObject.setUrlCorta(shortURL.getUrlCorta());
//                restObject.setFechaCreacion(shortURL.getFechaCreacion());
//                // Crear un objeto Gson
//                Gson gson = new Gson();
//
//                // Convertir el objeto RestObject a JSON
//                String json = gson.toJson(restObject);
//
//                System.out.println("else");
//                // Devolver el JSON como respuesta al cliente
//                context.result(json);
                return;
            }


            RestObject restObject = new RestObject();
            restObject.setUrlBase(urlBase);
            restObject.setUrlCorta(shortURL.getUrlCorta());
            restObject.setFechaCreacion(shortURL.getFechaCreacion());

            List<EstadisticaURL> listEstadistica = EstadisticaODM.getInstance().obtenerTodasLasEstadisticas();

            for(EstadisticaURL estadisticaURL: listEstadistica){
                if(Objects.equals(shortURL.getId(), estadisticaURL.getShortURL().getId())){
                    restObject.setEstadisticaURL(estadisticaURL);
                }
            }

            // Crear un objeto Gson
            Gson gson = new Gson();

            // Convertir el objeto RestObject a JSON
            String json = gson.toJson(restObject);

            // Devolver el JSON como respuesta al cliente
            context.result(json);
        });

    }
}
