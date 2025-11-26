package com.bookhub.service;

import com.bookhub.entity.ReporteSistema;
import com.bookhub.entity.Libro;
import com.bookhub.repository.ReporteRepository;
import com.bookhub.repository.PrestamoRepository;
import com.bookhub.repository.PrestamoRepository.PrestamoTopLibro;
import com.bookhub.repository.LibroRepository;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;

    public ReporteService(
        ReporteRepository reporteRepository,
        PrestamoRepository prestamoRepository,
        LibroRepository libroRepository
    ) {
        this.reporteRepository = reporteRepository;
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
    }

    public ReporteSistema crearReporte(ReporteSistema reporte) {
        validar(reporte);
        if (reporte.getFechaGeneracion() == null) {
            reporte.setFechaGeneracion(LocalDateTime.now());
        }
        return reporteRepository.save(reporte);
    }

    public List<ReporteSistema> listarReportes() {
        return reporteRepository.findAll();
    }

    public List<ReporteSistema> listarReportes(String titulo, LocalDateTime desde, LocalDateTime hasta, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReporteSistema> result;
        boolean tieneTitulo = titulo != null && !titulo.isBlank();
        boolean tieneRango = desde != null && hasta != null;

        if (tieneTitulo && tieneRango) {
            result = reporteRepository.findByTituloContainingIgnoreCaseAndFechaGeneracionBetween(
                titulo, desde, hasta, pageable);
        } else if (tieneTitulo) {
            result = reporteRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        } else if (tieneRango) {
            result = reporteRepository.findByFechaGeneracionBetween(desde, hasta, pageable);
        } else {
            result = reporteRepository.findAll(pageable);
        }
        return result.getContent();
    }

    public String exportarCsv(String titulo, LocalDateTime desde, LocalDateTime hasta) {
        List<ReporteSistema> reportes = listarReportes(
            titulo,
            desde,
            hasta,
            0,
            Integer.MAX_VALUE // exporta todo lo filtrado
        );
        String header = "id,titulo,descripcion,fechaGeneracion";
        String body = reportes.stream()
            .map(r -> String.format("%d,\"%s\",\"%s\",%s",
                r.getId(),
                r.getTitulo().replace("\"", "\"\""),
                r.getDescripcion().replace("\"", "\"\""),
                r.getFechaGeneracion()
            ))
            .collect(Collectors.joining("\n"));
        return header + "\n" + body;
    }

    public ReporteSistema generarResumenPrestamos(int topLibros) {
        long activos = prestamoRepository.countByEstadoTrue();
        long vencidos = prestamoRepository.countPrestamosVencidos();
        List<PrestamoTopLibro> top = prestamoRepository.topLibrosPrestados(PageRequest.of(0, topLibros));

        List<String> isbns = top.stream().map(PrestamoTopLibro::getLibroIsbn).toList();
        Map<String, Libro> librosPorIsbn = libroRepository.findByIsbnIn(isbns).stream()
            .collect(Collectors.toMap(Libro::getIsbn, l -> l));

        StringBuilder descripcion = new StringBuilder();
        descripcion.append("Prestamos activos: ").append(activos).append("\n");
        descripcion.append("Prestamos vencidos: ").append(vencidos).append("\n");
        descripcion.append("Top libros prestados:\n");
        for (PrestamoTopLibro entry : top) {
            Libro libro = librosPorIsbn.get(entry.getLibroIsbn());
            String nombreLibro = libro != null ? libro.getTitulo() : "ISBN " + entry.getLibroIsbn();
            descripcion.append("- ")
                .append(nombreLibro)
                .append(" (")
                .append(entry.getLibroIsbn())
                .append("): ")
                .append(entry.getTotal())
                .append(" prestamos\n");
        }

        ReporteSistema reporte = new ReporteSistema(
            "Resumen de prestamos",
            descripcion.toString().trim()
        );
        return crearReporte(reporte);
    }

    public Optional<ReporteSistema> obtenerPorId(Long id) {
        return reporteRepository.findById(id);
    }

    public void eliminarReporte(Long id) {
        reporteRepository.deleteById(id);
    }

    private void validar(ReporteSistema reporte) {
        if (reporte == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El reporte no puede ser nulo");
        }
        if (reporte.getTitulo() == null || reporte.getTitulo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El reporte debe tener un titulo");
        }
        if (reporte.getDescripcion() == null || reporte.getDescripcion().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El reporte debe tener una descripcion");
        }
    }
}
