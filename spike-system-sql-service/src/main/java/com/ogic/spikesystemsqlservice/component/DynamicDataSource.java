package com.ogic.spikesystemsqlservice.component;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author ogic
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DatabaseType> contextHolder = new ThreadLocal<DatabaseType>();

    @Override
    protected Object determineCurrentLookupKey() {
        return contextHolder.get();
    }

    public enum DatabaseType {
        Master, Slave
    }

    public static void master(){
        contextHolder.set(DatabaseType.Master);
    }


    public static void slave(){
        contextHolder.set(DatabaseType.Slave);
    }

    public static void setDatabaseType(DatabaseType type) {
        contextHolder.set(type);
    }

    public static DatabaseType getType(){
        return contextHolder.get();
    }
}
