package com.bookhub.controller;

import com.bookhub.entity.ReporteSistema;
import com.bookhub.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ReporteSistema crearReporte(@RequestBody ReporteSistema reporte) {
        return reporteService.crearReporte(reporte);
    }

    @GetMapping(produces = "application/json")
    public List<ReporteSistema> listarReportes(
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return reporteService.listarReportes(titulo, desde, hasta, page, size);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    public String exportarReportesCsv(
        @RequestParam(required = false) String titulo,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return reporteService.exportarCsv(titulo, desde, hasta);
    }

    @PostMapping("/auto/resumen-prestamos")
    public ReporteSistema generarReportePrestamos(@RequestParam(defaultValue = "5") int topLibros) {
        return reporteService.generarResumenPrestamos(topLibros);
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
