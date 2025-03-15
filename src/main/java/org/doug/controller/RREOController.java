package org.doug.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.doug.dto.RREODto;
import org.doug.service.RREOService;

import java.util.List;

@Path("/rreo")
public class RREOController {

    @Inject
    RREOService rreoService;

    @GET
    @Path("/dados")
    public List<RREODto> getRREO(
            @QueryParam("anoExercicio") int anoExercicio,
            @QueryParam("numeroPeriodo") int numeroPeriodo,
            @QueryParam("codigoTipoDemonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("nomeAnexo") String nomeAnexo,
            @QueryParam("codigoEsfera") String codigoEsfera,
            @QueryParam("idEnte") int idEnte
    ) {
        return rreoService.getRREO(anoExercicio, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte);
    }
}