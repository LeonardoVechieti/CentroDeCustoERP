package com.leonardovechieti.dev.project.reports;

import com.leonardovechieti.dev.project.model.dto.ProdutoDTO;
import com.leonardovechieti.dev.project.views.MessageView;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.List;

public class ProdutoReport {
    public void listarProdutos(List<ProdutoDTO> produtos)  {
        if (!produtos.isEmpty()) {
            System.out.println("Gerando relatório de produtos...");
            try {
                InputStream fonte = ProdutoReport.class.getResourceAsStream("/com/leonardovechieti/dev/project/reports/jasper/201-relacao_produtos.jrxml");
                JasperReport report = JasperCompileManager.compileReport(fonte);
                JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(produtos));
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            new MessageView("Aviso!", "Não ha dados para gerar relatório! Verifique", "alert");
        }
    }

    public void listarEstoque(List<ProdutoDTO> produtos) {
        if (!produtos.isEmpty()) {
            System.out.println("Gerando relatório de produtos e estoque...");
            try {
                InputStream fonte = ProdutoReport.class.getResourceAsStream("/com/leonardovechieti/dev/project/reports/jasper/202-estoque_produtos.jrxml");
                JasperReport report = JasperCompileManager.compileReport(fonte);
                JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(produtos));
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            new MessageView("Aviso!", "Não ha dados para gerar relatório! Verifique", "alert");
        }
    }
}
