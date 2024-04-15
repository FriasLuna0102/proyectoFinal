package org.example.encapsulaciones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@Entity("shortURL")
public class ShortURL {

	@Id
	private String id;
	private String codigo;
	private String urlBase;
	private String urlCorta;
	private Date fechaCreacion;

    private int contadorAccesos; // Contador de accesos

    // Constructor y otros métodos de la clase

    // Método para incrementar el contador de accesos
    public void incrementarContadorAccesos() {
        this.contadorAccesos++;
    }

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public void setUrlCorta(String urlCorta) {
        this.urlCorta = urlCorta;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEstadisticas(EstadisticaURL estadisticas) {
        this.estadisticas = estadisticas;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public ShortURL(String urlBase, String imgBase64) {
        this.urlBase = urlBase;
		this.codigo = generateUniqueId();
        this.urlCorta = shortener(this.codigo);
        this.imgBase64 = imgBase64;
        this.fechaCreacion = Date.from(Instant.now());
    }
    public ShortURL(String id, String urlBase, String urlCorta, Date fechaCreacion, EstadisticaURL estadisticas, String imgBase64) {
        this.id = id;
        this.urlBase = urlBase;
        this.urlCorta = urlCorta;
        this.fechaCreacion = fechaCreacion;
        this.estadisticas = estadisticas;
        this.imgBase64 = imgBase64;
    }

    public void setId(String id) {
        this.id = id;
    }

    private EstadisticaURL estadisticas;
	private String imgBase64;



    public ShortURL() {
    }
	public static String generateUniqueId() {
		SecureRandom random = new SecureRandom();
		String alphanumericCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < 8; i++) {
			int randomIndex = random.nextInt(alphanumericCharacters.length());
			sb.append(alphanumericCharacters.charAt(randomIndex));
		}

		return sb.toString();
	}

    // URL corta debe ser nombre de dominio + id
	public String shortener(String codigo){
		return "https://proyectofinal.friasluna.me:7000/" + codigo;
	}

	public String getId() {
		return id;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public String getUrlCorta() {
		return urlCorta;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public EstadisticaURL getEstadisticas() {
		return estadisticas;
	}

	public String getImgBase64() {
		return imgBase64;
	}
}
