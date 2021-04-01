package com.g02.flightsalesfx.businessEntities;

public interface Employee {
    /**
     *
     * @return Name of the employee
     */
    String getName();

    /**
     * @return Email-Address of the employee
     */
    String getEmail();

    /**
     * @return Password of the employee, could be null
     */
    String getPassword();

}
