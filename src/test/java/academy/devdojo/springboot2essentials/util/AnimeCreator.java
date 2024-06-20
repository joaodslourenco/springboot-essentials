package academy.devdojo.springboot2essentials.util;

import academy.devdojo.springboot2essentials.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved(String name) {
        return Anime.builder().name(name).build();
    }


    public static Anime createValidAnime(long id, String name) {
        return Anime.builder().id(id).name(name).build();
    }


    private static Anime createValidUpdatedAnime(String name) {
        return Anime.builder().name(name).build();
    }

}
