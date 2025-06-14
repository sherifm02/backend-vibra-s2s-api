package io.megafair.jinfra.vibra.shared.gameprovider.player.impl;


import io.megafair.jinfra.foundation.extclient.base.AssociationServiceDataProvider;
import io.megafair.jinfra.foundation.geoip.LocationService;
import io.megafair.jinfra.foundation.geoip.validation.GeoValidator;
import io.megafair.jinfra.foundation.mf.contract.api.AnalyticsPlatformCredentialsProvider;
import io.megafair.jinfra.foundation.mf.contract.api.BaseStartGameGuestResource;
import io.megafair.jinfra.foundation.mf.contract.api.GameURLConfig;
import io.megafair.jinfra.foundation.mf.contract.api.S2SBackConfiguration;
import io.megafair.jinfra.foundation.mf.contract.api.S2SBackJWTService;
import io.megafair.jinfra.foundation.platform.contract.service.PlatformSecretService;
import io.megafair.jinfra.foundation.platform.contract.service.impl.AdditionalParamsHandler;
import io.megafair.jinfra.foundation.platform.wintech.contract.common.WintechApiSupportedProtocols;
import io.megafair.jinfra.foundation.platform.wintech.contract.common.WintechAssociationServiceConsumerContextProvider;
import io.megafair.jinfra.foundation.tournament.rt.service.cache.LightTournamentCacheDataProvider;
import io.megafair.jinfra.foundation.utils.encoding.Base64Utils;
import io.megafair.jinfra.foundation.web.base.domain.DomainConfig;
import io.megafair.jinfra.foundation.web.session.ExtSystemUser;
import io.megafair.jinfra.foundation.web.session.cookie.SecuredSessionCookie;
import io.megafair.jinfra.foundation.web.session.cookie.SessionCookieBuilder;
import io.megafair.jinfra.vibra.shared.gameprovider.player.LaunchParameters;
import io.megafair.jinfra.vibra.shared.gameprovider.player.StartGameGuestAPI;
import io.megafair.jinfra.vibra.shared.gameprovider.player.url.GetUrlData;
import io.megafair.jinfra.vibra.shared.gameprovider.player.url.LaunchParametersHolder;
import io.megafair.jinfra.vibra.shared.gameprovider.player.url.LaunchRequestParams;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class WintechStartGameGuestResource extends BaseStartGameGuestResource<LaunchParametersHolder> implements StartGameGuestAPI {

    private final WintechAssociationServiceConsumerContextProvider wintechAssociationServiceContextProvider;
    private final S2SBackJWTService s2sJwtService;
    private final S2SBackConfiguration s2sConfiguration;
    public WintechStartGameGuestResource(LocationService locationService,
                                         PlatformSecretService platformSecretService,
                                         @ConfigProperty(name = "mf.is_local", defaultValue = "false") Boolean isLocal,
                                         GeoValidator geoValidator,
                                         AssociationServiceDataProvider associationServiceDataProvider,
                                         SessionCookieBuilder sessionCookieBuilder,
                                         DomainConfig domainConfig,
                                         GameURLConfig gameURLConfig,
                                         LightTournamentCacheDataProvider tournamentCacheDataProvider,
                                         AdditionalParamsHandler additionalParamsHandler,
                                         AnalyticsPlatformCredentialsProvider analyticsPlatformCredentials, WintechAssociationServiceConsumerContextProvider wintechAssociationServiceContextProvider, S2SBackJWTService s2sJwtService, S2SBackConfiguration s2sConfiguration) {
        super(locationService, platformSecretService, isLocal, geoValidator, associationServiceDataProvider, sessionCookieBuilder,
                domainConfig, gameURLConfig, tournamentCacheDataProvider, new WintechApiSupportedProtocols(), additionalParamsHandler, analyticsPlatformCredentials);
        this.wintechAssociationServiceContextProvider = wintechAssociationServiceContextProvider;
        this.s2sJwtService = s2sJwtService;
        this.s2sConfiguration = s2sConfiguration;
    }


    @Override
    public Response guestLogin(LaunchParameters requestHTTP, LaunchRequestParams requestParams, @Context HttpServerRequest httpRequest, HttpHeaders httpHeaders) {
        return doGuestLogin(new LaunchParametersHolder(requestHTTP, requestParams), httpRequest, httpHeaders);
    }

    @Override
    protected void doAfterInitContext(StartGameContext<LaunchParametersHolder> startGameContext) {
    }

    public ExtSystemUser isTokenValid(StartGameContext<LaunchParametersHolder> startGameContext) {
        String userIdFromPrevCookie = "na_" + UUID.randomUUID();
        ExtSystemUser extSystemUser = new ExtSystemUser();
        extSystemUser.setBrandId(startGameContext.getIntBrandId());
        extSystemUser.setPlatformId(startGameContext.getIntPlatformId());
        extSystemUser.setExtUserId(userIdFromPrevCookie);
        return extSystemUser;
    }

    @Override
    protected String getLang(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getLocale();
    }

    @Override
    protected String getHomeUrl(LaunchParametersHolder startGameAuthRequestHTTP) {
        return null;
    }

    @Override
    protected String getToken(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getToken();
    }

    @Override
    protected String getExtBrandId(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getCasinoSiteId();
    }

    @Override
    protected String getExtGameId(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getGameId();
    }

    @Override
    protected String getMode(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getGameMode();
    }

    @Override
    protected SecuredSessionCookie getSecuredSessionCookie(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getRequestHttp().getSecuredSessionCookie();
    }

    @Override
    protected String getAdditionalParams(LaunchParametersHolder startGameGuestRequestHTTP) {
//        return startGameGuestRequestHTTP.getRequestHttp().getAdditionalParams();
        return null;
    }


    @Override
    protected Response buildResponse(StartGameContext<LaunchParametersHolder> startGameContext) {
        URI newLocation = buildNewLocation(startGameContext);
        GetUrlData getUrlData = new GetUrlData();
        getUrlData.setGameUrl(newLocation.toString());
        Response resp = Response
                .status(Response.Status.OK)
                .cacheControl(noCache())
                .entity(getUrlData)
                .build();
        return resp;
    }

    @Override
    protected URI buildNewLocation(StartGameContext<LaunchParametersHolder> startGameContext) {
        URI newLocation = super.buildNewLocation(startGameContext);
        String openGameUrl = newLocation.toString();
        String encodedOpenGameUrl = Base64Utils.encode(openGameUrl);
        Map<String, String> initDataParams = new HashMap<>();
        initDataParams.put("openGameUrl", encodedOpenGameUrl);
        initDataParams.put("jwt", s2sJwtService.generateToken());
        String initDataUrl = s2sConfiguration.getFrontApiUrl();
        return URI.create(addQueryParams(initDataUrl, initDataParams));
    }
}
