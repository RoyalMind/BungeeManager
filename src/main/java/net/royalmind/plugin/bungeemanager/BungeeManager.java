package net.royalmind.plugin.bungeemanager;

import com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Plugin;
import net.royalmind.plugin.bungeemanager.core.Logger;
import net.royalmind.plugin.bungeemanager.core.configuration.ConfigurationManager;
import net.royalmind.plugin.bungeemanager.core.configuration.ConfigurationModel;

public final class BungeeManager extends Plugin {
    private static BungeeManager PLUGIN_INSTANCE;
    private final Gson GSON;
    private final ConfigurationManager configurationManager;
    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PRIVATE)
    private ConfigurationModel configuration;

    public BungeeManager() {
        super();
        PLUGIN_INSTANCE = this;
        GSON = new Gson();
        configurationManager = ConfigurationManager.getInstance();
    }

    private void loadConfiguration() {
        Logger.info("&aCargando cadena de conexión.");
        configuration = configurationManager.loadYamlDefaultConfiguration();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Logger.info("&aIniciando BungeeManager.");
        loadConfiguration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Gson getGson() { return this.GSON; }

    public static BungeeManager getInstance() { return PLUGIN_INSTANCE; }
}
