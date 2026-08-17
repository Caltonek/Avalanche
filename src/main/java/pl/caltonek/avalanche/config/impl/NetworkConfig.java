package pl.caltonek.avalanche.config.impl;

import pl.caltonek.avalanche.config.BaseConfig;

public class NetworkConfig extends BaseConfig {
    public NetworkConfig() { super("network.properties"); }

    public String getNetworkStatus() { return get("allow-network", "true"); }
}