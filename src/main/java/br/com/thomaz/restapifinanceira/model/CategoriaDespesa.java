package br.com.thomaz.restapifinanceira.model;

import java.text.Normalizer;
import java.util.EnumMap;
import java.util.Map;

public enum CategoriaDespesa {

    ALIMENTACAO, SAUDE, MORADIA, TRANSPORTE, EDUCACAO, LAZER, IMPREVISTOS, OUTRAS;

    private static final EnumMap<CategoriaDespesa, String> nomeCategorias =
            new EnumMap<>(Map.of(
                    ALIMENTACAO, "Alimentação",
                    EDUCACAO, "Educação",
                    IMPREVISTOS, "Imprevistos",
                    LAZER, "Lazer",
                    MORADIA, "Moradia",
                    SAUDE, "Saúde",
                    TRANSPORTE, "Transporte",
                    OUTRAS, "Outras"));

    public static String toString(CategoriaDespesa categoria) {
        return nomeCategorias.get(categoria);
    }

    public static CategoriaDespesa fromString(String nome, CategoriaDespesa enumPadrao) {
        try {
            return valueOf(Normalizer
                    .normalize(nome, Normalizer.Form.NFKD)
                    .replaceAll("\\p{M}", "")
                    .toUpperCase());
        } catch (NullPointerException | IllegalArgumentException e) {
            return enumPadrao;
        }
    }

}
