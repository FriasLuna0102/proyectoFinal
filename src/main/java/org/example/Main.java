package org.example;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.example.controladores.*;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.grpc.UrlCreateService;
import org.example.servicios.grpc.UrlServiceImpl;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.UsuarioODM;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		Javalin app = Javalin.create(config ->{
			//configurando los documentos estaticos.
			config.staticFiles.add(staticFileConfig -> {
				staticFileConfig.hostedPath = "/";
				staticFileConfig.directory = "/publico";
				staticFileConfig.location = Location.CLASSPATH;
				staticFileConfig.precompress=false;
				staticFileConfig.aliasCheck=null;
				JavalinRenderer.register(new JavalinThymeleaf(), ".html");

			});

			//Habilitando el CORS. Ver: https://javalin.io/plugins/cors#getting-started para más opciones.
			config.plugins.enableCors(corsContainer -> {
				corsContainer.add(corsPluginConfig -> {
					corsPluginConfig.anyHost();
				});
			});
		});
		app.start(7000);



        new ApiControlador(app).aplicarRutas();
		new IndexControlador(app).aplicarRutas();
		new LoginControlador(app).aplicarRutas();
		new RegistroControlador(app).aplicarRutas();
		new UsuarioControlador(app).aplicarRutas();
		new UrlControlador(app).aplicarRutas();
		new EstadisticaControlador(app).aplicarRutas();


//        //Filtro para enviar el header de validación
//        app.after(ctx -> {
//            if(ctx.path().equalsIgnoreCase("/serviceworkers.js")){
//                System.out.println("Enviando el header de seguridad para el Service Worker");
//                ctx.header("Content-Type","application/javascript");
//                ctx.header("Service-Worker-Allowed", "/");
//            }
//        });


		// Crear usuario administrador
		if (UsuarioODM.getInstance().buscarUsuarioByUsername("admin") == null){
			Usuario admin = new Usuario("admin","admin","admin", false);
			UsuarioODM.getInstance().guardarUsuario(admin);
		}
        List<Usuario> listUsuarios = UsuarioODM.getInstance().buscarTodosLosUsuarios();

		startGrpcServer();
    }

	private static void startGrpcServer() throws IOException, InterruptedException {
		Server server = ServerBuilder
				.forPort(50051)
				.addService(new UrlServiceImpl())
				.build();

		server.start();
		server.awaitTermination();
		System.out.println("Servidor gRPC iniciado en el puerto: " + server.getPort());
	}

}

