package org.doug.dto;

public record RGFDto(
        Long exercicio,
        Long periodo,
        String periodicidade,
        String instituicao,
        Long cod_ibge,
        String uf,
        String co_poder,
        Long populacao,
        String anexo,
        String rotulo,
        String coluna,
        String cod_conta,
        String conta,
        Double valor
) {
}
