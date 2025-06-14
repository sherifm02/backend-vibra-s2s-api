package io.megafair.jinfra.vibra.shared.handlers;

import io.megafair.jinfra.foundation.geoip.exception.RestrictedLocationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

class ExceptionMappers {

    @ConfigProperty(name = "mf.geo.redirect.uri")
    String redirect;

    @ServerExceptionMapper
    public Response mapException(RestrictedLocationException x) {
        return Response
            .seeOther(UriBuilder.fromUri(redirect)
                .build())
            .entity(x.getMessage())
            .build();
    }
}
