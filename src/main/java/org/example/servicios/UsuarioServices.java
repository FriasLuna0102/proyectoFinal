package org.example.servicios;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.example.encapsulaciones.Usuario;
import org.example.encapsulaciones.Visitante;
import org.jetbrains.annotations.NotNull;
import org.example.utils.TablasMongo;

import java.util.ArrayList;
import java.util.List;

public class UsuarioServices {

    private static UsuarioServices instancia;
    private MongoDbConexion mongoDbConexion;
    private Usuario usuarioLogueado;
    private Visitante visitanteActual;


    public Visitante getVisitanteActual() {
        return visitanteActual;
    }

    public void setVisitanteActual(Visitante visitanteActual) {
        this.visitanteActual = visitanteActual;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuario usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Constructor privado.
     */
    private UsuarioServices(){
        //
        mongoDbConexion = MongoDbConexion.getInstance();
        mongoDbConexion.getBaseDatos();

    }

    public static UsuarioServices getInstancia(){
        if(instancia==null){
            instancia = new UsuarioServices();
        }
        return instancia;
    }



    public List<Usuario> listarEstudiante(){
        List<Usuario> lista = new ArrayList<>();

        //
        MongoCollection<Document> usuarios = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.USUARIOS.getValor());

        //Consultando todos los elementos.
        MongoCursor<Document> iterator = usuarios.find().iterator();
        while (iterator.hasNext()){

            //obteniendo el documento
            Document next = iterator.next();

            //Encapsulando la información
            Usuario user = new Usuario();
            user.setId(next.getObjectId("_id").toHexString());
            user.setUsername(next.getString("username"));
            user.setNombre(next.getString("nombre"));
            user.setPassword(next.getString("password"));
            user.setUser(next.getBoolean("user"));

            // Agregando a la lista.
            lista.add(user);
        }
        //retornando...
        return lista;
    }

    public Usuario getUsuarioPorUsername(String username){
        Usuario usuario = null;
        //Conexion a Mongo.
        MongoCollection<Document> usuarios = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.USUARIOS.getValor());

        //
        Document filtro = new Document("username", username);
        Document first = usuarios.find(filtro).first();

        //si no fue encontrado retorna null.
        if(first!=null){
            usuario = new Usuario();
            usuario.setId(first.getObjectId("_id").toHexString());
            usuario.setUsername(first.getString("username"));
            usuario.setNombre(first.getString("nombre"));
            usuario.setPassword(first.getString("password"));
            usuario.setUser(first.getBoolean("user"));

            System.out.println("Consultado: "+usuario.toString());
        }

        //retornando.
        return usuario;
    }

    public Usuario crearEstudiante(@NotNull Usuario usuario){
        if(getUsuarioPorUsername(usuario.getUsername())!=null){
            System.out.println("Usuario registrado...");
            return null; //generar una excepcion...
        }

        //
        Document document = new Document("username", usuario.getUsername())
                .append("nombre", usuario.getNombre())
                .append("password", usuario.getPassword())
                .append("user", usuario.isUser());

        //
        MongoCollection<Document> estudiantes = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.USUARIOS.getValor());
        //
        InsertOneResult insertOneResult = estudiantes.insertOne(document);
        //
        System.out.println("Insertar: "+insertOneResult.getInsertedId()+", Acknowledged:"+insertOneResult.wasAcknowledged());

        return usuario;
    }

    public Usuario actualizarUsuario(@NotNull Usuario usuario){
        Usuario tmp = getUsuarioPorUsername(usuario.getUsername());

//        if(tmp == null){//no existe, no puede se actualizado
//            throw new NoExisteEstudianteException("No Existe el estudiante: "+estudiante.getMatricula());
//        }

        //Actualizando Documento.
        MongoCollection<Document> usuarios = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.USUARIOS.getValor());

        //
        Document filtro = new Document("_id", new ObjectId(usuario.getId()));
        //
        //
        Document document = new Document("username", usuario.getUsername())
                .append("nombre", usuario.getNombre())
                .append("password", usuario.getPassword())
                .append("user",usuario.isUser())
                .append("_id", new ObjectId(usuario.getId()));
        //
        usuarios.findOneAndUpdate(filtro, new Document("$set", document));

        return usuario;
    }

    public boolean eliminandoUsuario(String username){
        //
        Usuario usuarioPorUsername = getUsuarioPorUsername(username);
        //Actualizando Documento.
        MongoCollection<Document> usuarios = mongoDbConexion.getBaseDatos().getCollection(TablasMongo.USUARIOS.getValor());
        //
        Document filtro = new Document("_id", new ObjectId(usuarioPorUsername.getId()));
        //
        return usuarios.findOneAndDelete(filtro) !=null;
    }

}
