package businessEntities;
import businessLogic.EmployeeManagerImpl;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

public class EmployeeTest {
    private EmployeeManager employeeManager;

    @BeforeEach
    void setUp(){
        employeeManager = new EmployeeManagerImpl();System.gc();
    }

    @Test
    void getterTestManager() {
        var salesManager = employeeManager.createSalesManager("Peter", "peter@gmx.de", "peterIstDerBeste");

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(salesManager.getName()).isEqualTo("Peter");
            s.assertThat(salesManager.getEmail()).isEqualTo("peter@gmx.de");
            s.assertThat(salesManager.getPassword()).isEqualTo("peterIstDerBeste");
        });
    }

    @Test
    void getterTestOfficer() {
        var salesOfficer = employeeManager.createSalesOfficer("Peter", "peter@gmx.de", "peterIstDerBeste");

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(salesOfficer.getName()).isEqualTo("Peter");
            s.assertThat(salesOfficer.getEmail()).isEqualTo("peter@gmx.de");
            s.assertThat(salesOfficer.getPassword()).isEqualTo("peterIstDerBeste");
        });
    }

    @Test
    void getterTestEmployee() {
        var salesEmployee = employeeManager.createSalesEmployee("Peter", "peter@gmx.de", "peterIstDerBeste");

        SoftAssertions.assertSoftly( s -> {
            s.assertThat(salesEmployee.getName()).isEqualTo("Peter");
            s.assertThat(salesEmployee.getEmail()).isEqualTo("peter@gmx.de");
            s.assertThat(salesEmployee.getPassword()).isEqualTo("peterIstDerBeste");
        });
    }

}
