package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.mindex.challenge.service.impl.EmployeeServiceImplTest.assertEmployeeEquivalence;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String getCompensationUrl;
    private String createCompensationUrl;
    private String updateCompensationUrl;

    private String createEmployeeEndpoint;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        createEmployeeEndpoint = "http://localhost:" + port + "/employee";
        getCompensationUrl = "http://localhost:" + port + "/compensation/{id}";
        createCompensationUrl = "http://localhost:" + port + "/compensation";
        updateCompensationUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCompensationEndpoints() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create employee
        ResponseEntity<Employee> response = restTemplate.postForEntity(
            createEmployeeEndpoint,
            testEmployee,
            Employee.class
        );

        assertEquals(response.getStatusCode(), HttpStatus.OK);

        testEmployee = response.getBody();

        Compensation testCompensation = new Compensation(testEmployee, 50000, LocalDate.of(2022, 9, 18));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(createCompensationUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation.getEmployee());

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(getCompensationUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
        assertEquals(createdCompensation.getEmployee().getEmployeeId(), readCompensation.getEmployee().getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);

        // Update checks
        readCompensation.setSalary(55000);
        readCompensation.setEffectiveDate(LocalDate.of(2023, 9, 18));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Compensation updatedCompensation =
                restTemplate.exchange(updateCompensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(readCompensation, headers),
                        Compensation.class,
                        readCompensation.getEmployee().getEmployeeId()).getBody();

        assertCompensationEquivalence(readCompensation, updatedCompensation);

        // Update future, but dont return as active
        Compensation futureCompensation = new Compensation(testEmployee, 60000, LocalDate.of(2024, 9, 18));
        Compensation updatedFutureCompensation =
                restTemplate.exchange(updateCompensationUrl,
                        HttpMethod.PUT,
                        new HttpEntity<>(futureCompensation, headers),
                        Compensation.class,
                        readCompensation.getEmployee().getEmployeeId()).getBody();
        // Assert that the future compensation  is updated
        assertCompensationEquivalence(futureCompensation, updatedFutureCompensation);

        // Read active
        Compensation readActiveCompensation = restTemplate.getForEntity(getCompensationUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();

        // Assert still equal to updatedCompensation
        assertCompensationEquivalence(updatedCompensation, readActiveCompensation);

    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
