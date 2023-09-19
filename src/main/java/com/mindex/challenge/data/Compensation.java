package com.mindex.challenge.data;

import java.time.LocalDate;

//Compensation has the following fields: employee, salary, and effectiveDate. Create two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the Compensation from the persistence layer.
public class Compensation {
    private Employee employee;
    private int salary;
    private LocalDate effectiveDate;

    public Compensation() {
    }

    public Compensation(Employee employee, int salary, LocalDate effectiveDate) {
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
