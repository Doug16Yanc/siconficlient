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

    public List<RGFDto> getRGF(
            int anoExercicio,
            String inPeriodicidade,
            int numeroPeriodo,
            String codigoTipoDemonstrativo,
            String nomeAnexo,
            String codigoEsfera,
            String codigoPoder,
            int idEnte
    ) {
        return rgfRepository.getRGF(
                anoExercicio,
                inPeriodicidade,
                numeroPeriodo,
                codigoTipoDemonstrativo,
                nomeAnexo,
                codigoEsfera,
                codigoPoder,
                idEnte
        );
    }
}
