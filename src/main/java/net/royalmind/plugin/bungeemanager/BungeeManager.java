package net.royalmind.plugin.bungeemanager;

import com.google.gson.Gson;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.royalmind.plugin.bungeemanager.core.Logger;
import net.royalmind.plugin.bungeemanager.core.configuration.ConfigurationManager;
import net.royalmind.plugin.bungeemanager.core.configuration.DatabaseSettings;

public final class BungeeManager extends Plugin {
    private static BungeeManager PLUGIN_INSTANCE;
    private static boolean DEBUG;
    private ConfigurationManager configurationManager;

    private final Gson GSON;

    public BungeeManager() {
        super();
        PLUGIN_INSTANCE = this;
        GSON = new Gson();
        this.Initilize();
    }

    private void Initilize() {
        configurationManager = ConfigurationManager.getInstance();
    }

    private void loadConfiguration() {
        Logger.info("&aCargando cadena de conexión.");
        configurationManager.loadYamlDefaultConfiguration();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfiguration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Gson getGson() { return this.GSON; }

    public static BungeeManager getInstance() { return PLUGIN_INSTANCE; }

    public static boolean debug() { return DEBUG; }
}
