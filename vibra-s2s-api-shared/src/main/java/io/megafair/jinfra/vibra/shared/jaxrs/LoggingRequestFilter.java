package io.megafair.jinfra.vibra.shared.jaxrs;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;

//@Provider
public class LoggingRequestFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(LoggingRequestFilter.class);

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;
//    HttpRequest request;

    @Override
    public void filter(ContainerRequestContext context) {

        final String method = context.getMethod();
        final String path = info.getPath();
        MultivaluedMap<String, String> pathParams = info.getPathParameters();
        MultivaluedMap<String, String> queryParams = info.getQueryParameters();
        final String address = request.remoteAddress().toString();
//        final String address = request.getRemoteAddress();
        MultiMap headers = request.headers();

        LOG.infof("Request %s %s pathParams:%s queryParams: %s headers: %s from IP: %s", method, path, pathParams, queryParams, headers, address);
    }
}
