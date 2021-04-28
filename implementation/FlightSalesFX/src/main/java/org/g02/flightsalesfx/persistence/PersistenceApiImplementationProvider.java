package org.g02.flightsalesfx.persistence;

public interface PersistenceApiImplementationProvider extends PersistenceAPI {

    static PersistenceAPI getImplementation() {
        return new PersistenceAPIImpl();
    }

}
