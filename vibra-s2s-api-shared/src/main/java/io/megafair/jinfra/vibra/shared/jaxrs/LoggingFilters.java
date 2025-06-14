package io.megafair.jinfra.vibra.shared.jaxrs;

import io.megafair.jinfra.foundation.utils.Strings;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Objects;

public class LoggingFilters {
    private static final Logger LOG = Logger.getLogger(LoggingFilters.class);
//    @ServerRequestFilter
    public void requestFilter(ContainerRequestContext reqCtx,
                              UriInfo info,
                              HttpServerRequest request,
                              RoutingContext routingContext) {
        String.format(
                "ContainerRequestContext: %s;" +
                        "UriInfo: %s;" +
                        "request: %s;" +
                        "RoutingContext: %s;" +
                        "RoutingContext RemoteAddress: %s;",
                reqCtx,
                info,
                request,
                routingContext,
                routingContext.request().remoteAddress()
        );

//        LOG.infof("ContainerRequestContext: %s", reqCtx);
//        LOG.infof("UriInfo: %s", info);
//        LOG.infof("request: %s", request);
//        LOG.infof("RoutingContext: %s", routingContext);
//        LOG.infof("RoutingContext RemoteAddress: %s", routingContext.request().remoteAddress());

        final String method = reqCtx.getMethod();
        final String path = info.getPath();
        MultivaluedMap<String, String> pathParams = info.getPathParameters();
        MultivaluedMap<String, String> queryParams = info.getQueryParameters();
        String address = null;
        MultiMap requestHeaders = null;
        if (request != null) {
            //https://github.com/quarkusio/quarkus/pull/25718

            address = "" + request.remoteAddress();
//        final String address = request.getRemoteAddress();
            requestHeaders = request.headers();
        }
        LOG.infof("Request %s %s pathParams:%s queryParams: %s headers: %s from IP: %s", method, path, pathParams, queryParams, requestHeaders, address);

    }

//    @ServerResponseFilter
    public void responseFilter(ContainerRequestContext reqCtx,
                               ContainerResponseContext respCtx,
                              UriInfo info,
                              HttpServerRequest request) {
        String.format(
                "ContainerRequestContext: %s;" +
                        "ContainerResponseContext: %s;" +
                        "UriInfo: %s;" +
                        "request: %s;",
                respCtx,
                reqCtx,
                info,
                request
        );


//        LOG.infof("ContainerRequestContext: %s", reqCtx);
//        LOG.infof("ContainerResponseContext: %s", respCtx);
//        LOG.infof("UriInfo: %s", info);
//        LOG.infof("request: %s", request);

        final String method = reqCtx.getMethod();
        final String path = info.getPath();
        MultivaluedMap<String, String> pathParams = info.getPathParameters();
        MultivaluedMap<String, String> queryParams = info.getQueryParameters();
        String address = null;
        MultiMap requestHeaders = null;
        if (request != null) {
            address = "" + request.remoteAddress();
//        final String address = request.getRemoteAddress();
            requestHeaders = request.headers();
        }
        MultivaluedMap<String, Object>  respHeaders = respCtx.getHeaders();
        String body = null;
        if (respCtx.hasEntity()) {
            Object ent = respCtx.getEntity();
            body = Objects.toString(ent);
            body = Strings.leftN(body, 100);
        }
        int respStatus = respCtx.getStatus();
        URI respLocation = respCtx.getLocation();
        LOG.infof("Response %s %s pathParams:%s queryParams: %s reqHeaders: %s respStatus: %d respLocation: %s respHeaders: %s body: %s from IP: %s",
                method, path, pathParams, queryParams, requestHeaders,
                respStatus, respLocation, respHeaders, body,
                address);

    }


}
