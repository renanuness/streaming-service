package core.model.entity;

import core.model.valueObject.ArtistId;

import java.util.List;

public class Artist {
    private ArtistId id;
    private String name;
    private List<Genre> genres;

    public ArtistId id(){ return id; }
}
