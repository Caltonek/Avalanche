package pl.caltonek.avalanche.path;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AvalanchePaths {

    public static final Path BASE_DIR = Path.of("avalanche");
    public static final Path SCRIPTS_DIR = BASE_DIR.resolve("scripts");
    public static final Path CONFIG_DIR = BASE_DIR.resolve("config");

    private AvalanchePaths() {}

    public static void createDirectories() {
        try {
            Files.createDirectories(BASE_DIR);
            Files.createDirectories(SCRIPTS_DIR);
            Files.createDirectories(CONFIG_DIR);
        } catch (final IOException e) {
            throw new RuntimeException("Could not create Avalanche directories", e);
        }
    }
}