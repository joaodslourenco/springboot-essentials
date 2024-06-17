package academy.devdojo.springboot2essentials.client;

import academy.devdojo.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        String url = "http://localhost:8080/animes/{id}";

        ResponseEntity<Anime> entity = new RestTemplate().getForEntity(url, Anime.class, 2);
        log.info(entity);


        Anime anime = new RestTemplate().getForObject(url, Anime.class, 1);

        log.info(anime);
    }
}