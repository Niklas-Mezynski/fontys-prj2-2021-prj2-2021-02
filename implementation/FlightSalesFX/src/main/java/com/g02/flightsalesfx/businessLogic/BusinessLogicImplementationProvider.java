package com.g02.flightsalesfx.businessLogic;

import com.g02.flightsalesfx.persistence.PersistenceAPI;

public interface BusinessLogicImplementationProvider extends BusinessLogicAPI {

    static BusinessLogicAPI getImplementation(PersistenceAPI persistenceAPI) {
        return new BusinessLogicAPIImpl(persistenceAPI);
    };

}
