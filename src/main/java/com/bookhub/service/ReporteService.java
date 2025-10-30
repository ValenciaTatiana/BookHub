package com.bookhub.service;

import com.bookhub.entity.ReporteSistema;
import com.bookhub.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public ReporteSistema crearReporte(ReporteSistema reporte) {
        return reporteRepository.save(reporte);
    }

    public List<ReporteSistema> listarReportes() {
        return reporteRepository.findAll();
    }

    public Optional<ReporteSistema> obtenerPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }
}
