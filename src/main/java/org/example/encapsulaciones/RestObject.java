package org.example.encapsulaciones;

import java.util.Date;

public class RestObject {

    private String urlBase;
    private String urlCorta;
    private Date fechaCreacion;
    private EstadisticaURL estadisticaURL;

    private String imgBase64;

    public RestObject(String urlBase, String urlCorta, Date fechaCreacion, EstadisticaURL estadisticaURL, String imgBase64) {
        this.urlBase = urlBase;
        this.urlCorta = urlCorta;
        this.fechaCreacion = fechaCreacion;
        this.estadisticaURL = estadisticaURL;
        this.imgBase64 = imgBase64;
    }

    public RestObject() {
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    public String getUrlCorta() {
        return urlCorta;
    }

    public void setUrlCorta(String urlCorta) {
        this.urlCorta = urlCorta;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadisticaURL getEstadisticaURL() {
        return estadisticaURL;
    }

    public void setEstadisticaURL(EstadisticaURL estadisticaURL) {
        this.estadisticaURL = estadisticaURL;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }
}
