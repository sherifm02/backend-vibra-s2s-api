package io.megafair.jinfra.vibra.shared.gameprovider.player.url;

import io.megafair.jinfra.foundation.mf.contract.api.utils.MFParameterNames;
import io.megafair.jinfra.foundation.web.session.cookie.SecuredSessionCookie;
import io.megafair.jinfra.foundation.web.session.cookie.SessionCookieBuilder;
import io.megafair.jinfra.foundation.web.session.cookie.SharedSessionCookie;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.QueryParam;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LaunchRequestParams {

    @QueryParam(MFParameterNames.MF_SUPPORT_TRAFFIC_ENCRYPTION_PARAMETER_NAME)
    //TODO: This param is added for debug.
    private Boolean supportTrafficEncryption;

    @QueryParam(MFParameterNames.MF_PREDEFINED_SEGMENT_ID_PARAMETER_NAME)
    //TODO: This param is added for debug.
    private String predefinedSegmentId;

    @QueryParam(MFParameterNames.MF_PREDEFINED_TOURNAMENT_ID_PARAMETER_NAME)
    //TODO: This param is added for debug.
    private String predefinedTournamentIds;

    //TODO: The logic with key will be refactored
    private String encryptSecretKeyId = "1";

    @CookieParam(SessionCookieBuilder.MF_SESSION_COOKIE_NAME)
    SecuredSessionCookie securedSessionCookie;

    @CookieParam(SessionCookieBuilder.MF_SHARED_SESSION_COOKIE_NAME)
    SharedSessionCookie sharedSessionCookie;

}
