package br.com.thomaz.restapifinanceira.service;

import java.time.LocalDate;

import br.com.thomaz.restapifinanceira.model.Registro;

public class Periodo {

    private LocalDate inicio;
    private LocalDate fim;
    
    private Periodo(LocalDate inicio, LocalDate fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

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

    private static LocalDate diaUmProximoMes(int ano, int mes) {
        if (mes == 12) {
            ano++;
            mes = 0;
        }
        LocalDate fim = LocalDate.of(ano, mes+1, 1);
        return fim;
    }
    
    public LocalDate ini() {
        return inicio;
    }
    
    public LocalDate fim() {
        return fim;
    }
    
}
