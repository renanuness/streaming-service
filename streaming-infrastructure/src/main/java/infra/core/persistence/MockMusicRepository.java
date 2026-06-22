package infra.core.persistence;

import core.model.entity.Artist;
import core.model.entity.Genre;
import core.model.entity.Music;
import core.model.valueObject.ArtistId;
import core.model.valueObject.MusicDuration;
import core.model.valueObject.MusicId;
import core.repository.MusicRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MockMusicRepository implements MusicRepository {

    private final List<Music> musicCatalog;

    public MockMusicRepository() {
        this.musicCatalog = createMockCatalog();
    }

    @Override
    public Optional<Music> findById(MusicId id) {
        return musicCatalog.stream()
                .filter(m -> m.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Music> searchByTitle(String query) {
        return musicCatalog.stream()
                .filter(m -> m.title().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List<Music> createMockCatalog() {
        List<Music> catalog = new ArrayList<>();

        // Artistas
        Artist artist1 = new Artist(new ArtistId("artist-1"), "Queen");
        Artist artist2 = new Artist(new ArtistId("artist-2"), "The Beatles");
        Artist artist3 = new Artist(new ArtistId("artist-3"), "Pink Floyd");
        Artist artist4 = new Artist(new ArtistId("artist-4"), "Michael Jackson");
        Artist artist5 = new Artist(new ArtistId("artist-5"), "AC/DC");

        // Gêneros
        Genre rock = new Genre("Rock");
        Genre pop = new Genre("Pop");
        Genre progressive = new Genre("Progressive Rock");
        Genre hardRock = new Genre("Hard Rock");

        // Queen
        catalog.add(new Music(
                new MusicId("music-1"), "Bohemian Rhapsody", artist1, rock,
                new MusicDuration(355)
        ));
        catalog.add(new Music(
                new MusicId("music-2"), "Don't Stop Me Now", artist1, rock,
                new MusicDuration(209)
        ));
        catalog.add(new Music(
                new MusicId("music-3"), "We Will Rock You", artist1, rock,
                new MusicDuration(122)
        ));

        // The Beatles
        catalog.add(new Music(
                new MusicId("music-4"), "Hey Jude", artist2, rock,
                new MusicDuration(431)
        ));
        catalog.add(new Music(
                new MusicId("music-5"), "Let It Be", artist2, rock,
                new MusicDuration(243)
        ));
        catalog.add(new Music(
                new MusicId("music-6"), "Yesterday", artist2, pop,
                new MusicDuration(125)
        ));

        // Pink Floyd
        catalog.add(new Music(
                new MusicId("music-7"), "Comfortably Numb", artist3, progressive,
                new MusicDuration(383)
        ));
        catalog.add(new Music(
                new MusicId("music-8"), "Wish You Were Here", artist3, progressive,
                new MusicDuration(334)
        ));

        // Michael Jackson
        catalog.add(new Music(
                new MusicId("music-9"), "Billie Jean", artist4, pop,
                new MusicDuration(294)
        ));
        catalog.add(new Music(
                new MusicId("music-10"), "Thriller", artist4, pop,
                new MusicDuration(357)
        ));

        // AC/DC
        catalog.add(new Music(
                new MusicId("music-11"), "Back in Black", artist5, hardRock,
                new MusicDuration(255)
        ));
        catalog.add(new Music(
                new MusicId("music-12"), "Highway to Hell", artist5, hardRock,
                new MusicDuration(208)
        ));

        return catalog;
    }
}
