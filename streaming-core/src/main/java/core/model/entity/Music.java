package core.model.entity;

import core.model.valueObject.MusicDuration;
import core.model.valueObject.MusicId;

import java.util.Objects;

public class Music {
    public MusicId id;
    public Artist artist;
    public String title;
    public Genre genre;
    public MusicDuration duration;

    public Music(MusicId id, String title, Artist artist, Genre genre, MusicDuration duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
    }

    public MusicId id() { return id; }
    public String title() { return title; }
    public Artist artist() { return artist; }
    public Genre genre() { return genre; }
    public MusicDuration duration() { return duration; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Music music)) return false;
        return id.equals(music.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
