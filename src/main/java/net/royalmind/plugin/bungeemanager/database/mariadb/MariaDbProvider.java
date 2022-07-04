package net.royalmind.plugin.bungeemanager.database.mariadb;

import net.royalmind.plugin.bungeemanager.database.IDatabase;

public class MariaDbProvider implements IDatabase {
    public MariaDbProvider() {
    }

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public boolean test() {
        return false;
    }
}
