package org.doug.repository;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.doug.client.RREOClient;
import org.doug.dto.RREODto;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RREORepository {

    @Inject
    @RestClient
    RREOClient rreoClient;

    @Inject
    ObjectMapper objectMapper;

    public List<RREODto> getRREO(
            int anoExercicio,
            int numeroPeriodo,
            String codigoTipoDemonstrativo,
            String nomeAnexo,
            String codigoEsfera,
            int idEnte
    ) {
        JsonObject response = rreoClient.getRREO(
                anoExercicio,
                numeroPeriodo,
                codigoTipoDemonstrativo,
                nomeAnexo,
                codigoEsfera,
                idEnte
        );

        if (!response.containsKey("items")) {
            return Collections.emptyList();
        }

        JsonArray itemsArray = response.getJsonArray("items");

        List<Map<String, Object>> items = itemsArray.stream()
                .map(jsonValue -> objectMapper.convertValue(jsonValue, new TypeReference<Map<String, Object>>() {
                }))
                .toList();

        return items.stream()
                .map(map -> objectMapper.convertValue(map, RREODto.class))
                .collect(Collectors.toList());
    }
}
