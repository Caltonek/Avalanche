package pl.caltonek.avalanche.config.impl;

import pl.caltonek.avalanche.config.BaseConfig;

public class LoaderConfig extends BaseConfig {
    public LoaderConfig() { super("loader.properties"); }

    public boolean isAutoReload() { return getBoolean("scripts.autoReload", true); }
    public void setAutoReload(boolean value) { set("scripts.autoReload", value); }
}