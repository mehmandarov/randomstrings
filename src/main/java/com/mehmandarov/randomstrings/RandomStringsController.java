package com.mehmandarov.randomstrings;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.openapi.annotations.Operation;


/**
 * Endpoints for generating random pairs using various methods.
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
    @Counted(name = "totalCountToRandomPairCalls",
            absolute = true,
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public String[] getRndString() {
        return rndStrSup.generateRandomStringsPair();
    }
}
