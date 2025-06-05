package com.mehmandarov.randomstrings.apidemo;

import com.mehmandarov.randomstrings.RandomStringsSupplier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.net.URI;


/**
 * Endpoints for generating random pairs using various methods.
 */
@Path("/rnd")
@ApplicationScoped
public class RandomStringsAPIDemoController {

    @Inject
    RandomStringsSupplier rndStrSup;

    // <<<<< The code below is intended to serve as a DEMO for various API versioning strategies ONLY. >>>>>

    /**
    * A demo of the path versioning approach of the API.
    */
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

    /**
     * A demo of an API end-point deprecation.
     */
    @GET
    @Path("v0.1/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(deprecated = true,
            summary = "DEPRECATED. Use v2 now. Returns the adjective-noun pair",
            description = "Deprecated function. The pair of one random adjective and one random noun is returned as an array.")
    @Counted(name = "totalCountToRandomPairCalls_Versioned_Path_DEPRECATED",
            absolute = true,
            description = "Deprecated function call: Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    @Deprecated
    public Response getRndStringPathDeprecated() {
        URI newVersionURI = UriBuilder.fromUri("/api/rnd/v2/").build();
        Link newVersionLink = Link.fromUri(newVersionURI).rel("alternate").build();
        return Response.ok(rndStrSup.generateRandomStringsPair(), MediaType.APPLICATION_JSON)
                .header(jakarta.ws.rs.core.HttpHeaders.LINK, newVersionLink.toString())
                .header("X-API-Version", "0.1")
                .build();
    }

    /**
     * A demo of header based versioning of the API end-points.
     */
    @GET
    @Path("/versioned")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Returns the adjective-noun pair using *header versioning*",
            description = "The pair of one random adjective and one random noun is returned as an array.")
    @Counted(name = "totalCountToRandomPairCalls_Versioned_RequestHeader",
            absolute = true,
            description = "Total number of calls to random string pairs.",
            tags = {"calls=pairs"})
    public Response getRndStringV2Header(@HeaderParam("Accept-Version") String apiVersion) {
        // Fallback to regular API endpoint method if no apiVersion header is provided.
        if (apiVersion == null || apiVersion.isEmpty()) {
            return Response.ok(rndStrSup.generateRandomStringsPair(), MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("This v2 using *header versioning* of the API is not implemented. Version: " + apiVersion)
                .build();
    }

    /**
     * A demo of media type based versioning of the API end-points using multiple versions.
     */
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

    /**
     * A demo of media type based versioning of the API end-points.
     */
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
