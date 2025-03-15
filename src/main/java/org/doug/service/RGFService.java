package org.doug.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.doug.dto.RGFDto;
import org.doug.repository.RGFRepository;

import java.util.List;

@ApplicationScoped
public class RGFService {

    @Inject
    RGFRepository rgfRepository;

    public List<RGFDto> getRGF(int anoExercicio, String inPeriodicidade, int numeroPeriodo, String codigoTipoDemonstrativo, String nomeAnexo, String codigoEsfera, int idEnte) {
        return List.of(rgfRepository.getRGF(anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte));
    }
}
