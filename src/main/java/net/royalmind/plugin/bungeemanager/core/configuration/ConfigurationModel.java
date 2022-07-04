package net.royalmind.plugin.bungeemanager.core.configuration;

import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

public class ConfigurationModel {
    public static ConfigurationModel INSTANCE;

    public ConfigurationModel() { INSTANCE = this; }

    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "version", type = int.class)
    public int VERSION = 1;

    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "debug", type = boolean.class)
    public boolean DEBUG = true;

    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "database.host", type = String.class)
    public String DATABASE_HOST = "localhost";


    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "database.port", type = Long.class)
    public int DATABASE_PORT = 3306;


    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "database.name", type = String.class)
    public String DATABASE_DATABASE = "database";


    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "database.user", type = String.class)
    public String DATABASE_USER = "user";


    @Nullable
    @Getter
    @Setter
    @SettingsField(path = "database.password", type = String.class)
    public String DATABASE_PASSWORD = "password";

    @Override
    public String toString() { return new com.google.gson.Gson().toJson(this); }

    public static ConfigurationModel getInstance() { return INSTANCE; }
}
