package com.bookhub.controller;

import com.bookhub.entity.ReporteSistema;
import com.bookhub.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ReporteSistema crearReporte(@RequestBody ReporteSistema reporte) {
        return reporteService.crearReporte(reporte);
    }

    @GetMapping
    public List<ReporteSistema> listarReportes() {
        return reporteService.listarReportes();
    }

    @GetMapping("/{id}")
    public Optional<ReporteSistema> obtenerReportePorId(@PathVariable Long id) {
        return reporteService.obtenerPorId(id);
    }

    @DeleteMapping("/{id}")
    public String eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return "Reporte eliminado exitosamente";
    }
}
