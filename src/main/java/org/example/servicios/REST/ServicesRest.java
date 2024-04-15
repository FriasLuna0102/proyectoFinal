package org.example.servicios.REST;

import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.example.servicios.URLServices;
import org.example.servicios.UsuarioServices;
import org.example.servicios.mongo.EstadisticaODM;
import org.example.servicios.mongo.URLODM;
import org.example.servicios.mongo.UsuarioODM;

import java.util.ArrayList;
import java.util.List;

public class ServicesRest {

    List<ShortURL> listUrl= URLODM.getInstance().obtenerTodasLasUrl();
    List<EstadisticaURL> listEstadistica = EstadisticaODM.getInstance().obtenerTodasLasEstadisticas();

    List<ShortURL> listUrlConEstadisticas = new ArrayList<>();

    List<Usuario> listUsuarios = UsuarioODM.getInstance().buscarTodosLosUsuarios();
    private static ServicesRest instacia;

    public static ServicesRest getInstacia(){
        if(instacia == null){
            instacia = new ServicesRest();
        }
        return instacia;
    }

    public List<ShortURL> listUrlConEstadistica(){

        for(ShortURL url: listUrl){
            for (EstadisticaURL estadisticaURL: listEstadistica){
                if(url.getId().equals(estadisticaURL.getShortURL().getId())){
                    url.setEstadisticas(estadisticaURL);

                    for(Usuario usua: listUsuarios){
                        if(usua.getUrlList() != null){

                            for(ShortURL uUsuario : usua.getUrlList()){

                                if(uUsuario.getId().equals(url.getId())){
                                    System.out.println("Son iguales");
                                    listUrlConEstadisticas.add(url);
                                }

                            }


                        }

                    }


                }
            }
        }
        return listUrlConEstadisticas;
    }




    public List<ShortURL> listUrlConEstadisticaParaUnUsuario(String nombre){
        Usuario user = UsuarioODM.getInstance().buscarUsuarioByNombre(nombre);

       if(user != null){
           for(ShortURL url: listUrl){
               for (EstadisticaURL estadisticaURL: listEstadistica){
                   if(url.getId().equals(estadisticaURL.getShortURL().getId())){
                       url.setEstadisticas(estadisticaURL);

                       for(Usuario usua: listUsuarios){
                           if(usua.getId().equals(user.getId())){

                               for(ShortURL uUsuario : usua.getUrlList()){

                                   if(uUsuario.getId().equals(url.getId())){
                                       System.out.println("Son iguales");
                                       listUrlConEstadisticas.add(url);
                                   }
                               }
                           }
                       }
                   }
               }
           }
           return listUrlConEstadisticas;

       }else {
           System.out.println("No existe el usuario.");
           return null;
       }

    }








}
