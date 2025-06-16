package io.megafair.jinfra.vibra.shared.gameprovider.player.impl;


import io.megafair.jinfra.auth.shared.service.AuthParametersExtractor;
import io.megafair.jinfra.foundation.currencyexchange.base.CurrencySettings;
import io.megafair.jinfra.foundation.extclient.base.AssociationServiceDataProvider;
import io.megafair.jinfra.foundation.feature.toggle.common.FeatureToggleService;
import io.megafair.jinfra.foundation.game.core.cache.management.LightCompositeGameCacheDataProvider;
import io.megafair.jinfra.foundation.geoip.LocationService;
import io.megafair.jinfra.foundation.geoip.validation.GeoValidator;
import io.megafair.jinfra.foundation.mf.contract.api.AnalyticsPlatformCredentialsProvider;
import io.megafair.jinfra.foundation.mf.contract.api.BaseStartGameAuthResource;
import io.megafair.jinfra.foundation.mf.contract.api.GameURLConfig;
import io.megafair.jinfra.foundation.mf.contract.api.S2SBackConfiguration;
import io.megafair.jinfra.foundation.mf.contract.api.S2SBackJWTService;
import io.megafair.jinfra.foundation.platform.contract.service.PlatformConfigService;
import io.megafair.jinfra.foundation.platform.contract.service.PlatformSecretService;
import io.megafair.jinfra.foundation.platform.contract.service.impl.AdditionalParamsHandler;
import io.megafair.jinfra.foundation.platform.vibra.contract.api.DynamicDataNames;
import io.megafair.jinfra.foundation.platform.vibra.contract.api.VibraRequestHeaderInfo;
import io.megafair.jinfra.foundation.platform.vibra.contract.api.request.RelayInitializeRequest;
import io.megafair.jinfra.foundation.platform.vibra.contract.api.request.RelayLaunchGameRequest;
import io.megafair.jinfra.foundation.platform.vibra.contract.api.response.RelayInitializeResponse;
import io.megafair.jinfra.foundation.platform.vibra.contract.api.response.RelayLaunchGameResponse;
import io.megafair.jinfra.foundation.platform.vibra.contract.auth.impl.VibraRelayInitializeGameAPIClientImpl;
import io.megafair.jinfra.foundation.platform.vibra.contract.auth.impl.VibraRelayLaunchGameAPIClientImpl;
import io.megafair.jinfra.foundation.platform.vibra.contract.common.VibraApiSupportedProtocols;
import io.megafair.jinfra.foundation.player.transaction.service.UnitConversionService;
import io.megafair.jinfra.foundation.tutorial.main.service.PlayerTutorialDataService;
import io.megafair.jinfra.foundation.user.registration.client.HttpUserRegistrationService;
import io.megafair.jinfra.foundation.utils.encoding.Base64Utils;
import io.megafair.jinfra.foundation.utils.logging.MDC;
import io.megafair.jinfra.foundation.web.base.domain.DomainConfig;
import io.megafair.jinfra.foundation.web.session.ExtSystemUser;
import io.megafair.jinfra.foundation.web.session.SessionService;
import io.megafair.jinfra.foundation.web.session.cookie.SecuredSessionCookie;
import io.megafair.jinfra.foundation.web.session.cookie.SessionCookieBuilder;
import io.megafair.jinfra.vibra.shared.gameprovider.player.LaunchParameters;
import io.megafair.jinfra.vibra.shared.gameprovider.player.StartGameAuthAPI;
import io.megafair.jinfra.vibra.shared.gameprovider.player.url.GetUrlData;
import io.megafair.jinfra.vibra.shared.gameprovider.player.url.LaunchParametersHolder;
import io.megafair.jinfra.vibra.shared.gameprovider.player.url.LaunchRequestParams;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;
import java.net.URI;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.megafair.jinfra.foundation.utils.logging.LogPropertyNames.EXT_USER_ID;

@Slf4j
public class VibraStartGameAuthResource extends BaseStartGameAuthResource<LaunchParametersHolder> implements StartGameAuthAPI {

    private final VibraRelayLaunchGameAPIClientImpl relayLaunchGameAPIClient;
    private final VibraRelayInitializeGameAPIClientImpl relayInitializeGameAPIClient;
    private final S2SBackJWTService s2sJwtService;
    private final S2SBackConfiguration s2sConfiguration;

    public VibraStartGameAuthResource(LocationService locationService,
                                      PlatformSecretService platformSecretService,
                                      SessionService sessionService,
                                      FeatureToggleService featureToggleService,
                                      PlatformConfigService platformConfigService,
                                      @ConfigProperty(name = "mf.is_local", defaultValue = "false") Boolean isLocal,
                                      GeoValidator geoValidator,
                                      AssociationServiceDataProvider associationServiceDataProvider,
                                      SessionCookieBuilder sessionCookieBuilder,
                                      PlayerTutorialDataService playerTutorialDataService,
                                      HttpUserRegistrationService userRegistrationService,
                                      DomainConfig domainConfig,
                                      GameURLConfig gameURLConfig,
                                      LightCompositeGameCacheDataProvider tournamentCacheDataProvider,
                                      AuthParametersExtractor authParametersExtractor,
                                      UnitConversionService unitConversionService,
                                      CurrencySettings currencySettings,
                                      AdditionalParamsHandler additionalParamsHandler,
                                      AnalyticsPlatformCredentialsProvider analyticsPlatformCredentials, VibraRelayLaunchGameAPIClientImpl relayLaunchGameAPIClient, VibraRelayInitializeGameAPIClientImpl relayInitializeGameAPIClient,
                                      S2SBackJWTService s2sJwtService, S2SBackConfiguration s2sConfiguration) {
        super(locationService, platformSecretService, sessionService, featureToggleService, platformConfigService, isLocal, geoValidator, associationServiceDataProvider, sessionCookieBuilder, playerTutorialDataService, userRegistrationService,
                domainConfig, gameURLConfig, tournamentCacheDataProvider, authParametersExtractor, unitConversionService, new VibraApiSupportedProtocols(), currencySettings, additionalParamsHandler, analyticsPlatformCredentials);
        this.relayLaunchGameAPIClient = relayLaunchGameAPIClient;
        this.relayInitializeGameAPIClient = relayInitializeGameAPIClient;
        this.s2sJwtService = s2sJwtService;
        this.s2sConfiguration = s2sConfiguration;
    }


