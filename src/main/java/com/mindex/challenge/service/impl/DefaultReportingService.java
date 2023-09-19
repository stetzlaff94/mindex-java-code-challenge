package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DefaultReportingService implements ReportingService {

    // I am using constructor injection as that is the preferred method per Spring documentation.
    private final EmployeeRepository employeeRepository;

    public DefaultReportingService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public ReportingStructure getReportingStructure(String id) {
        final Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        } else {
            return new ReportingStructure(employee, solveNumberOfReports(employee, employeeRepository::findByEmployeeId));
        }

    }

    private int solveNumberOfReports(Employee employee, Function<String, Employee> populateEmployee) {
        List<Employee> directReports = employee.getDirectReports();
        directReports = directReports != null ? directReports.stream().map(
                e -> populateEmployee.apply(e.getEmployeeId())
        ).collect(Collectors.toList()) : Collections.emptyList();
        employee.setDirectReports(directReports);
        return directReports.stream().map(e -> 1 + solveNumberOfReports(e, populateEmployee)).reduce(0, Integer::sum);
    }
}
