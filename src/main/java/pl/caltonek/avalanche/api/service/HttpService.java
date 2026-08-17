package pl.caltonek.avalanche.api.service;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public interface HttpService {
    @NotNull String getAsync(@NotNull final String url);
    @NotNull String postAsync(@NotNull final String url, @NotNull final String jsonBody);
    @NotNull String jsonEncode(@NotNull final Object obj);
    @NotNull JsonElement jsonDecode(@NotNull final String json);
}