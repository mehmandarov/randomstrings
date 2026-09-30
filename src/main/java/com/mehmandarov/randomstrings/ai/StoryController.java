package com.mehmandarov.randomstrings.ai;

import com.mehmandarov.randomstrings.RandomStringsSupplier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.metrics.annotation.Counted;
import java.io.IOException;

@Path("/story")
@ApplicationScoped
public class StoryController {

    @Inject
    RandomStringsSupplier rndStrSup;

    @Inject
    @RestClient
    VertexAIClient vertexAIClient;

    @Inject
    @RestClient
    ModelArmorClient modelArmorClient;

    @Inject
    AuthService authService;

    @Inject
    @ConfigProperty(name = "ai.project.id", defaultValue = "my-project-id")
    String projectId;

    @Inject
    @ConfigProperty(name = "ai.location", defaultValue = "us-central1")
    String location;

    @Inject
    @ConfigProperty(name = "ai.model", defaultValue = "gemini-1.5-flash")
    String model;

    @Inject
    @ConfigProperty(name = "model.armor.enabled", defaultValue = "false")
    boolean modelArmorEnabled;

    @Inject
    @ConfigProperty(name = "model.armor.template", defaultValue = "default-template")
    String templateId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generate a story using Vertex AI",
               description = "Generates a 3-sentence story based on a random adjective-noun pair and user input.")
    @Counted(name = "totalCountToStoryCalls",
             absolute = true,
             description = "Total number of calls to story generation.",
             tags = {"calls=story"})
    public JsonObject getStory(@QueryParam("about") String about) {
        String[] pair = rndStrSup.generateRandomStringsPair();
        String adjective = pair[0];
        String noun = pair[1];

        String prompt = String.format("Write a 3-sentence story about the %s %s. Include: %s", adjective, noun, about != null ? about : "");

        try {
            String token = authService.getAccessToken();
            String finalPrompt = prompt;

            // Layer 2: Model Armor - Sanitize Prompt
            if (modelArmorEnabled) {
                JsonObject sanitizeRequest = Json.createObjectBuilder()
                        .add("text", prompt)
                        .build();
                JsonObject sanitizeResponse = modelArmorClient.sanitizeUserPrompt(
                        templateId + ":sanitizeUserPrompt", token, sanitizeRequest);
                
                // For simplicity, just use the sanitized text (if supported) or block if finding is malicious
                // The exact response format of Model Armor may vary, but let's assume it returns a "sanitizedText" field 
                // or "findings" array that we can check. For the DevFest demo, we might just return an error if it's blocked.
                if (sanitizeResponse.containsKey("sanitizedText")) {
                     finalPrompt = sanitizeResponse.getString("sanitizedText");
                }
                
                // For the sake of the demo, if it blocks, we might want to return that finding directly
                if (sanitizeResponse.containsKey("block") && sanitizeResponse.getBoolean("block", false)) {
                     return Json.createObjectBuilder()
                             .add("error", "Blocked by Model Armor")
                             .add("findings", sanitizeResponse)
                             .build();
                }
            }

            // Call Vertex AI
            JsonObject part = Json.createObjectBuilder().add("text", finalPrompt).build();
            JsonArrayBuilder partsArray = Json.createArrayBuilder().add(part);
            JsonObject content = Json.createObjectBuilder().add("role", "user").add("parts", partsArray).build();
            JsonArrayBuilder contentsArray = Json.createArrayBuilder().add(content);
            JsonObject aiRequest = Json.createObjectBuilder().add("contents", contentsArray).build();

            JsonObject aiResponse = vertexAIClient.generateContent(
                    projectId, location, model + ":generateContent", token, aiRequest);

            // Extract the generated text
            String generatedText = "";
            try {
                generatedText = aiResponse.getJsonArray("candidates")
                        .getJsonObject(0)
                        .getJsonObject("content")
                        .getJsonArray("parts")
                        .getJsonObject(0)
                        .getString("text");
            } catch (Exception e) {
                generatedText = "Error parsing AI response: " + aiResponse.toString();
            }

            String finalResponseText = generatedText;

            // Layer 2: Model Armor - Sanitize Response
            if (modelArmorEnabled) {
                JsonObject sanitizeRespRequest = Json.createObjectBuilder()
                        .add("text", generatedText)
                        .build();
                JsonObject sanitizeRespResponse = modelArmorClient.sanitizeModelResponse(
                        templateId + ":sanitizeModelResponse", token, sanitizeRespRequest);
                
                if (sanitizeRespResponse.containsKey("sanitizedText")) {
                     finalResponseText = sanitizeRespResponse.getString("sanitizedText");
                }
                
                if (sanitizeRespResponse.containsKey("block") && sanitizeRespResponse.getBoolean("block", false)) {
                     return Json.createObjectBuilder()
                             .add("error", "Blocked by Model Armor on response")
                             .add("findings", sanitizeRespResponse)
                             .build();
                }
            }

            return Json.createObjectBuilder()
                    .add("pair", Json.createArrayBuilder().add(adjective).add(noun))
                    .add("prompt", finalPrompt)
                    .add("story", finalResponseText)
                    .build();

        } catch (IOException e) {
            return Json.createObjectBuilder().add("error", "Authentication error: " + e.getMessage()).build();
        } catch (jakarta.ws.rs.WebApplicationException e) {
            String body = "No body";
            try {
                body = e.getResponse().readEntity(String.class);
            } catch (Exception ex) {}
            return Json.createObjectBuilder().add("error", "API error: " + e.getMessage() + ", body: " + body).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.createObjectBuilder().add("error", "Server error: " + e.getMessage()).build();
        }
    }
}
