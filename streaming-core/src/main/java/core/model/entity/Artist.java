package core.model.entity;

import core.model.valueObject.ArtistId;

import java.util.List;

public class Artist {
    private ArtistId id;
    private String name;

    public Artist(ArtistId id, String name) {
        this.id = id;
        this.name = name;
    }

    public ArtistId id(){ return id; }
    public String name() { return name; }
}
