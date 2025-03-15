package org.doug.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.doug.client.RGFClient;
import org.doug.dto.RGFDto;
import org.doug.dto.RREODto;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class RGFRepository {

    @Inject
    @RestClient
    RGFClient rgfClient;

    public RGFDto[] getRGF(int anoExercicio, String inPeriodicidade, int numeroPeriodo, String codigoTipoDemonstrativo, String nomeAnexo, String codigoEsfera, int idEnte) {
        Response response = rgfClient.getRGF(anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte);
        return response.readEntity(RGFDto[].class);
    }

}