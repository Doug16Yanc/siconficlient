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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.doug.dto.RGFDto;
import org.doug.service.RGFService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
    public Response getRGFController(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("in_periodicidade") String inPeriodicidade,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("co_poder") String codigoPoder,
            @QueryParam("id_ente") int idEnte) {

        List<RGFDto> items = rgfService.getRGF(
                anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo,
                nomeAnexo, codigoEsfera, codigoPoder, idEnte
        );

        return Response.ok(rgf.data("items", items).render()).build();
    }


    @GET
    @Path("/csv")
    @Produces("text/csv")
    public Response getRGFAsCsv(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("in_periodicidade") String inPeriodicidade,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("co_poder") String codigoPoder,
            @QueryParam("id_ente") int idEnte) {

        List<RGFDto> items = rgfService.getRGF(
                anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo,
                nomeAnexo, codigoEsfera, codigoPoder, idEnte
        );

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(
                     new OutputStreamWriter(out, StandardCharsets.UTF_8),
                     CSVFormat.DEFAULT
                             .withHeader(
                                     "Exercicio", "Periodo", "Periodicidade",
                                     "Instituicao", "CodIbge", "UF", "CodPoder", "Populacao", "Anexo",
                                     "Rotulo", "Coluna", "CodConta", "Conta", "Valor"
                             ))) {

            for (RGFDto item : items) {
                printer.printRecord(
                        item.exercicio(),
                        item.periodo(),
                        item.periodicidade(),
                        item.instituicao(),
                        item.codIbge(),
                        item.uf(),
                        item.coPoder(),
                        item.populacao(),
                        item.anexo(),
                        item.rotulo(),
                        item.codConta(),
                        item.conta(),
                        item.valor()
                );
            }

            printer.flush();

            return Response.ok(out.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"rgf-data.csv\"")
                    .build();

        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}