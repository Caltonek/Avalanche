package pl.caltonek.avalanche.api.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.api.service.HttpService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public final class HttpServiceImpl implements HttpService {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static final Gson GSON = new GsonBuilder().create();

    @NotNull
    public String GetAsync(@NotNull final String url) {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Avalanche-Client")
                    .GET()
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (final Exception e) {
            throw new RuntimeException("Failed HTTP GET request to: " + url, e);
        }
    }

    @NotNull
    @Override
    public String getAsync(@NotNull final String url) {
        return GetAsync(url);
    }

    @NotNull
    public String get(@NotNull final String url) {
        return GetAsync(url);
    }

    @NotNull
    public String PostAsync(@NotNull final String url, @NotNull final String jsonBody) {
        try {
            final HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "Avalanche-Client")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (final Exception e) {
            throw new RuntimeException("Failed HTTP POST request to: " + url, e);
        }
    }

    @NotNull
    @Override
    public String postAsync(@NotNull final String url, @NotNull final String jsonBody) {
        return PostAsync(url, jsonBody);
    }

    @NotNull
    public String JSONEncode(@NotNull final Object obj) {
        return GSON.toJson(obj);
    }

    @NotNull
    @Override
    public String jsonEncode(@NotNull final Object obj) {
        return JSONEncode(obj);
    }

    @NotNull
    public JsonElement JSONDecode(@NotNull final String json) {
        return JsonParser.parseString(json);
    }

    @NotNull
    @Override
    public JsonElement jsonDecode(@NotNull final String json) {
        return JSONDecode(json);
    }
}