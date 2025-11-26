package com.bookhub.repository;

import com.bookhub.entity.Prestamo;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    @Query("SELECT p FROM Prestamo p WHERE p.usuarioId = :usuarioId AND p.estado = true")
    List<Prestamo> findActivosByUsuario(Integer usuarioId);

    @Query("SELECT p FROM Prestamo p WHERE p.usuarioId = :usuarioId AND p.estado = false")
    List<Prestamo> findHistorialByUsuario(Integer usuarioId);

    @Query("SELECT p FROM Prestamo p WHERE p.estado = true")
    List<Prestamo> findPrestamosActivos();

    @Transactional
    @Modifying
    @Query("UPDATE Prestamo p SET p.estado = false WHERE p.libroIsbn = :isbn AND p.usuarioId = :usuarioId")
    int marcarComoDevuelto(String isbn, Integer usuarioId);

    @Query("SELECT COUNT(p) > 0 FROM Prestamo p WHERE p.libroIsbn = :isbn")
    boolean existePrestamoPorLibro(String isbn);

    long countByEstadoTrue();

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.estado = true AND p.fechaDevolucion < CURRENT_DATE")
    long countPrestamosVencidos();

    @Query("""
        SELECT p.libroIsbn AS libroIsbn, COUNT(p) AS total
        FROM Prestamo p
        GROUP BY p.libroIsbn
        ORDER BY total DESC
        """)
    List<PrestamoTopLibro> topLibrosPrestados(Pageable pageable);

    interface PrestamoTopLibro {
        String getLibroIsbn();
        long getTotal();
    }
}
