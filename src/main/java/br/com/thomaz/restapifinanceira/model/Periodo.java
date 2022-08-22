package br.com.thomaz.restapifinanceira.model;

import java.time.LocalDate;

public class Periodo {

    private final LocalDate inicio;
    private final LocalDate fim;
    
    public static Periodo doMes(int mes, int ano) {
        LocalDate inicio = LocalDate.of(ano, mes, 1).minusDays(1);
        LocalDate fim = diaUmProximoMes(ano, mes);
        
        return new Periodo(inicio, fim);
    }
    
    public static Periodo doRegistro(Registro registro) {
        int ano = registro.getAno();
        int mes = registro.getMes();
        LocalDate inicio = LocalDate.of(ano, mes, 1).minusDays(1);
        LocalDate fim = diaUmProximoMes(ano, mes);
        return new Periodo(inicio, fim);
    }

    public LocalDate ini() {
        return inicio;
    }

    public LocalDate fim() {
        return fim;
    }

    private Periodo(LocalDate inicio, LocalDate fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    private static LocalDate diaUmProximoMes(int ano, int mes) {
        if (mes == 12) {
            ano++;
            mes = 0;
        }
        LocalDate fim = LocalDate.of(ano, mes+1, 1);
        return fim;
    }
    
}
