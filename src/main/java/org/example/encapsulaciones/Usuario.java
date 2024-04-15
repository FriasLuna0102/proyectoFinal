package org.example.encapsulaciones;


import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity("Usuarios")
public class Usuario {

	@Id
	private String id;
	private String username;
	private String nombre;
	private String password;
	private boolean user;

	@Reference
	private List<ShortURL> urlList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setUrlList(List<ShortURL> urlList) {
        this.urlList = urlList;
    }

	public Usuario(String username, String nombre, String password, boolean user) {
		this.username = username;
		this.nombre = nombre;
		this.password = password;
		this.user = user;
		this.urlList = new ArrayList<>();
	}

    public Usuario(String username, String nombre, String password, boolean user, String id) {
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.user = user;
        this.urlList = new ArrayList<>();
        this.id = id;
    }

    public Usuario() {
    }

    public void setUsername(String username) {
		this.username = username;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isUser() {
		return user;
	}

	public List<ShortURL> getUrlList() {
		return urlList;
	}
}
