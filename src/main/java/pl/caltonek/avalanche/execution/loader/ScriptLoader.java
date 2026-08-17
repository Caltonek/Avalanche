package pl.caltonek.avalanche.execution.loader;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.exceptions.ScriptNotFoundException;
import pl.caltonek.avalanche.path.AvalanchePaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ScriptLoader {

    public ScriptLoader() {}

    @NotNull
    public List<String> findAvailableScripts() {
        if (!Files.exists(AvalanchePaths.SCRIPTS_DIR)) {
            return Collections.emptyList();
        }

        try (final Stream<Path> stream = Files.walk(AvalanchePaths.SCRIPTS_DIR)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".lua"))
                    .map(path -> AvalanchePaths.SCRIPTS_DIR.relativize(path).toString().replace('\\', '/'))
                    .collect(Collectors.toList());
        } catch (final IOException exception) {
            return Collections.emptyList();
        }
    }

    @NotNull
    public Path resolveScriptPath(@NotNull final String scriptName) {
        final String normalizedName = normalizeScriptName(scriptName);
        final Path scriptPath = AvalanchePaths.SCRIPTS_DIR.resolve(normalizedName);

        if (!Files.exists(scriptPath)) {
            throw new ScriptNotFoundException(normalizedName);
        }

        return scriptPath;
    }

    public boolean isUrl(@NotNull final String input) {
        return input.startsWith("http://") || input.startsWith("https://");
    }

    @NotNull
    public String normalizeScriptName(@NotNull final String scriptName) {
        final String formatted = scriptName.replace('\\', '/');
        return formatted.endsWith(".lua") ? formatted : formatted + ".lua";
    }
}