package com.tech_challenge.medical.domain.event;

import org.springframework.context.ApplicationEvent;

public class AnalysisCaseCreatedEvent extends ApplicationEvent {

    private final Long analysisCaseId;

    public AnalysisCaseCreatedEvent(Object source, Long analysisCaseId) {
        super(source);
        this.analysisCaseId = analysisCaseId;
    }

    public Long getAnalysisCaseId() {
        return analysisCaseId;
    }
}
