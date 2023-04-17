package com.leonardovechieti.dev.project.util;

import javax.swing.*;
import javax.swing.table.*;

public class TabelaDeletarButton extends JFrame {
    private JTable tabela;

    public TabelaDeletarButton() {
        setTitle("Exemplo de tabela com botão deletar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Cria o modelo de tabela personalizado
        TableModel modelo = new DefaultTableModel(
                new Object[][] {
                        { "1", "Item 1", new JButton("Deletar") },
                        { "2", "Item 2", new JButton("Deletar") },
                        { "3", "Item 3", new JButton("Deletar") }
                },
                new Object[] { "ID", "Descrição", "Ação" }) {
            // Sobrescreve o método getColumnClass para que a última coluna da tabela
            // seja renderizada como um botão
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2)
                    return JButton.class;
                return Object.class;
            }
        };

        // Cria a tabela com o modelo personalizado
        tabela = new JTable(modelo);

        // Adiciona um ouvinte de eventos de clique ao botão deletar
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int linha = tabela.rowAtPoint(e.getPoint());
                int coluna = tabela.columnAtPoint(e.getPoint());
                if (coluna == 2) {
                    ((DefaultTableModel) tabela.getModel()).removeRow(linha);
                }
            }
        });

        // Adiciona a tabela a um JScrollPane e exibe a janela
        JScrollPane scrollPane = new JScrollPane(tabela);
        getContentPane().add(scrollPane);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new TabelaDeletarButton();
    }
}

