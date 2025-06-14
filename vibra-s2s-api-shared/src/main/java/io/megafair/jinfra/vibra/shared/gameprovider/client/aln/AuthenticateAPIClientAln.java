package io.megafair.jinfra.vibra.shared.gameprovider.client.aln;


import io.megafair.jinfra.foundation.platform.wintech.contract.api.authenticate.WintechAuthenticateAPI;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey= AuthenticateAPIClientAln.CONFIG_KEY)
public interface AuthenticateAPIClientAln extends WintechAuthenticateAPI {
    String CONFIG_KEY = "authenticate-api-aln";
}