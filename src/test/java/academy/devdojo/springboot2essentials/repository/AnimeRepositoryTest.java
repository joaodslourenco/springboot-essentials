package academy.devdojo.springboot2essentials.repository;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persists anime when successful")
    void save_PersistAnime_WhenSuccessful() {
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved("One Piece");
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(savedAnime).isNotNull();
        Assertions.assertThat(savedAnime.getId()).isNotNull();
        Assertions.assertThat(savedAnime.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    void save_UpdatesAnime_WhenSuccessful() {
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved("One Piece");
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        savedAnime.setName("Overlord");

        Anime updatedAnime = this.animeRepository.save(savedAnime);


        Assertions.assertThat(updatedAnime).isNotNull();
        Assertions.assertThat(updatedAnime.getId()).isNotNull();
        Assertions.assertThat(updatedAnime.getName()).isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful() {
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved("Naruto");
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(savedAnime);

        Optional<Anime> animeOptional = this.animeRepository.findById(savedAnime.getId());

        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Name returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful() {
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved("One Piece");
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        String name = savedAnime.getName();


        List<Anime> animeList = this.animeRepository.findByName(name);

        Assertions.assertThat(animeList)
                .isNotEmpty()
                .contains(savedAnime);

    }

    @Test
    @DisplayName("Find By Name returns empty list when no anime was found")
    void findByName_ReturnsEmptyList_WhenNoAnimeIsFound() {
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved("One Piece");
        Anime savedAnime = this.animeRepository.save(animeToBeSaved);

        String name = "Mob Psycho 100";

        List<Anime> animeList = this.animeRepository.findByName(name);

        Assertions.assertThat(animeList).isEmpty();
    }

    @Test
    @DisplayName("Save throws ConstraintViolationException when name is empty")
    void save_ThrowsConstraintViolationException_WhenNameIsEmpty() {
        Anime animeToBeSaved = new Anime();
//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(animeToBeSaved)).isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(animeToBeSaved))
                .withMessageContaining("The anime name cannot be empty.");

    }


}