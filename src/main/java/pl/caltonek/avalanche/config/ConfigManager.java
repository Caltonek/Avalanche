package pl.caltonek.avalanche.config;

import pl.caltonek.avalanche.config.impl.LoaderConfig;
import pl.caltonek.avalanche.config.impl.NetworkConfig;

public final class ConfigManager {
    private final LoaderConfig loader = new LoaderConfig();
    private final NetworkConfig network = new NetworkConfig();

    public void loadAll() {
        loader.load();
        network.load();
    }

    public LoaderConfig getLoader() { return loader; }
    public NetworkConfig getNetwork() { return network; }
}