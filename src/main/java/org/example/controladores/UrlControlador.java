package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.encapsulaciones.Visitante;
import org.example.servicios.URLServices;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;
import org.example.servicios.mongo.VisitanteODM;
import org.example.utils.ControladorClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UrlControlador extends ControladorClass {

    Usuario usuarioLogueado;
    List<ShortURL> listUrlsBase = URLODM.getInstance().obtenerTodasLasUrl();

    List<Visitante> listVisitante = new ArrayList<>();

    public UrlControlador(Javalin app) {
        super(app);
    }


    @Override
    public void aplicarRutas() {



//        EstadisticaURL esta = new EstadisticaURL();
//        Date date = new Date();
//        URLServices.getInstancia().crearUrl(new ShortURL("ddd","www.com","com",date,esta,"foto"));

        app.routes(() -> {
           path("/url", () -> {


               post("generar", context -> {
                   String url = context.formParam("urlBase");
                   ShortURL shortURL = URLODM.getInstance().buscarUrlByUrlLarga(url);

                   if (shortURL == null){
                       shortURL = new ShortURL(url,null);
                       URLODM.getInstance().guardarURL(shortURL);
                       usuarioLogueado = UsuarioServices.getInstancia().getUsuarioLogueado();
                       if (usuarioLogueado != null){
                           usuarioLogueado.getUrlList().add(shortURL);
                           UsuarioODM.getInstance().guardarUsuario(usuarioLogueado);
                       }else {
                           UsuarioServices.getInstancia().getVisitanteActual().getUrlList().add(shortURL);
                           VisitanteODM.getInstance().guardarVisitante(UsuarioServices.getInstancia().getVisitanteActual());
                       }
                   }

                   EstadisticaURL estadisticaURL = new EstadisticaURL(shortURL);
                   EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
                   context.result(shortURL.getUrlCorta());
               });

               get("misUrl", cxt ->{
                   Map<String, Object> model = new HashMap<>();
                   usuarioLogueado = UsuarioServices.getInstancia().getUsuarioLogueado();
                   if (usuarioLogueado != null){
                       if (usuarioLogueado.isUser()){
                           listUrlsBase = usuarioLogueado.getUrlList();
                       }else {
                           listUrlsBase = URLODM.getInstance().obtenerTodasLasUrl();
                       }
                   }else {
                        listUrlsBase = UsuarioServices.getInstancia().getVisitanteActual().getUrlList();
                   }
                   model.put("listUrl", listUrlsBase);
                   Usuario user = UsuarioServices.getInstancia().getUsuarioLogueado();
                   model.put("usuario",user);

//                   List<ShortURL> lis = UsuarioServices.getInstancia().getVisitanteActual().getUrlList();
//                   String idVisitante = UsuarioServices.getInstancia().getVisitanteActual().getId();
//                   String idUrl = "660ec94b3ac092111c9fbf61";
//                   System.out.println("Visitante id: "+idVisitante);
//                   VisitanteODM.getInstance().eliminarUrlDeVisitante(idVisitante,idUrl);

                   cxt.render("publico/html/misUrl.html",model);
               });


               get("eliminarURL", cxt ->{
                   String codigoUrl = cxt.queryParam("codigoUrl");
                   ShortURL url = URLODM.getInstance().buscarUrlByCodig(codigoUrl);

                   List<ShortURL> lis = UsuarioServices.getInstancia().getVisitanteActual().getUrlList();
                   String idVisitante = UsuarioServices.getInstancia().getVisitanteActual().getId();
                   String idURL = url.getId();

                   for (ShortURL ur: lis){

                       if (ur.getCodigo().equals(url.getCodigo())){
                           VisitanteODM.getInstance().eliminarUrlDeVisitante(idVisitante,idURL);
                           URLODM.getInstance().eliminarUrl(url);
                           System.out.println("Son iguales");
                       }else {
                           URLODM.getInstance().eliminarUrl(url);
                       }

                   }
                   cxt.redirect("/");
               });

           });

        });


        app.get("/serviceworkers.js", context -> {
            // Cargar el contenido del archivo del Service Worker
            String contenidoServiceworker = obtenerContenidoServiceworker(); // Implementa esta función para cargar el contenido del archivo

            // Configurar la respuesta HTTP con el contenido del Service Worker
            context.contentType("application/javascript").result(contenidoServiceworker);
        });

        app.get("/{codigo}", context -> {
            String codigo = context.pathParam("codigo");
            System.out.println(codigo);
            String url = "";
            try {
                //url tira null pero no le hagan caso
                url = URLODM.getInstance().buscarUrlByCodigo(codigo);
            }catch (Exception ignored){
            }

            if (url == null){
                context.result("Pagina no encontrada").status(404);
            }

            // Obtener la fecha y hora actual
            LocalDateTime now = LocalDateTime.now();

            // Formatear la fecha y hora según el formato deseado
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTime = now.format(formatter);

            if(codigo.equalsIgnoreCase("serviceworkers.js") || codigo.equalsIgnoreCase("favicon.ico")){
                return;
            }

            if(codigo.equalsIgnoreCase("listlol")){
                return;
            }
                ShortURL shorurl = URLODM.getInstance().buscarUrlByCodig(codigo);

                // Registrar información sobre la solicitud
                String userAgent = context.userAgent();
                String ipAddress = context.ip();
                String clientDomain = context.host();
                String operatingSystem = parseOperatingSystem(userAgent);
                String navegador = parseBrowser(userAgent);

                EstadisticaURL estadisticaURL = EstadisticaODM.getInstance().buscarEstadisticaByCodigoOfUrl(shorurl.getCodigo());


                estadisticaDatos(estadisticaURL,ipAddress,clientDomain,operatingSystem,navegador,dateTime);

            assert url != null;
	        context.redirect(url);
        });

    }

    private String obtenerContenidoServiceworker() {
        // Obtener la ruta del directorio actual
        //String rutaDirectorioActual = System.getProperty("user.dir");

        // Construir la ruta absoluta al archivo serviceworkers.js
        //String rutaArchivo = rutaDirectorioActual + "/src/main/resources/publico/serviceworkers.js";

        // Definir la ruta del archivo
        String rutaArchivo = "/home/azureuser/proyectoFinal/src/main/resources/publico/serviceworkers.js";

        // Verificar si el archivo existe
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.isDirectory()) {
            // Manejar el caso en el que el archivo no exista o sea un directorio
            System.err.println("El archivo serviceworkers.js no existe en la ruta especificada: " + rutaArchivo);
            return ""; // Retornar una cadena vacía o manejar el error de otra forma
        }

        try {
            // Leer el contenido del archivo y devolverlo como una cadena
            return new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Manejar el caso de error de lectura del archivo
        }

    }



    private static void estadisticaDatos(EstadisticaURL estadisticaURL, String ipAddress, String clientDomain, String operatingSystem, String navegador, String dateTime){
        // Obtenemos los mapas de la instancia de EstadisticaURL
        Map<String, Integer> direccionesIP = estadisticaURL.getDireccionesIP();
        Map<String, Integer> dominioCliente = estadisticaURL.getDominiosClientes();
        Map<String, Integer> sistemaOperativo = estadisticaURL.getPlataformasSO();
        Map<String, Integer> navegadores = estadisticaURL.getNavegadores();
        Map<String, Integer> horaAcceso = estadisticaURL.getHorasAcceso();

        int cantAcceso = estadisticaURL.getCantidadAccesos();
        cantAcceso++;
        estadisticaURL.setCantidadAccesos(cantAcceso);

        // Inicializamos los mapas si son nulos
        if (direccionesIP == null) {
            direccionesIP = new HashMap<>();
        }
        if (dominioCliente == null) {
            dominioCliente = new HashMap<>();
        }
        if (sistemaOperativo == null) {
            sistemaOperativo = new HashMap<>();
        }
        if (navegadores == null) {
            navegadores = new HashMap<>();
        }

        if(horaAcceso == null){
            horaAcceso = new HashMap<>();
        }

        // Verificamos y aumentamos el valor de la clave si ya está presente
        direccionesIP.put(ipAddress, direccionesIP.getOrDefault(ipAddress, 0) + 1);
        dominioCliente.put(clientDomain, dominioCliente.getOrDefault(clientDomain, 0) + 1);
        sistemaOperativo.put(operatingSystem, sistemaOperativo.getOrDefault(operatingSystem, 0) + 1);
        navegadores.put(navegador, navegadores.getOrDefault(navegador, 0) + 1);
        horaAcceso.put(dateTime, horaAcceso.getOrDefault(dateTime, 0) + 1);

        // Establecemos los mapas actualizados en la instancia de EstadisticaURL
        estadisticaURL.setDireccionesIP(direccionesIP);
        estadisticaURL.setDominiosClientes(dominioCliente);
        estadisticaURL.setPlataformasSO(sistemaOperativo);
        estadisticaURL.setNavegadores(navegadores);
        estadisticaURL.setHorasAcceso(horaAcceso);

        // Guardamos la estadística actualizada
        EstadisticaODM.getInstance().guardarEstadistica(estadisticaURL);
    }



    private static String parseOperatingSystem(String userAgent) {
        String os = "Unknown";
        if (userAgent != null && !userAgent.isEmpty()) {
            if (userAgent.toLowerCase().contains("windows")) {
                os = "Windows";
            } else if (userAgent.toLowerCase().contains("macintosh") || userAgent.toLowerCase().contains("mac os")) {
                os = "Mac OS";
            } else if (userAgent.toLowerCase().contains("linux")) {
                os = "Linux";
            } else if (userAgent.toLowerCase().contains("android")) {
                os = "Android";
            } else if (userAgent.toLowerCase().contains("iphone") || userAgent.toLowerCase().contains("ipad")) {
                os = "iOS";
            }
        }
        return os;
    }

    private static String parseBrowser(String userAgent) {
        String browser = "Unknown";
        if (userAgent != null && !userAgent.isEmpty()) {
            if (userAgent.toLowerCase().contains("chrome")) {
                browser = "Chrome";
            } else if (userAgent.toLowerCase().contains("firefox")) {
                browser = "Firefox";
            } else if (userAgent.toLowerCase().contains("safari")) {
                browser = "Safari";
            } else if (userAgent.toLowerCase().contains("opera")) {
                browser = "Opera";
            } else if (userAgent.toLowerCase().contains("edge")) {
                browser = "Edge";
            } else if (userAgent.toLowerCase().contains("ie") || userAgent.toLowerCase().contains("msie")) {
                browser = "Internet Explorer";
            }
        }
        return browser;
    }
}
