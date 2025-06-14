package io.megafair.jinfra.vibra.shared.gameprovider.platform;

import io.megafair.jinfra.vibra.shared.gameprovider.platform.dto.GameListResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

@Path("/gamelist.do")
public class GameListResource {
    private static final Logger LOG = Logger.getLogger(GameListResource.class);

    @Produces(value = MediaType.APPLICATION_XML)
    @GET
    public GameListResponse getGameList(@QueryParam("bankId") String bankId) {

        LOG.infov("getGameList bankId:{0}", bankId);
        return new GameListResponse();
    }

}
