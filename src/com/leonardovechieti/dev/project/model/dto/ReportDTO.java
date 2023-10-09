package com.leonardovechieti.dev.project.model.dto;

public class ReportDTO {

    private String nomeReport;
    private String filtroOperacao;
    private String filtroCentro;
    private String filtroDataInicial;
    private String filtroDataFinal;
    private String filtroCancelado;
    private String totalEntrada;
    private String totalSaida;
    private String totalFinal;

    public ReportDTO() { }

    public String getNomeReport() {
        return nomeReport;
    }

    public void setNomeReport(String nomeReport) {
        this.nomeReport = nomeReport;
    }

    public String getFiltroOperacao() {
        return filtroOperacao;
    }

    public void setFiltroOperacao(String filtroOperacao) {
        this.filtroOperacao = filtroOperacao;
    }

    public String getFiltroCentro() {
        return filtroCentro;
    }

    public void setFiltroCentro(String filtroCentro) {
        this.filtroCentro = filtroCentro;
    }

    public String getFiltroDataInicial() {
        return filtroDataInicial;
    }

    public void setFiltroDataInicial(String filtroDataInicial) {
        this.filtroDataInicial = filtroDataInicial;
    }

    public String getFiltroDataFinal() {
        return filtroDataFinal;
    }

    public void setFiltroDataFinal(String filtroDataFinal) {
        this.filtroDataFinal = filtroDataFinal;
    }

    public String getFiltroCancelado() {
        return filtroCancelado;
    }

    public void setFiltroCancelado(String filtroCancelado) {
        this.filtroCancelado = filtroCancelado;
    }

    public String getTotalEntrada() {
        return totalEntrada;
    }

    public void setTotalEntrada(String totalEntrada) {
        this.totalEntrada = totalEntrada;
    }

    public String getTotalSaida() {
        return totalSaida;
    }

    public void setTotalSaida(String totalSaida) {
        this.totalSaida = totalSaida;
    }

    public String getTotalFinal() {
        return totalFinal;
    }

    public void setTotalFinal(String totalFinal) {
        this.totalFinal = totalFinal;
    }

}
