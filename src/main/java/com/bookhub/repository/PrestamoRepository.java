package com.bookhub.repository;

import com.bookhub.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
