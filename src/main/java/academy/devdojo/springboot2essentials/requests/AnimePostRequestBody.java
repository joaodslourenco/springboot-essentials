package academy.devdojo.springboot2essentials.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class AnimePostRequestBody {
    @NotEmpty(message = "The anime name cannot be empty.")
    private String name;

    @URL(message = "The URL is not valid.")
    private String url;
}

