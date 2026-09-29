package com.mehmandarov.randomstrings.ai;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.json.JsonObject;

@RegisterRestClient(configKey = "model-armor-api")
@Path("/v1/projects/{projectId}/locations/{location}/templates/{templateId}")
public interface ModelArmorClient {

    @POST
    @Path(":sanitizeUserPrompt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject sanitizeUserPrompt(
            @PathParam("projectId") String projectId,
            @PathParam("location") String location,
            @PathParam("templateId") String templateId,
            @HeaderParam("Authorization") String authorization,
            JsonObject request
    );

    @POST
    @Path(":sanitizeModelResponse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject sanitizeModelResponse(
            @PathParam("projectId") String projectId,
            @PathParam("location") String location,
            @PathParam("templateId") String templateId,
            @HeaderParam("Authorization") String authorization,
            JsonObject request
    );
}
