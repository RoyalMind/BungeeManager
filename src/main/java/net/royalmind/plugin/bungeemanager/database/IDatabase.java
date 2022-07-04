package net.royalmind.plugin.bungeemanager.database;

public interface IDatabase {
    boolean connect();
    void disconnect();
    boolean test();
}
