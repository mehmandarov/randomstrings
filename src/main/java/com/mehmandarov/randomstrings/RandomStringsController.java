package com.mehmandarov.randomstrings;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.openapi.annotations.Operation;



/**
 *
 */
@Path("/rnd")
@ApplicationScoped
public class RandomStringsController {

    @Inject
    RandomStringsSupplier rndStrSup;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Returns the adjective-noun pair",
               description = "The pair of one random adjective and one random noun is returned as an array.")
    @Metered(name = "numberOfCalls", unit = MetricUnits.MINUTES,
             description = "Metrics to monitor numbers of calls to get random string pairs.", absolute = true)
    @Counted(unit = MetricUnits.NONE,
            name = "totalCountToRandomPairCalls",
            absolute = true,
            displayName = "Total count to random pair calls",
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public String[] getRndString() {
        return rndStrSup.generateRandomStringsPair();
    }
}
