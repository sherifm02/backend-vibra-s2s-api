package io.megafair.jinfra.vibra.shared.common;

import io.megafair.jinfra.foundation.security.keystore.PrivateKeyRepository;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
@Startup
public class EagerLoaderBean {

    private final PrivateKeyRepository privateKeyRepository;

    @Inject
    public EagerLoaderBean(PrivateKeyRepository privateKeyRepository) {
        this.privateKeyRepository = privateKeyRepository;
    }

    @PostConstruct
    public void init() {
        privateKeyRepository.getKey("");
    }
}
