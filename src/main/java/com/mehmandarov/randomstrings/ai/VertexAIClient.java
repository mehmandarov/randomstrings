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

@RegisterRestClient(configKey = "vertex-ai-api")
@Path("/v1/projects/{projectId}/locations/{location}/publishers/google/models/{model}:generateContent")
public interface VertexAIClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject generateContent(
            @PathParam("projectId") String projectId,
            @PathParam("location") String location,
            @PathParam("model") String model,
            @HeaderParam("Authorization") String authorization,
            JsonObject request
    );
}
