package io.megafair.jinfra.vibra.shared.gameprovider.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class BaseStartGameRequestHTTP extends BasePlayerOriginatedRequest {

}
