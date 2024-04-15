package org.example.encapsulaciones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Visitante {
	@Id
	private String id;

	@Reference
	private List<ShortURL> urlList;

    private static Visitante instance;
    public static Visitante getInstance(){
        if(instance == null){
            instance = new Visitante();
        }
        return instance;
    }

	public Visitante(String uuid) {
		this.id = uuid;
		this.urlList = new ArrayList<>();
	}

	public Visitante(){
		this.urlList = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public List<ShortURL> getUrlList() {
		return urlList;
	}
}
