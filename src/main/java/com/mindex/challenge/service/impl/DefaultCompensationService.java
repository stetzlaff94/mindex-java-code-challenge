package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class DefaultCompensationService implements CompensationService {

    private final static Logger LOG = LoggerFactory.getLogger(DefaultCompensationService.class);
    private CompensationRepository compensationRepository;

    public DefaultCompensationService(CompensationRepository compensationRepository) {
        this.compensationRepository = compensationRepository;
    }

    @Override
    public Compensation getCompensation(String employeeId) {
        return compensationRepository.findByEmployeeEmployeeId(employeeId)
                .stream().max(this::compareEffectiveDatesIgnoringFuture)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Compensation not found for this Employee"
            ));
    }

    @Override
    public Compensation createCompensation(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);
        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation updateCompensation(String id, Compensation compensation) {
        LOG.debug("Updating compensation [{}]", compensation);
        if (id.equals(compensation.getEmployee().getEmployeeId())) {
            compensationRepository.save(compensation);
            return compensation;
        } else {
            // This is a bad request, so we throw a 400, the message is only providing minimal info for security reasons.
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Not a valid Compensation for this Employee"
            );
        }
    }
    private int compareEffectiveDatesIgnoringFuture(Compensation c1, Compensation c2) {
        LocalDate date1 = c1.getEffectiveDate();
        LocalDate date2 = c2.getEffectiveDate();
        LocalDate now = LocalDate.now();

        if (date1 == null || date1.isAfter(now)) {
            return -1;
        }

        if (date2 == null || date2.isAfter(now)) {
            return 1;
        }

        return date1.compareTo(date2);
    }


}
