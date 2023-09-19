package com.mindex.challenge.controller;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportingController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingController.class);

    private final ReportingService reportingService;

    public ReportingController(
            ReportingService reportingService
    ) {
        this.reportingService = reportingService;
    }

    @GetMapping("/reporting/{id}")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received GET reporting structure request for id [{}]", id);
        return reportingService.getReportingStructure(id);
    }

}
