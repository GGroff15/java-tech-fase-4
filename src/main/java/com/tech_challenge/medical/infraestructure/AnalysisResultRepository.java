package com.tech_challenge.medical.infraestructure;

import com.tech_challenge.medical.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
}