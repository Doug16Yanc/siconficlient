package org.doug.client;

import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://apidatalake.tesouro.gov.br/ords/siconfi/tt")
public interface RGFClient {

    @GET
    @Path("/rgf")
    JsonObject getRGF(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("in_periodicidade") String inPeriodicidade,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("id_ente") int idEnte
    );
}
