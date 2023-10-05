package com.leonardovechieti.dev.project.reports;

import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.views.MessageView;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.List;

public class LancamentoFinanceiroReport {
    public void listarLancamentosFinanceiros(List<LancamentoFinanceiroDTO> lancamento)  {
        if (!lancamento.isEmpty()) {
            System.out.println("Gerando relatório de lançamentos financeiros...");
            try {
                InputStream fonte = LancamentoFinanceiroReport.class.getResourceAsStream("/com/leonardovechieti/dev/project/reports/jasper/102-lancamento_financeiro.jrxml");
                JasperReport report = JasperCompileManager.compileReport(fonte);
                JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(lancamento));
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            new MessageView("Aviso!", "Não ha dados para gerar relatório! Verifique", "alert");
        }
    }
}