    @Override
//    @POST
//    @Path("")
    public Response startGameAuth(LaunchParameters requestHTTP, LaunchRequestParams urlRequestParams, @Context HttpServerRequest httpRequest, HttpHeaders httpHeaders) {
        LaunchParametersHolder gameUrlRequestHolder = new LaunchParametersHolder(requestHTTP, urlRequestParams);
        return doAuthLogin(gameUrlRequestHolder, httpRequest, httpHeaders);
    }

    @Override
    protected void doAfterInitContext(StartGameContext<LaunchParametersHolder> startGameContext) {
    }

    @Override
    public ExtSystemUser isTokenValid(StartGameContext<LaunchParametersHolder> startGameContext) {

        var pay = startGameContext.getRequest().getPayload();
        RelayInitializeResponse authResp = authenticate(startGameContext);
        Optional<ExtSystemUser> extSystemUser;

        ExtSystemUser extUser = null;
        try {
            extUser = buildExtUser(authResp.getUserId(), authResp, pay.getGroupingSeparator().charAt(0), pay.getDecimalSeparator().charAt(0), startGameContext.getIntPlatformId(), startGameContext.getIntBrandId());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        extSystemUser = Optional.of(extUser);
        MDC.put(EXT_USER_ID, extUser.getExtUserId());

        return extSystemUser.get();
    }

    private ExtSystemUser buildExtUser(String userId, RelayInitializeResponse authResp, char groupingSeparator, char decimalSeparator, String platformId, String brandId) throws ParseException {


        Objects.requireNonNull(platformId, "platformId cannot be null");
        Objects.requireNonNull(brandId, "brandId cannot be null");

        Objects.requireNonNull(authResp, "authResp cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(authResp.getBalance(), "authResp.getBalance() cannot be null");
        Objects.requireNonNull(authResp.getCurrencyId(), "authResp.getCurrencyId() cannot be null");

        ExtSystemUser extSystemUser = new ExtSystemUser(
                userId,
                userId,
                BigDecimal.valueOf(Long.parseLong(authResp.getBalance())),
                authResp.getCurrencyId(),
                platformId,
                brandId,
                buildExtDynamicData(authResp, authResp.getSessionId())
        );
        return extSystemUser;
    }

    private Map<String, Object> buildExtDynamicData(RelayInitializeResponse resp, String sessionId) {
        Map<String, Object> map = new HashMap<>();
        if (resp.getToken() != null) {
            map.put(DynamicDataNames.TOKEN.getName(), resp.getToken());
        }
        if (sessionId != null) {
            map.put(DynamicDataNames.EXT_GAME_SESSION_ID.getName(), sessionId);
        }
        if (!map.isEmpty()) {
            return map;
        }

        return null;
    }

    private RelayInitializeResponse authenticate(StartGameContext<LaunchParametersHolder> startGameContext) {
        var launchGameRequest = new RelayLaunchGameRequest();
        launchGameRequest.setToken(startGameContext.getRequest().getPayload().getToken());
        RelayLaunchGameResponse launch = relayLaunchGameAPIClient.launch(buildVibraRequestHeaderInfo(startGameContext), launchGameRequest);

        log.info("relay launch response: {}", launch);
        if (launch.getIsInterrupted()) {
            log.info("There is unfinished game session");
        }

        var initReq = new RelayInitializeRequest();
        initReq.setToken(launch.getToken());
        initReq.setSessionId(launch.getSessionId());
        RelayInitializeResponse init = relayInitializeGameAPIClient.initialize(buildVibraRequestHeaderInfo(startGameContext), initReq);
        log.info("relay initialize response: {}", init);
        return init;
    }

    private VibraRequestHeaderInfo buildVibraRequestHeaderInfo(StartGameContext<LaunchParametersHolder> startGameContext) {
        VibraRequestHeaderInfo vibraRequestHeaderInfo = new VibraRequestHeaderInfo();
        vibraRequestHeaderInfo.setBrandId(startGameContext.getIntBrandId());
        return vibraRequestHeaderInfo;
    }

    @Override
    public String getEncryptSecretKeyId(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getRequestHttp().getEncryptSecretKeyId();
    }

    @Override
    public String getPredefinedTournamentIds(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getRequestHttp().getPredefinedTournamentIds();
    }

    @Override
    public String getPredefinedSegmentId(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getRequestHttp().getPredefinedSegmentId();
    }

    @Override
    public Boolean getSupportTrafficEncryption(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getRequestHttp().getSupportTrafficEncryption();
    }

    @Override
    protected String getLang(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getLocale();
    }

    @Override
    protected String getHomeUrl(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getPayload().getServerAddress();
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

    @Override
    protected SecuredSessionCookie getSecuredSessionCookie(LaunchParametersHolder startGameAuthRequestHTTP) {
        return startGameAuthRequestHTTP.getRequestHttp().getSecuredSessionCookie();
    }

    @Override
    protected String getAdditionalParams(LaunchParametersHolder startGameAuthRequestHTTP) {
        return null;
    }
}
