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
import org.doug.dto.RREODto;
import org.doug.service.RREOService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
            @QueryParam("id_ente") int idEnte,
            @QueryParam("todos_periodos") Boolean todosPeriodos,
            @QueryParam("todos_municipios") Boolean todosMunicipios) {

        nomeAnexo = URLDecoder.decode(nomeAnexo, StandardCharsets.UTF_8);
        List<RREODto> items = new ArrayList<>();

        if (Boolean.TRUE.equals(todosMunicipios)) {
            items.addAll(processarTodosMunicipios(todosPeriodos, anoExercicio, numeroPeriodo,
                    codigoTipoDemonstrativo, nomeAnexo, codigoEsfera));
        } else {
            items.addAll(processarUnicoMunicipio(todosPeriodos, anoExercicio, numeroPeriodo,
                    codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte));
        }

        if (items.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum dado encontrado para os parâmetros fornecidos.")
                    .build();
        }

        return gerarCsvResponse(items);
    }

    private List<RREODto> processarTodosMunicipios(Boolean todosPeriodos, int anoExercicio, int numeroPeriodo,
                                                   String codigoTipoDemonstrativo, String nomeAnexo, String codigoEsfera) {
        List<RREODto> items = new ArrayList<>();
        for (int ano = 2015; ano <= 2024; ano++) {
            for (int codigoMunicipio : MUNICIPIOS_CEARA) {
                items.addAll(processarPeriodos(todosPeriodos, ano, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, codigoMunicipio));
            }
        }
        return items;
    }

    private List<RREODto> processarUnicoMunicipio(Boolean todosPeriodos, int anoExercicio, int numeroPeriodo,
                                                  String codigoTipoDemonstrativo, String nomeAnexo, String codigoEsfera, int idEnte) {
        List<RREODto> items = new ArrayList<>();
        for (int ano = 2015; ano <= 2025; ano++) {
            items.addAll(processarPeriodos(todosPeriodos, ano, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte));
        }
        return items;
    }

    private List<RREODto> processarPeriodos(Boolean todosPeriodos, int ano, int numeroPeriodo, String codigoTipoDemonstrativo,
                                            String nomeAnexo, String codigoEsfera, int idEnte) {
        List<RREODto> items = new ArrayList<>();
        if (Boolean.TRUE.equals(todosPeriodos)) {
            for (int periodo = 1; periodo <= 6; periodo++) {
                items.addAll(rreoService.getRREO(ano, periodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte));
            }
        } else if (numeroPeriodo > 0) {
            items.addAll(rreoService.getRREO(ano, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte));
        }
        return items;
    }

    private Response gerarCsvResponse(List<RREODto> items) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out, StandardCharsets.UTF_8),
                     CSVFormat.DEFAULT.withHeader(
                             "Exercicio", "Demonstrativo", "Periodo", "Periodicidade",
                             "Instituicao", "CodIbge", "UF", "Populacao", "Anexo",
                             "Rotulo", "Coluna", "CodConta", "Conta", "Valor"
                     ))) {

            for (RREODto item : items) {
                printer.printRecord(
                        item.exercicio(), item.demonstrativo(), item.periodo(), item.periodicidade(),
                        item.instituicao(), item.codIbge(), item.uf(), item.populacao(),
                        item.anexo(), item.rotulo(), item.coluna(), item.codConta(), item.conta(), item.valor()
                );
            }
            printer.flush();

            return Response.ok(out.toByteArray())
                    .header("Content-Disposition", "attachment; filename=\"rreo-data.csv\"")
                    .build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar o arquivo CSV.")
                    .build();
        }
    }

    private static final List<Integer> MUNICIPIOS_CEARA = List.of(
            2300101, 2300150, 2300200, 2300309, 2300408, 2300507, 2300606, 2300705, 2300754, 2300804,
            2300903, 2301000, 2301109, 2301208, 2301257, 2301307, 2301406, 2301505, 2301604, 2301703,
            2301802, 2301851, 2301901, 2302008, 2302057, 2302107, 2302206, 2302305, 2302404, 2302503,
            2302602, 2302701, 2302800, 2302909, 2303006, 2303105, 2303204, 2303303, 2303402, 2303501,
            2303600, 2303659, 2303709, 2303808, 2303907, 2303931, 2303956, 2304004, 2304103, 2304202,
            2304236, 2304251, 2304269, 2304277, 2304285, 2304301, 2304350, 2304400, 2304459, 2304509,
            2304608, 2304657, 2304707, 2304806, 2304905, 2304954, 2305001, 2305100, 2305209, 2305233,
            2305266, 2305308, 2305332, 2305357, 2305407, 2305506, 2305605, 2305654, 2305704, 2305803
    );

  /*  @GET
    @Path("/csv")
    @Produces("text/csv")
    public Response getRREOAsCsv(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("id_ente") int idEnte,
            @QueryParam("todos_periodos") Boolean todosPeriodos) {

        nomeAnexo = URLDecoder.decode(nomeAnexo, StandardCharsets.UTF_8);

        List<RREODto> items = new ArrayList<>();

        if (Boolean.TRUE.equals(todosPeriodos)) {
            for (int i = 1; i <= 6; i++) {
                List<RREODto> periodoItems = rreoService.getRREO(
                        anoExercicio, i, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte
                );
                items.addAll(periodoItems);
            }
        } else if (numeroPeriodo > 0) {
            items = rreoService.getRREO(
                    anoExercicio, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte
            );
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Parâmetro 'nr_periodo' é obrigatório quando 'todos_periodos' é false.")
                    .build();
        }

        if (items.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum dado encontrado para os parâmetros fornecidos.")
                    .build();
        }

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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao gerar o arquivo CSV.")
                    .build();
        }
    } */
}
