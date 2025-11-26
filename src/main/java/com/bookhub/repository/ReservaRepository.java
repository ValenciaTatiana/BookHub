package com.bookhub.repository;

import com.bookhub.entity.Reserva;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsByLibroIsbnAndUsuarioIdAndAtendidaFalse(String libroIsbn, Integer usuarioId);

    List<Reserva> findByLibroIsbnAndAtendidaFalseOrderByFechaReservaAsc(String libroIsbn);

    Optional<Reserva> findTopByLibroIsbnAndAtendidaFalseOrderByFechaReservaAsc(String libroIsbn);
}
