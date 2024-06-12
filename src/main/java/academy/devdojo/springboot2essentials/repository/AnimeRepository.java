package academy.devdojo.springboot2essentials.repository;

import academy.devdojo.springboot2essentials.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class AnimeRepository {
    private static List<Anime> animes;

    static {
        animes = new ArrayList<>(List.of(new Anime(1L, "One Piece"), new Anime(2L, "Boku no Hero"), new Anime(3L, "Berserk")));
    }

    public List<Anime> listAll() {
        return animes;
    }

    public Anime findOneById(long id) {
        return animes.stream().filter(anime -> anime.getId().equals(id)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found."));
    }

    public Anime save(Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(3, 100000));
        animes.add(anime);

        return anime;
    }
}
