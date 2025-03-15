package org.doug.controller;

import io.quarkus.qute.Template;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.doug.dto.RGFDto;
import org.doug.service.RGFService;

import java.util.List;

@Path("/rgf")
@Produces(MediaType.TEXT_HTML)
@RequestScoped
public class RGFController {

    @Inject
    RGFService rgfService;

    @Inject
    Template rgf;

    @GET
    public Response getTabela(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("in_periodicidade") String inPeriodicidade,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("id_ente") int idEnte
    ) {
        List<RGFDto> items = rgfService.getRGF(
                anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo,
                nomeAnexo, codigoEsfera, idEnte
        );

        String html = rgf.data("items", items).render();

        return Response.ok(html).build();
    }
}
