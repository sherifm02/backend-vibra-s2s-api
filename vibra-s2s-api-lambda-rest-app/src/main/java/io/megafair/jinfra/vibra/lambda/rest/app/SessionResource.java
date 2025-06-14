package io.megafair.jinfra.vibra.lambda.rest.app;

import io.megafair.jinfra.foundation.geoip.LocationParams;
import io.megafair.jinfra.foundation.geoip.LocationService;
import io.megafair.jinfra.foundation.web.session.SessionService;
import io.megafair.jinfra.foundation.web.session.WebSession;
import io.megafair.jinfra.foundation.geoip.GeoIP;

import io.quarkus.amazon.lambda.http.model.AwsProxyRequest;
import org.jboss.logging.Logger;
import io.vertx.core.http.HttpServerRequest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import java.util.Map;
import java.util.Optional;

@Path("/session")
public class SessionResource {
    private static final Logger LOG = Logger.getLogger(SessionResource.class);

    @Inject
    LocationService locationService;
    public SessionResource(SessionService sessionService) {
        this.sessionService = sessionService;
        LOG.infov("SessionResource created with sessionService: {0}", sessionService);
    }

    SessionService sessionService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "test";
    }

    @GET
    @Path("/sess/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getSession(@PathParam("id") String sessionId) {
        Optional<WebSession> sess = sessionService.getSession(sessionId);
        Response resp = Response
                .status(Response.Status.NOT_FOUND)
                .entity("NOT FOUND:" + sessionId)
                .build();
        if (sess.isPresent()) {
            resp = Response
                    .ok(sess.get())
                    .build();
        }
        return resp;
    }

    @GET
    @Path("/headers")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getHeaders(@Context HttpHeaders headers,
//                               @Context RoutingContext routingContext,
                               @Context HttpServerRequest request,
                               @Context AwsProxyRequest awsProxyRequest) {

        MultivaluedMap<String, String> reqHeaders = headers.getRequestHeaders();
        Map<String, Cookie> cookies = headers.getCookies();

        StringBuilder sb = new StringBuilder();
        sb.append( "headers:")
                .append(reqHeaders)
                .append("|||")
                .append("Cookies:")
                .append(cookies)
                .append("|||")
                .append("HttpRequest:")
                .append(request.remoteAddress())
                .append("|||")
                .append("awsProxyRequest:")
                .append(awsProxyRequest.getMultiValueHeaders())
            ;



        Response resp = Response
                .ok(sb.toString())
                .build();

        return resp;
    }

    @GET
    @Path("/geo/{ip}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response geoIP(@PathParam("ip") String ip,
                          @Context HttpHeaders httpHeaders) {
        LOG.infov("Requesting geoip for: {0}", ip);
        GeoIP geoIP = locationService.resolveLocation(
                LocationParams.builder()
                        .headers(httpHeaders)
                        .hostAddress(ip)
                        .build()
        );
        LOG.infov("Gor result geoip: {0}", geoIP);
        Response resp = Response
                .ok(geoIP)
                .build();
        return resp;
    }
}
