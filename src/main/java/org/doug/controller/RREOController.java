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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.doug.dto.RREODto;
import org.doug.service.RREOService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
    public Response getRREOController(
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

    @GET
    @Path("/csv")
    @Produces("text/csv")
    public Response getRREOAsCsv(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("id_ente") int idEnte) {

        List<RREODto> items = rreoService.getRREO(
                anoExercicio, numeroPeriodo, codigoTipoDemonstrativo,
                nomeAnexo, codigoEsfera, idEnte
        );

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(
                     new OutputStreamWriter(out, StandardCharsets.UTF_8),
                     CSVFormat.DEFAULT.withHeader(
                             "Exercicio", "Demonstrativo", "Periodo", "Periodicidade",
                             "Instituicao", "CodIbge", "UF", "Populacao", "Anexo",
                             "Rotulo", "Coluna", "CodConta", "Conta", "Valor"
                     ))) {

            for (RREODto item : items) {
                printer.printRecord(
                        item.exercicio(),
                        item.demonstrativo(),
                        item.periodo(),
                        item.periodicidade(),
                        item.instituicao(),
                        item.codIbge(),
                        item.uf(),
                        item.populacao(),
                        item.anexo(),
                        item.rotulo(),
                        item.coluna(),
                        item.codConta(),
                        item.conta(),
                        item.valor()
                );
            }

            printer.flush();

            return Response.ok(out.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"rreo-data.csv\"")
                    .build();

        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
