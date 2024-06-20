package academy.devdojo.springboot2essentials.controller;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.requests.AnimePutRequestBody;
import academy.devdojo.springboot2essentials.service.AnimeService;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import academy.devdojo.springboot2essentials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essentials.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setup() {
        List<Anime> animeList = List.of(
                AnimeCreator.createValidAnime(1L, "One Piece"),
                AnimeCreator.createValidAnime(2L, "Mob Psycho 100")
        );

        PageImpl<Anime> animePage = new PageImpl<>(animeList);

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())).thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable()).thenReturn(animeList);

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime(1L, "One Piece"));

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime(1L, "One Piece")));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime(1L, "One Piece"));

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of animes inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObj_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime(1L, "One Piece").getName();

        Page<Anime> animePage = animeController.list(PageRequest.of(1, 1)).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage).isNotEmpty().hasSize(2);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("list All returns list of animes when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime(1L, "One Piece").getName();

        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty();
        Assertions.assertThat(animeList).hasSize(2);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime(1L, "One Piece").getId();

        Anime anime = animeController.findOne(1L).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime(1L, "One Piece").getName();

        List<Anime> animeList = animeController.findByName("anime").getBody();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when none was found")
    void findByName_ReturnsEmptyListOfAnime_WhenNoneIsFound() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        List<Anime> animeList = animeController.findByName("anime").getBody();

        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody("One Piece");

        Anime anime = animeController.save(animePostRequestBody).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isEqualTo(AnimeCreator.createValidAnime(1L, "One Piece"));
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {
        AnimePutRequestBody animePutRequestBody = AnimePutRequestBodyCreator.createAnimePutRequestBody(1L, "One Piece");
        Assertions.assertThatCode(() -> animeController.replace(animePutRequestBody)).doesNotThrowAnyException();


        ResponseEntity<Void> entity = animeController.replace(animePutRequestBody);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {

        Assertions.assertThatCode(() -> animeController.delete(1)).doesNotThrowAnyException();


        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}