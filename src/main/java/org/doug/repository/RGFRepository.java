package org.doug.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import org.doug.client.RGFClient;
import org.doug.dto.RGFDto;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RGFRepository {

    @Inject
    @RestClient
    RGFClient rgfClient;

    @Inject
    ObjectMapper objectMapper;

    public List<RGFDto> getRGF(int anoExercicio, String inPeriodicidade, int numeroPeriodo, String codigoTipoDemonstrativo, String nomeAnexo, String codigoEsfera, int idEnte) {
        JsonObject response = rgfClient.getRGF(anoExercicio, inPeriodicidade, numeroPeriodo, codigoTipoDemonstrativo, nomeAnexo, codigoEsfera, idEnte);

        if (!response.containsKey("items")) {
            return Collections.emptyList();
        }

        JsonArray itemsArray = response.getJsonArray("items");

        List<Map<String, Object>> items = itemsArray.stream()
                .map(jsonValue -> objectMapper.convertValue(jsonValue, new TypeReference<Map<String, Object>>() {}))
                .toList();

        return items.stream()
                .map(map -> objectMapper.convertValue(map, RGFDto.class))
                .collect(Collectors.toList());
    }
}

