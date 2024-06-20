package academy.devdojo.springboot2essentials.util;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;

public class AnimePostRequestBodyCreator {
    public static AnimePostRequestBody createAnimePostRequestBody(String name) {
        return AnimePostRequestBody.builder().name(AnimeCreator.createAnimeToBeSaved(name).getName()).build();
    }
}
