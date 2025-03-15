package org.doug.client;

import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://apidatalake.tesouro.gov.br/ords/siconfi/tt")
public interface RREOClient {

    @GET
    @Path("/rreo")
    JsonObject getRREO(
            @QueryParam("an_exercicio") int anoExercicio,
            @QueryParam("nr_periodo") int numeroPeriodo,
            @QueryParam("co_tipo_demonstrativo") String codigoTipoDemonstrativo,
            @QueryParam("no_anexo") String nomeAnexo,
            @QueryParam("co_esfera") String codigoEsfera,
            @QueryParam("id_ente") int idEnte
    );
}
