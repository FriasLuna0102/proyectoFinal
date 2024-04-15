package org.example.controladores;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.UsuarioODM;
import org.example.utils.ControladorClass;
import org.example.utils.JWTutils;

public class LoginControlador extends ControladorClass {

	public LoginControlador (Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/login", context -> {
			context.render("publico/html/login.html");
		});

        app.post("/login", context -> {
            String username = context.formParam("username");
            String password = context.formParam("password");

            System.out.println(username);
            if (username == null && password == null) {
                JsonObject requestBody = new JsonParser().parse(context.body()).getAsJsonObject();
                String nombreClient = requestBody.get("username").getAsString();
                String passClint = requestBody.get("password").getAsString();
                Usuario usuarioClient = UsuarioODM.getInstance().buscarUsuarioByUsername(nombreClient);

                if (usuarioClient != null && usuarioClient.getPassword().equals(passClint)){
                    //UsuarioServices.getInstancia().setUsuarioLogueado(usuarioClient);
                    String jwt = JWTutils.generateJwt(username);

                    // Enviar el JWT como una cookie
                    context.cookie("jwt", jwt);

                    // Crear un objeto JSON para enviar el JWT en el cuerpo de la respuesta
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("jwt", jwt);

                    context.result(jsonObject.toString());
                    return;
                }
            }




            Usuario usuario = UsuarioODM.getInstance().buscarUsuarioByUsername(username);

            if (usuario != null && usuario.getPassword().equals(password)){
                UsuarioServices.getInstancia().setUsuarioLogueado(usuario);
                context.cookie("jwt", JWTutils.generateJwt(username));
                context.redirect("/");
                return;
            }



            context.redirect("/login");
        });


        app.get("/logout", context -> {
			UsuarioServices.getInstancia().setUsuarioLogueado(null);
			context.removeCookie("jwt");
			context.redirect("/");
		});
	}

}
