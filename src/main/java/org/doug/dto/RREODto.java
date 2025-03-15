package org.doug.dto;

public record RREODto(
        Long exercicio,
        String demonstrativo,
        Long periodo,
        String periodicidade,
        String instituicao,
        Long codIbge,
        String uf,
        Long populacao,
        String anexo,
        String rotulo,
        String coluna,
        String codConta,
        String conta,
        Double valor
) {}