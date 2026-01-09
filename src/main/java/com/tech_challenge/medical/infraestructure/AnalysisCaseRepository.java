package com.tech_challenge.medical.infraestructure;

import com.tech_challenge.medical.domain.AnalysisCase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisCaseRepository extends JpaRepository<AnalysisCase, Long> {
}
