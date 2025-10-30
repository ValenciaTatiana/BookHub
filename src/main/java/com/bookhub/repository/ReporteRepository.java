package com.bookhub.repository;

import com.bookhub.entity.ReporteSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteSistema, Long> {
}
