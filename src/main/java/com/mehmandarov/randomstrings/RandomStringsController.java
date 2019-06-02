package com.mehmandarov.randomstrings;

import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    @Operation(summary = "Gets the a adjective noun pair",
               description = "The pair of one random adjective and one random noun is returned as an array.")
    @Metered(name = "numberOfCalls", unit = MetricUnits.MINUTES,
             description = "Metrics to monitor numbers of calls to get random string pairs.", absolute = true)
    @Counted(unit = MetricUnits.NONE,
            name = "totalCountToRandomPairCalls",
            absolute = true,
            monotonic = true,
            displayName = "Total count to random pair calls",
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public String[] getRndString() {
        return rndStrSup.generateRandomStringsPair();
    }
}
