package pl.caltonek.avalanche.execution.loader;

import org.jetbrains.annotations.NotNull;
import pl.caltonek.avalanche.exceptions.ScriptNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ScriptLoader {

    private final Path scriptsDirectory;

    public ScriptLoader(@NotNull final Path scriptsDirectory) {
        this.scriptsDirectory = scriptsDirectory;
        createScriptsDirectory();
    }

    private void createScriptsDirectory() {
        try {
            Files.createDirectories(this.scriptsDirectory);
        } catch (final IOException exception) {
            throw new IllegalStateException("Unable to create Avalanche scripts directory.", exception);
        }
    }

    @NotNull
    public List<String> findAvailableScripts() {
        if (!Files.exists(this.scriptsDirectory)) {
            return Collections.emptyList();
        }

        try (final Stream<Path> stream = Files.walk(this.scriptsDirectory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".lua"))
                    .map(path -> this.scriptsDirectory.relativize(path).toString().replace('\\', '/'))
                    .collect(Collectors.toList());
        } catch (final IOException exception) {
            return Collections.emptyList();
        }
    }

    @NotNull
    public Path resolveScriptPath(@NotNull final String scriptName) {
        final String normalizedName = normalizeScriptName(scriptName);
        final Path scriptPath = this.scriptsDirectory.resolve(normalizedName);

        if (!Files.exists(scriptPath)) {
            throw new ScriptNotFoundException(normalizedName);
        }

        return scriptPath;
    }

    @NotNull
    public String normalizeScriptName(@NotNull final String scriptName) {
        final String formatted = scriptName.replace('\\', '/');
        return formatted.endsWith(".lua") ? formatted : formatted + ".lua";
    }

    @NotNull
    public Path getScriptsDirectory() {
        return this.scriptsDirectory;
    }
}