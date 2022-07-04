package net.royalmind.plugin.bungeemanager.core.configuration;

public class DatabaseSettings {
    @ConfigValue(value = "database.host", type = String.class)
    public static String DATABASE_HOST;
    @ConfigValue(value = "database.port", type = Integer.class)
    public static Integer DATABASE_PORT;
    @ConfigValue(value = "database.name", type = String.class)
    public static String DATABASE_DATABASE;
    @ConfigValue(value = "database.user", type = String.class)
    public static String DATABASE_USER;
    @ConfigValue(value = "database.password", type = String.class)
    public static String DATABASE_PASSWORD;

    static {
        DATABASE_HOST = "localhost";
        DATABASE_PORT = 3306;
        DATABASE_DATABASE = "database";
        DATABASE_USER = "user";
        DATABASE_PASSWORD = "password";
    }
}
