package academy.devdojo.springboot2essentials.client;

import academy.devdojo.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);


        Anime[] anime = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);

        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
        });

        log.info(exchange.getBody());

//        Anime kingdom = Anime.builder().name("Kingdom").build();
//
//        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
//        log.info("Saved anime: {}", kingdomSaved);

        Anime samurai = Anime.builder().name("Samurai Champloo").build();

        ResponseEntity<Anime> samuraiSaved = new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.POST, new HttpEntity<>(samurai, createJsonHeader()), new ParameterizedTypeReference<Anime>() {
        });
        log.info("Saved anime: {}", samuraiSaved);

        
        Anime animeToBeUpdated = samuraiSaved.getBody();

        animeToBeUpdated.setName("Samurai Champloo 2");


        ResponseEntity<Void> samuraiUpdated = new RestTemplate().exchange("http://localhost:8080/animes", HttpMethod.PUT, new HttpEntity<>(samurai, createJsonHeader()), Void.class);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
