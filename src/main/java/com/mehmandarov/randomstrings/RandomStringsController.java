package com.mehmandarov.randomstrings;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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


    // <<<<< The code below is a DEMO for API versioning only >>>>>

    @GET
    @Path("v2/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Returns the adjective-noun pair",
            description = "The pair of one random adjective and one random noun is returned as an array.")
    @Counted(name = "totalCountToRandomPairCalls_Versioned_Path",
            absolute = true,
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public Response getRndStringV2path() {
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("This v2 using *path versioning* of the API is not implemented.")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Returns the adjective-noun pair using *header versioning*",
            description = "The pair of one random adjective and one random noun is returned as an array.")
    @Counted(name = "totalCountToRandomPairCalls_Versioned_RequestHeader",
            absolute = true,
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public Response getRndStringV2Header(@HeaderParam("Accept-Version") String apiVersion) {
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("This v2 using *header versioning* of the API is not implemented. Version: " + apiVersion)
                .build();
    }

    @GET
    @Produces({"application/rnd.v3+json", "application/rnd.v4+json"})
    @Operation(summary = "Returns the adjective-noun pair using *media type versioning*",
            description = "The pair of one random adjective and one random noun is returned as an array.")
    @Counted(name = "totalCountToRandomPairCalls_Versioned_MediaType_V3V4",
            absolute = true,
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public Response getRndStringV3V4MediaType() {
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("This v3 and v4 using *media type versioning* of the API is not implemented.")
                .build();
    }

    @GET
    @Produces({"application/rnd.v5+json"})
    @Operation(summary = "Returns the adjective-noun pair using *media type versioning*",
            description = "The pair of one random adjective and one random noun is returned as an array.")
    @Counted(name = "totalCountToRandomPairCalls_Versioned_MediaType_V5",
            absolute = true,
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public String[] getRndStringV5MediaType() {
        return rndStrSup.generateRandomStringsPair();
    }

}
