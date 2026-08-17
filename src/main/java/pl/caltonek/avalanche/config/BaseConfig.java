package pl.caltonek.avalanche.config;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public abstract class BaseConfig {
    protected final Path filePath;
    protected final Properties properties = new Properties();

    public BaseConfig(String fileName) {
        this.filePath = pl.caltonek.avalanche.path.AvalanchePaths.CONFIG_DIR.resolve(fileName);
    }

    public void load() {
        if (!java.nio.file.Files.exists(filePath)) {
            save();
            return;
        }
        try (InputStream input = java.nio.file.Files.newInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config: " + filePath, e);
        }
    }

    public void save() {
        try (OutputStream output = java.nio.file.Files.newOutputStream(filePath)) {
            properties.store(output, "Avalanche Configuration");
        } catch (IOException e) {
            throw new RuntimeException("Could not save config: " + filePath, e);
        }
    }

    protected String get(String key, String def) { return properties.getProperty(key, def); }
    protected boolean getBoolean(String key, boolean def) { return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(def))); }
    protected void set(String key, Object value) { properties.setProperty(key, String.valueOf(value)); }
}