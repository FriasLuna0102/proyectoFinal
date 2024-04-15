package org.example.controladores;

import io.javalin.Javalin;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.UsuarioODM;
import org.example.utils.ControladorClass;

public class RegistroControlador extends ControladorClass {

	public RegistroControlador (Javalin app){
		super(app);
	}

	@Override
	public void aplicarRutas() {

		app.get("/registro", context -> {
			context.render("publico/html/registro.html");
		});

		app.post("/registro", context -> {
			String nombre = context.formParam("nombre");
			String username = context.formParam("username");
			String password = context.formParam("password");

			if (verificarExistenciaOfUsuario(username)){
				context.redirect("/registro");
				return;
			}

			Usuario usuario = new Usuario(username,nombre,password,true);
			UsuarioODM.getInstance().guardarUsuario(usuario);
			UsuarioServices.getInstancia().setUsuarioLogueado(usuario);
			context.redirect("/");
		});
	}

	public boolean verificarExistenciaOfUsuario(String username){
		return UsuarioODM.getInstance().buscarUsuarioByUsername(username) != null;
	}
}
