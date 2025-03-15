package org.doug.controller;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.doug.dto.RREODto;
import org.doug.service.RREOService;

import java.util.List;

@Path("/rreo")
@Produces(MediaType.TEXT_HTML)
@RequestScoped
public class RREOController {

    @Inject
    RREOService rreoService;

    @Inject
    Template rreo;

    @GET
    public Response getTabela(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("id_ente") int idEnte
    ) {
        List<RREODto> items = rreoService.getRREO(
                anoExercicio, numeroPeriodo, codigoTipoDemonstrativo,
                nomeAnexo, codigoEsfera, idEnte
        );

        String html = rreo.data("items", items).render();

        return Response.ok(html).build();
    }
}
