package org.doug.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.doug.dto.RGFDto;
import org.doug.service.RGFService;

import java.util.List;

@Path("/rgf")
public class RGFController {

    @Inject
    RGFService rgfService;

    @GET
    @Path("/dados")
    public List<RGFDto> getRGF(
            @QueryParam("anoExercicio") int anoExercicio,
            @QueryParam("inPeriodicidade") String inPeriodicidade,
            @QueryParam("numeroPeriodo") int numeroPeriodo,
            @QueryParam("codigoTipoDemonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("nomeAnexo") String nomeAnexo,
            @QueryParam("codigoEsfera") String codigoEsfera,
            @QueryParam("idEnte") int idEnte
    ) {
        return rgfService.getRGF(anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte);
    }
}
