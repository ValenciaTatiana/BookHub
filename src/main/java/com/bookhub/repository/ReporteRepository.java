package com.bookhub.repository;

import com.bookhub.entity.ReporteSistema;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteSistema, Long> {

    Page<ReporteSistema> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    Page<ReporteSistema> findByFechaGeneracionBetween(LocalDateTime desde, LocalDateTime hasta, Pageable pageable);

    Page<ReporteSistema> findByTituloContainingIgnoreCaseAndFechaGeneracionBetween(
        String titulo,
        LocalDateTime desde,
        LocalDateTime hasta,
        Pageable pageable
    );
}
