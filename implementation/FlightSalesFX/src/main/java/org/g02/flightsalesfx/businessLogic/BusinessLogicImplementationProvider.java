package org.g02.flightsalesfx.businessLogic;

import org.g02.flightsalesfx.persistence.PersistenceAPI;

public interface BusinessLogicImplementationProvider extends BusinessLogicAPI {

    static BusinessLogicAPI getImplementation(PersistenceAPI persistenceAPI) {
        return new BusinessLogicAPIImpl(persistenceAPI);
    };

}
