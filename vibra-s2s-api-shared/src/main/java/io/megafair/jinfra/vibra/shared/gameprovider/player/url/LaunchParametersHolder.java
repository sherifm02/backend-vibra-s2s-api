package io.megafair.jinfra.vibra.shared.gameprovider.player.url;

import io.megafair.jinfra.vibra.shared.gameprovider.player.LaunchParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class LaunchParametersHolder {
    LaunchParameters payload;
    LaunchRequestParams requestHttp;
}
