package com.mehmandarov.randomstrings.health;

import com.mehmandarov.randomstrings.RandomStringsSupplier;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class ServiceHealthCheck implements HealthCheck {

    @Inject
    RandomStringsSupplier rndStrSup;

    @Override
    public HealthCheckResponse call() {

        if (rndStrSup.generateRandomStringsPair().length == 2) {
            return HealthCheckResponse.named(ServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(ServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }
}
