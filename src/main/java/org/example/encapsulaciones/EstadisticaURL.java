package org.example.encapsulaciones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity("Estadistica")
public class EstadisticaURL {
	@Id
	private ObjectId id;
	private int cantidadAccesos;
	private Map<String, Integer> navegadores;
	private Map<String, Integer> direccionesIP;
	private Map<String, Integer> dominiosClientes;
	private Map<String, Integer> plataformasSO;
	private Map<String, Integer> horasAcceso;

	@Reference
	private ShortURL shortURL;

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setNavegadores(Map<String, Integer> navegadores) {
        this.navegadores = navegadores;
    }

    public void setDireccionesIP(Map<String, Integer> direccionesIP) {
        this.direccionesIP = direccionesIP;
    }

    public void setDominiosClientes(Map<String, Integer> dominiosClientes) {
        this.dominiosClientes = dominiosClientes;
    }

    public void setPlataformasSO(Map<String, Integer> plataformasSO) {
        this.plataformasSO = plataformasSO;
    }

    public void setHorasAcceso(Map<String, Integer> horasAcceso) {
        this.horasAcceso = horasAcceso;
    }

    public void setShortURL(ShortURL shortURL) {
        this.shortURL = shortURL;
    }

    public EstadisticaURL(ShortURL shortURL) {
		this.cantidadAccesos = 0;
		this.navegadores = new HashMap<>();
		this.direccionesIP = new HashMap<>();
		this.dominiosClientes = new HashMap<>();
		this.plataformasSO = new HashMap<>();
		this.horasAcceso = new HashMap<>();
		this.shortURL = shortURL;
	}

	public EstadisticaURL() {
	}

	public ObjectId getId() {
		return id;
	}

	public ShortURL getShortURL() {
		return shortURL;
	}

	public void aumentarCantAcceso(){
		this.cantidadAccesos++;
	}
	public int getCantidadAccesos() {
		return cantidadAccesos;
	}

	public Map<String, Integer> getNavegadores() {
		return navegadores;
	}

	public Map<String, Integer> getDireccionesIP() {
		return direccionesIP;
	}

	public Map<String, Integer> getDominiosClientes() {
		return dominiosClientes;
	}

	public Map<String, Integer> getPlataformasSO() {
		return plataformasSO;
	}

	public Map<String, Integer> getHorasAcceso() {
		return horasAcceso;
	}

    public void setCantidadAccesos(int cantidadAccesos) {
        this.cantidadAccesos = cantidadAccesos;
    }
}
