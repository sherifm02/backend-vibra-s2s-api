package io.megafair.jinfra.vibra.shared.gameprovider.player.url;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * GetUrlResponse - Model representing a response with a game launch URL
 */
@Data
public class GetUrlData {
    @NotNull
    @JsonProperty("game_url")
    private String gameUrl;
}
