package com.bookhub.service;

import com.bookhub.entity.Auditoria;
import com.bookhub.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    public void registrar(String entidad, String accion, String referenciaId, String detalles) {
        Auditoria registro = new Auditoria(entidad, accion, referenciaId, detalles);
        auditoriaRepository.save(registro);
    }
}
