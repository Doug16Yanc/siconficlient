package org.doug.dto;

public record RGFDto(
        Long exercicio,
        Long periodo,
        String periodicidade,
        String instituicao,
        Long codIbge,
        String uf,
        String coPoder,
        Long populacao,
        String anexo,
        String rotulo,
        String coluna,
        String codConta,
        String conta,
        Double valor
) {
}
