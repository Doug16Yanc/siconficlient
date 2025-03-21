package org.doug.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.doug.dto.RREODto;
import org.doug.repository.RREORepository;
import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RREOService {

    @Inject
    RREORepository rreoRepository;
    @Inject
    private Logger logger;

    public List<RREODto> getRREO(
            int anoExercicio,
            int numeroPeriodo,
            String codigoTipoDemonstrativo,
            String nomeAnexo,
            String codigoEsfera,
            int idEnte
    ) {
        return rreoRepository.getRREO(
                anoExercicio,
                numeroPeriodo,
                codigoTipoDemonstrativo,
                nomeAnexo,
                codigoEsfera,
                idEnte
        );
    }
}
