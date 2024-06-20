package academy.devdojo.springboot2essentials.util;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createAnimePutRequestBody(Long id, String name) {
        Anime validAnime = AnimeCreator.createValidAnime(id, name);
        return AnimePutRequestBody.builder().id(validAnime.getId()).name(validAnime.getName()).build();
    }
}
