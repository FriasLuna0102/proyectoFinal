package org.example.servicios;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.encapsulaciones.EstadisticaURL;
import org.example.encapsulaciones.ShortURL;
import org.example.encapsulaciones.Usuario;
import org.jetbrains.annotations.NotNull;
import org.example.utils.TablasMongo;

import java.util.ArrayList;
import java.util.List;

public class URLServices {

    private static URLServices instancia;
    private MongoDbConexion mongoDbConexion;



    /**
     * Constructor privado.
     */
    private URLServices(){
        //
        mongoDbConexion = MongoDbConexion.getInstance();
        mongoDbConexion.getBaseDatos();

    }

    public static URLServices getInstancia(){
        if(instancia==null){
            instancia = new URLServices();
        }
        return instancia;
    }



    public List<ShortURL> listarEstudiante(){
        List<ShortURL> lista = new ArrayList<>();

        //
        MongoCollection<Document> urls = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.URLS.getValor());

        //Consultando todos los elementos.
        MongoCursor<Document> iterator = urls.find().iterator();
        while (iterator.hasNext()){

            //obteniendo el documento
            Document next = iterator.next();

            //Encapsulando la información
            ShortURL url = new ShortURL();
            url.setId(next.getObjectId("_id").toHexString());
            url.setUrlBase(next.getString("urlBase"));
            url.setUrlCorta(next.getString("urlCorta"));
            url.setFechaCreacion(next.getDate("fechaCreacion"));
            url.setImgBase64(next.getString("imgBase64"));

//            // Obtener estadísticas del documento y asignarlas a la URL corta
//            Document statsDoc = (Document) next.get("estadisticas");
//            if (statsDoc != null) {
//                EstadisticaURL estadisticas = new EstadisticaURL();
//                // Aquí debes llenar los campos de la clase EstadisticaURL con los datos del documento statsDoc
//                // Supongamos que tienes métodos en la clase EstadisticaURL para establecer los campos, como setCantidadAccesos, setNavegadores, etc.
//                estadisticas.setCantidadAccesos(statsDoc.getInteger("cantidadAccesos"));
//                // Continúa con el resto de los campos
//
//                // Asignar las estadísticas a la URL corta
//                url.setEstadisticas(estadisticas);
//            }


            // Agregando a la lista.
            lista.add(url);
        }
        //retornando...
        return lista;
    }

    public ShortURL getURLPorID(String id){
        ShortURL url = null;
        //Conexion a Mongo.
        MongoCollection<Document> urls = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.URLS.getValor());

        //
        Document filtro = new Document("id", id);
        Document first = urls.find(filtro).first();

        //si no fue encontrado retorna null.
        if(first!=null){
            url = new ShortURL();
            url.setId(first.getObjectId("_id").toHexString());
            url.setUrlBase(first.getString("urlBase"));
            url.setUrlCorta(first.getString("urlCorta"));
            url.setFechaCreacion(first.getDate("fechaCreacion"));
            url.setImgBase64(first.getString("imgBase64"));

//            // Obtener estadísticas del documento y asignarlas a la URL corta
//            Document statsDoc = (Document) first.get("estadisticas");
//            if (statsDoc != null) {
//                EstadisticaURL estadisticas = new EstadisticaURL();
//                // Aquí debes llenar los campos de la clase EstadisticaURL con los datos del documento statsDoc
//                // Supongamos que tienes métodos en la clase EstadisticaURL para establecer los campos, como setCantidadAccesos, setNavegadores, etc.
//                estadisticas.setCantidadAccesos(statsDoc.getInteger("cantidadAccesos"));
//                // Continúa con el resto de los campos
//
//                // Asignar las estadísticas a la URL corta
//                url.setEstadisticas(estadisticas);
//            }

            System.out.println("Consultado: "+url.toString());
        }

        //retornando.
        return url;
    }

    public ShortURL crearUrl(@NotNull ShortURL url){
        if(getURLPorID(url.getUrlBase())!=null){
            System.out.println("URL registrado...");
            return null; //generar una excepcion...
        }

        //
        Document document = new Document("url", url.getUrlBase())
                .append("urlBase", url.getUrlBase())
                .append("urlCorta", url.getUrlCorta())
                .append("fechaCreacion", url.getFechaCreacion())
                .append("imgBase64",url.getImgBase64());
//                .append("estadisticas",url.getEstadisticas());

        //
        MongoCollection<Document> urls = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.URLS.getValor());
        //
        InsertOneResult insertOneResult = urls.insertOne(document);
        //
        System.out.println("Insertar: "+insertOneResult.getInsertedId()+", Acknowledged:"+insertOneResult.wasAcknowledged());

        return url;
    }

//    public Usuario actuali(@NotNull Usuario usuario){
//        Usuario tmp = getUsuarioPorUsername(usuario.getUsername());
//
////        if(tmp == null){//no existe, no puede se actualizado
////            throw new NoExisteEstudianteException("No Existe el estudiante: "+estudiante.getMatricula());
////        }
//
//        //Actualizando Documento.
//        MongoCollection<Document> usuarios = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.USUARIOS.getValor());
//
//        //
//        Document filtro = new Document("_id", new ObjectId(usuario.getId()));
//        //
//        //
//        Document document = new Document("username", usuario.getUsername())
//                .append("nombre", usuario.getNombre())
//                .append("password", usuario.getPassword())
//                .append("user",usuario.isUser())
//                .append("_id", new ObjectId(usuario.getId()));
//        //
//        usuarios.findOneAndUpdate(filtro, new Document("$set", document));
//
//        return usuario;
//    }

    public boolean eliminarUrl(String id){
        //
        ShortURL urlBa = getURLPorID(id);
        //Actualizando Documento.
        MongoCollection<Document> urls = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.URLS.getValor());
        //
        Document filtro = new Document("_id", new ObjectId(urlBa.getId()));
        //
        return urls.findOneAndDelete(filtro) !=null;
    }

}
