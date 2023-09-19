package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.ReportingStructure;

public interface CompensationService {
    Compensation getCompensation(String employeeId);

    Compensation createCompensation(Compensation compensation);

    Compensation updateCompensation(String id, Compensation compensation);

}
