package io.megafair.jinfra.vibra.shared.jaxrs;

import io.megafair.jinfra.foundation.utils.Strings;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

//@Provider
//https://quarkus.io/guides/resteasy-reactive#request-or-response-filters
public class LoggingResponseFilter implements ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(LoggingResponseFilter.class);

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;
//    org.jboss.resteasy.spi.HttpRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        final String method = requestContext.getMethod();
        final String path = info.getPath();
        MultivaluedMap<String, String> pathParams = info.getPathParameters();
        MultivaluedMap<String, String> queryParams = info.getQueryParameters();
        final String address = request.remoteAddress().toString();
//        final String address = request.getRemoteAddress();
        MultiMap requestHeaders = request.headers();
        MultivaluedMap<String, Object>  respHeaders = responseContext.getHeaders();
        String body = null;
        if (responseContext.hasEntity()) {
            Object ent = responseContext.getEntity();
            body = Objects.toString(ent);
            body = Strings.leftN(body, 100);
        }
        int respStatus = responseContext.getStatus();
        URI respLocation = responseContext.getLocation();
        LOG.infof("Response %s %s pathParams:%s queryParams: %s reqHeaders: %s respStatus: %d respLocation: %s respHeaders: %s body: %s from IP: %s",
                method, path, pathParams, queryParams, requestHeaders,
                respStatus, respLocation, respHeaders, body,
                address);

    }
}
