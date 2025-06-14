package io.megafair.jinfra.vibra.shared.gameprovider.player;

import io.megafair.jinfra.vibra.shared.gameprovider.player.url.LaunchRequestParams;
import io.vertx.core.http.HttpServerRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(StartGameAuthAPI.API_PATH)
public interface StartGameAuthAPI extends StartGameAPI {
    String API_PATH = "/cwstartgamev2.do";

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response startGameAuth(@Valid LaunchParameters requestHTTP, @Valid @BeanParam LaunchRequestParams urlRequestParams, @Context HttpServerRequest httpRequest, HttpHeaders httpHeaders);
}
