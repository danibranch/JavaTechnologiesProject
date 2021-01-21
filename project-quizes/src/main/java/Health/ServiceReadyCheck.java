package Health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;


public class ServiceReadyCheck implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named(ServiceReadyCheck.class.getSimpleName()).withData("ready",true).up().build();
    }
}
