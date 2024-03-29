/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.Operacao;
import com.leonardovechieti.dev.project.model.enums.TipoOperacao;
import com.leonardovechieti.dev.project.repository.CentroDeCustoRepository;
import com.leonardovechieti.dev.project.repository.OperacaoRepository;

import java.awt.*;



/**
 *
 * @author Leonardo
 */
public class NovoLancamentoView extends javax.swing.JFrame {

    public NovoLancamentoView() {
        initialize();
    }
    
    private void initialize() {
        initComponents();
        setIcon();
        formataBotoes();
        setSelectBox();
        //Desabilita os campos de centro de custo destino
        labelCentroDeCustoDestino.setVisible(false);
        comboBoxCentroDeCustoDestino.setVisible(false);
    }

    private void setSelectBox(){
        //seta os itens do select box centro de custo
        comboBoxCentroDeCusto.removeAllItems();
        comboBoxCentroDeCustoDestino.removeAllItems();
        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
        String todosOsNomes = centroDeCustoRepository.todosNomes();
        String[] nomes = todosOsNomes.split(",");
        for (String nome : nomes) {
            comboBoxCentroDeCusto.addItem(nome);
            comboBoxCentroDeCustoDestino.addItem(nome);

        }
        //seta os itens do select box tipo de opercao
        comboBoxOperacao.removeAllItems();
        OperacaoRepository operacaoRepository = new OperacaoRepository();
        String todasAsOperacoes = operacaoRepository.todasDescricao();
        String[] operacoes = todasAsOperacoes.split(",");
        for (String operacao : operacoes) {
            comboBoxOperacao.addItem(operacao);
        }

    }
    private void formataBotoes() {
        btnPrincipal.setBackground(new Color(0, 0, 0, 0));
        btnPrincipal.setBorderPainted(false);
        btnPrincipal.setFocusPainted(false);
        btnPrincipal.setContentAreaFilled(false);
        btnPrincipal.setOpaque(false);
        btnPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void abreNovaMovimentacao() {
//        if (comboBoxOperacao.getSelectedItem().equals("TRANSFERENCIA")) {
//            if (comboBoxCentroDeCusto.getSelectedItem().equals(comboBoxCentroDeCustoDestino.getSelectedItem())) {
//                new MessageView("Alerta!", "Selecione centros de custo diferentes", "alert");
//            } else {
//                OperacaoRepository operacaoRepository = new OperacaoRepository();
//                CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
//                LancamentoFinanceiroView lancamento = new LancamentoFinanceiroView(
//                        operacaoRepository.buscaOperacaoDescricao(comboBoxOperacao.getSelectedItem().toString()),
//                        centroDeCustoRepository.buscaCentroDeCustoNome(comboBoxCentroDeCusto.getSelectedItem().toString()),
//                        centroDeCustoRepository.buscaCentroDeCustoNome(comboBoxCentroDeCustoDestino.getSelectedItem().toString())
//                );
//                dispose();
//            }
//        } else {
//            OperacaoRepository operacaoRepository = new OperacaoRepository();
//            CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
//            LancamentoFinanceiroView lancamento = new LancamentoFinanceiroView(
//                    operacaoRepository.buscaOperacaoDescricao(comboBoxOperacao.getSelectedItem().toString()),
//                    centroDeCustoRepository.buscaCentroDeCustoNome(comboBoxCentroDeCusto.getSelectedItem().toString()),
//                    centroDeCustoRepository.buscaCentroDeCustoNome(comboBoxCentroDeCustoDestino.getSelectedItem().toString())
//            );
//            dispose();
//        }
    }

 
    private boolean validarCampos() {
            return true;
        }

    private void setIcon(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/leonardovechieti/dev/project/icon/iconesistema.png")));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnPrincipal = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        labelOperacao = new javax.swing.JLabel();
        comboBoxOperacao = new javax.swing.JComboBox<>();
        labelCentroDeCusto = new javax.swing.JLabel();
        comboBoxCentroDeCusto = new javax.swing.JComboBox<>();
        labelCentroDeCustoDestino = new javax.swing.JLabel();
        comboBoxCentroDeCustoDestino = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Novo Lançamento");
        setResizable(false);

        btnPrincipal.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png"))); // NOI18N
        btnPrincipal.setText("Novo Lançamento");
        btnPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrincipalActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecione as informações de lançamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N

        labelOperacao.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelOperacao.setText("Tipo de operação:");

        comboBoxOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxOperacao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxOperacaoFocusLost(evt);
            }
        });
        comboBoxOperacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                comboBoxOperacaoMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                comboBoxOperacaoMouseExited(evt);
            }
        });
        comboBoxOperacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxOperacaoActionPerformed(evt);
            }
        });

        labelCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelCentroDeCusto.setText("Centro de Custo:");

        comboBoxCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCusto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxCentroDeCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxCentroDeCustoActionPerformed(evt);
            }
        });

        labelCentroDeCustoDestino.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        labelCentroDeCustoDestino.setText(" Destino:");

        comboBoxCentroDeCustoDestino.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCustoDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelOperacao)
                    .addComponent(labelCentroDeCusto)
                    .addComponent(labelCentroDeCustoDestino))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxCentroDeCustoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelOperacao)
                    .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCentroDeCusto)
                    .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCentroDeCustoDestino)
                    .addComponent(comboBoxCentroDeCustoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(444, 298));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void comboBoxOperacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxOperacaoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_comboBoxOperacaoActionPerformed

    private void btnPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrincipalActionPerformed
        // TODO add your handling code here:
        abreNovaMovimentacao();
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void comboBoxCentroDeCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCustoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxCentroDeCustoActionPerformed

    private void comboBoxOperacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboBoxOperacaoMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_comboBoxOperacaoMouseClicked

    private void comboBoxOperacaoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboBoxOperacaoMouseExited
        // TODO add your handling code here:
        
    }//GEN-LAST:event_comboBoxOperacaoMouseExited

    private void comboBoxOperacaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxOperacaoFocusLost
        if (comboBoxOperacao.getSelectedItem().equals("TRANSFERENCIA")) {
            labelCentroDeCustoDestino.setVisible(true);
            comboBoxCentroDeCustoDestino.setVisible(true);
        } else {
            labelCentroDeCustoDestino.setVisible(false);
            comboBoxCentroDeCustoDestino.setVisible(false);
        }
    }//GEN-LAST:event_comboBoxOperacaoFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NovoLancamentoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NovoLancamentoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NovoLancamentoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NovoLancamentoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NovoLancamentoView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JComboBox<String> comboBoxCentroDeCusto;
    private javax.swing.JComboBox<String> comboBoxCentroDeCustoDestino;
    private javax.swing.JComboBox<String> comboBoxOperacao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelCentroDeCusto;
    private javax.swing.JLabel labelCentroDeCustoDestino;
    private javax.swing.JLabel labelOperacao;
    // End of variables declaration//GEN-END:variables
}
