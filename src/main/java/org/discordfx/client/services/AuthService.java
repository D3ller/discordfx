package org.discordfx.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.discordfx.client.Config;
import org.discordfx.client.http.ApiClient;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AuthService {

    public record AuthResult(
            boolean success,
            String token,
            boolean requiresMfa
    ) {}

    private record RequestData(
            ObjectMapper mapper,
            Map<String, Object> parameters
    ) {}

    private final ApiClient apiClient;

    public AuthService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public CompletableFuture<AuthResult> login(String email, String password) {
        var data = this.prepareRequest(email, password);

        ObjectMapper mapper = data.mapper();
        Map<String, Object> parameters = data.parameters();

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder().uri(URI.create(Config.API_BASE_URL + "/auth/login")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(parameters))).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return this.apiClient.sendRequest(request).thenApply(response -> {
            JsonNode body = null;
            try {
                body = mapper.readTree(response.body());
            } catch (JsonProcessingException e) {
                throw new CompletionException(e);
            }
            boolean requiresMfa = body.path("mfa").asBoolean(false);
            return new AuthResult(response.statusCode() == 200, null, requiresMfa);
        });
    }

    private RequestData prepareRequest(String email, String password) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("gift_code_sku_id", null);
        parameters.put("login", email);
        parameters.put("login_source", null);
        parameters.put("password", password);
        parameters.put("undelete", false);

        return new RequestData(mapper, parameters);
    }
}
