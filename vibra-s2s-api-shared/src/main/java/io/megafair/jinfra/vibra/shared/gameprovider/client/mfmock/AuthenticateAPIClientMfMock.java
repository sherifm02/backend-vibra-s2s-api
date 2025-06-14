package io.megafair.jinfra.vibra.shared.gameprovider.client.mfmock;


import io.megafair.jinfra.foundation.platform.wintech.contract.api.authenticate.WintechAuthenticateAPI;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey= AuthenticateAPIClientMfMock.CONFIG_KEY)
public interface AuthenticateAPIClientMfMock extends WintechAuthenticateAPI {
    String CONFIG_KEY = "authenticate-api-mfmock";
}