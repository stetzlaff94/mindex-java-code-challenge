package com.mindex.challenge.data;

/**
 * The Class ReportingStructure.
 */
public class ReportingStructure {
    private Employee employee;

    private int numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    /**
     * Gets the employee.
     *
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets the employee.
     *
     * @param employee the employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;

    }

    /**
     * Gets the number of reports.
     *
     * @return the number of reports
     */
    public int getNumberOfReports() {
        return numberOfReports;
    }

}
