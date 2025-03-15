package org.doug.repository;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.doug.client.RREOClient;
import org.doug.dto.RREODto;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class RREORepository {

    @Inject
    @RestClient
    RREOClient rreoClient;

    public RREODto[] getRREO(int anoExercicio, int numeroPeriodo, String codigoTipoDemonstrativo, String nomeAnexo, String codigoEsfera, int idEnte) {
        Response response = rreoClient.getRREO(anoExercicio, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte);
        return response.readEntity(RREODto[].class);
    }
}