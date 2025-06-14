package io.megafair.jinfra.vibra.shared;

import io.megafair.jinfra.vibra.shared.gameprovider.player.url.GetUrlData;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {GetUrlData.class, })
public class ReflectionConfiguration {
}
