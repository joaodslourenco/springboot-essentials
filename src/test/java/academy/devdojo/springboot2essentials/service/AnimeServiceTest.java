package academy.devdojo.springboot2essentials.service;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.exception.BadRequestException;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.requests.AnimePutRequestBody;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup() {
        List<Anime> animeList = List.of(
                AnimeCreator.createValidAnime(1L, "One Piece"),
                AnimeCreator.createValidAnime(2L, "Mob Psycho 100")
        );

        PageImpl<Anime> animePage = new PageImpl<>(animeList);

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(animeList);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime(1L, "One Piece")));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime(1L, "One Piece")));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime(1L, "One Piece"));

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("list returns list of animes inside page object when successful")
    void listAll_ReturnsListOfAnimesInsidePageObj_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime(1L, "One Piece").getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage).isNotEmpty().hasSize(2);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("list All returns list of animes when successful")
    void listAllNonPageable_ReturnsListOfAnimes_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime(1L, "One Piece").getName();

        List<Anime> animeList = animeService.listAllNonPageable();

        Assertions.assertThat(animeList).isNotNull().isNotEmpty();
        Assertions.assertThat(animeList).hasSize(2);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successful")
    void findByIdOrThrowBadRequestException_ReturnsAnime_WhenSuccessful() {
        Long expectedId = AnimeCreator.createValidAnime(1L, "One Piece").getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws Bad Request Exception when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(2L));
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        String expectedName = AnimeCreator.createValidAnime(1L, "One Piece").getName();

        List<Anime> animeList = animeService.findByName("anime");

        Assertions.assertThat(animeList).isNotNull().isNotEmpty();
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns empty list of anime when none was found")
    void findByName_ReturnsEmptyListOfAnime_WhenNoneIsFound() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(Collections.emptyList());

        List<Anime> animeList = animeService.findByName("anime");

        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody("One Piece");

        Anime anime = animeService.save(animePostRequestBody);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime).isEqualTo(AnimeCreator.createValidAnime(1L, "One Piece"));
    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful() {
        AnimePutRequestBody animePutRequestBody = AnimePutRequestBodyCreator.createAnimePutRequestBody(1L, "One Piece");
        Assertions.assertThatCode(() -> animeService.replace(animePutRequestBody)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Assertions.assertThatCode(() -> animeService.delete(1)).doesNotThrowAnyException();
    }
}