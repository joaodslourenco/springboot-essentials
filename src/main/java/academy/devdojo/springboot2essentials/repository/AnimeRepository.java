package academy.devdojo.springboot2essentials.repository;

import academy.devdojo.springboot2essentials.domain.Anime;

import java.util.List;

public class AnimeRepository {
    public List<Anime> listALl() {
        return List.of(new Anime(1L, "Boku no Hero"), new Anime(2L, "Berserk"), new Anime(3L, "One Piece"));
    }
}
