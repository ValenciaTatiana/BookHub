package com.bookhub.repository;

import com.bookhub.entity.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, String> {

    List<Libro> findByDisponibleTrueOrderByTituloAsc();

    @Query("""
        SELECT l FROM Libro l
        WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
           OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :termino, '%'))
        ORDER BY l.titulo
        """)
    List<Libro> buscarPorTituloOAutor(@Param("termino") String termino);

    List<Libro> findAllByOrderByTituloAsc();
}
