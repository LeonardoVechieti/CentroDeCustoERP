/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.*;
import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.model.enums.TipoOperacao;
import com.leonardovechieti.dev.project.repository.*;
import com.leonardovechieti.dev.project.util.Func;
import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;



/**
 *
 * @author Leonardo
 */
public class ListLancamentoFinanceiroView extends javax.swing.JFrame {
    private String id = null;

    public ListLancamentoFinanceiroView() {
        initialize();
        pesquisarLancamentos();
        this.setVisible(true);
        btnCancelarLancamento.setEnabled(false);

        comboBoxCentroDeCusto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    pesquisarLancamentos();
                }
            }
        });
        comboBoxOperacao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    pesquisarLancamentos();
                }
            }
        });
    }

    private void pesquisarLancamentos() {
        //Verifica se o campo data inicial e valido e o campo data final está vazio, se sim, seta a data final como a data inicial
        if (!txtDataInicial.getText().equals("  /  /    ") && txtDataFinal.getText().equals("  /  /    ")) {
            txtDataFinal.setText(txtDataInicial.getText());
        }
        LancamentoFinanceiroRepository lancamentoFinanceiroRepository = new LancamentoFinanceiroRepository();
        java.util.List<LancamentoFinanceiroDTO> listaLancamentoFinanceiro = new java.util.ArrayList<>();
        if (comboBoxCentroDeCusto.getSelectedItem().toString().equals("TODOS")) {
            if (comboBoxOperacao.getSelectedItem().toString().equals("TODAS")) {
                if (txtDataInicial.getText().equals("  /  /    ") && txtDataFinal.getText().equals("  /  /    ")) {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarAll();
                } else {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarAll(Func.formataDataBanco(txtDataInicial.getText()), Func.formataDataBanco(txtDataFinal.getText()));
                }
            } else {
                OperacaoRepository operacaoRepository = new OperacaoRepository();
                Operacao operacao = operacaoRepository.buscaOperacaoDescricao(comboBoxOperacao.getSelectedItem().toString());
                if (txtDataInicial.getText().equals("  /  /    ") && txtDataFinal.getText().equals("  /  /    ")) {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarPorOperacao(operacao.getId());
                } else {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarPorOperacao(operacao.getId(), Func.formataDataBanco(txtDataInicial.getText()), Func.formataDataBanco(txtDataFinal.getText()));
                }
            }
        } else {
            CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
            CentroDeCusto centroDeCusto = centroDeCustoRepository.buscaCentroDeCustoNome(comboBoxCentroDeCusto.getSelectedItem().toString());
            if (comboBoxOperacao.getSelectedItem().toString().equals("TODAS")) {
                if (txtDataInicial.getText().equals("  /  /    ") && txtDataFinal.getText().equals("  /  /    ")) {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarPorCentroDeCusto(centroDeCusto.getId());
                } else {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarPorCentroDeCusto(centroDeCusto.getId(), Func.formataDataBanco(txtDataInicial.getText()), Func.formataDataBanco(txtDataFinal.getText()));
                }
            } else {
                OperacaoRepository operacaoRepository = new OperacaoRepository();
                Operacao operacao = operacaoRepository.buscaOperacaoDescricao(comboBoxOperacao.getSelectedItem().toString());
                if (txtDataInicial.getText().equals("  /  /    ") && txtDataFinal.getText().equals("  /  /    ")) {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarPorCentroDeCustoOperacao(centroDeCusto.getId(), operacao.getId());
                } else {
                    listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listarPorCentroDeCustoOperacao(centroDeCusto.getId(), operacao.getId(), Func.formataDataBanco(txtDataInicial.getText()), Func.formataDataBanco(txtDataFinal.getText()));
                }
            }
        }
        //Seta a tabela com os dados do ArrayList
        DefaultTableModel model = (DefaultTableModel) tabelaLancamentos.getModel();
        model.setRowCount(0);
        for (LancamentoFinanceiroDTO lancamentoFinanceiroDTO : listaLancamentoFinanceiro) {
            model.addRow(new Object[]{lancamentoFinanceiroDTO.getId(), lancamentoFinanceiroDTO.getOperacao(), lancamentoFinanceiroDTO.getCentro(), lancamentoFinanceiroDTO.getUsuario(), lancamentoFinanceiroDTO.getData(), lancamentoFinanceiroDTO.getValor()});
        }
        formataTabela();
    }


    private void initialize() {
        initComponents();
        setIcon();
        formataBotoes();
        formataTabela();
        setSelectBox();
    }

    private void setSelectBox(){
        //seta os itens do select box centro de custo
        comboBoxCentroDeCusto.removeAllItems();
        comboBoxCentroDeCusto.addItem("TODOS");
        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
        String todosOsNomes = centroDeCustoRepository.todosNomes();
        String[] nomes = todosOsNomes.split(",");
        for (String nome : nomes) {
            comboBoxCentroDeCusto.addItem(nome);
        }
        //seta os itens do select box tipo de opercao
        comboBoxOperacao.removeAllItems();
        comboBoxOperacao.addItem("TODAS");
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

        btnCancelarLancamento.setBackground(new Color(0, 0, 0, 0));
        btnCancelarLancamento.setBorderPainted(false);
        btnCancelarLancamento.setFocusPainted(false);
        btnCancelarLancamento.setContentAreaFilled(false);
        btnCancelarLancamento.setOpaque(false);
        btnCancelarLancamento.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPesquisar.setBackground(new Color(0, 0, 0, 0));
        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setFocusPainted(false);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setOpaque(false);
        btnPesquisar.setCursor(new Cursor(Cursor.HAND_CURSOR));


    }

    private void formataTabela(){
        //Seta o tamanho das linhas
        tabelaLancamentos.setRowHeight(25);       
        //Seta o tamanho da fonte
        tabelaLancamentos.setFont(new Font("Arial", Font.PLAIN, 14));  
        //Seta o tamanho da fonte do cabeçalho
        tabelaLancamentos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        //Seta a cor da linha quando selecionada
        tabelaLancamentos.setSelectionBackground(new Color(152, 156, 157));
        //Seta a cor da fonte quando selecionada
        tabelaLancamentos.setSelectionForeground(Color.black);
        //Bloqueia a edição da tabela
        tabelaLancamentos.setDefaultEditor(Object.class, null);
        tabelaLancamentos.getColumnModel().getColumn(0).setPreferredWidth(20);
        tabelaLancamentos.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabelaLancamentos.getColumnModel().getColumn(2).setPreferredWidth(85);
        tabelaLancamentos.getColumnModel().getColumn(3).setPreferredWidth(85);
        tabelaLancamentos.getColumnModel().getColumn(4).setPreferredWidth(40);
        tabelaLancamentos.getColumnModel().getColumn(5).setPreferredWidth(40);
        //Seta o alinhamento a direita nas colunas
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        tabelaLancamentos.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tabelaLancamentos.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
    }

    private boolean validaCampos() {
        return true;
    }

    private void limparCampos() {
        btnPrincipal.setText("Novo Lançamento");
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png")));
        this.id = null;
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        painelProdutos = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        painel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaLancamentos = new javax.swing.JTable();
        Filtrar = new javax.swing.JPanel();
        btnPesquisar = new javax.swing.JButton();
        comboBoxCentroDeCusto = new javax.swing.JComboBox<>();
        labelCentroDeCusto2 = new javax.swing.JLabel();
        labelOperacao2 = new javax.swing.JLabel();
        comboBoxOperacao = new javax.swing.JComboBox<>();
        labelOperacao3 = new javax.swing.JLabel();
        labelOperacao4 = new javax.swing.JLabel();
        txtDataInicial = new javax.swing.JFormattedTextField();
        txtDataFinal = new javax.swing.JFormattedTextField();
        btnPrincipal = new javax.swing.JButton();
        btnCancelarLancamento = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lançamento de Movimenteções");
        setResizable(false);

        painelProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        tabelaLancamentos.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tabelaLancamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "OPERACAO", "CENTRO", "USUARIO", "DATA", "VALOR TOTAL"
            }
        ));
        tabelaLancamentos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelaLancamentos.setGridColor(java.awt.SystemColor.scrollbar);
        tabelaLancamentos.setRequestFocusEnabled(false);
        tabelaLancamentos.setRowMargin(2);
        tabelaLancamentos.setSelectionBackground(java.awt.SystemColor.controlHighlight);
        tabelaLancamentos.setShowHorizontalLines(false);
        tabelaLancamentos.setShowVerticalLines(false);
        tabelaLancamentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaLancamentosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaLancamentos);

        Filtrar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        Filtrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnPesquisar.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/search-file.png"))); // NOI18N
        btnPesquisar.setText("Pesquisar");
        btnPesquisar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        comboBoxCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCusto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxCentroDeCusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxCentroDeCustoKeyPressed(evt);
            }
        });

        labelCentroDeCusto2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCusto2.setText("Centro de Custo:");

        labelOperacao2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao2.setText("Operação:");

        comboBoxOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxOperacao.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxOperacaoItemStateChanged(evt);
            }
        });
        comboBoxOperacao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxOperacaoFocusLost(evt);
            }
        });
        comboBoxOperacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxOperacaoActionPerformed(evt);
            }
        });
        comboBoxOperacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxOperacaoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                comboBoxOperacaoKeyReleased(evt);
            }
        });

        labelOperacao3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao3.setText("Data:");

        labelOperacao4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao4.setText("a");

        try {
            txtDataInicial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataInicial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataInicialFocusLost(evt);
            }
        });
        txtDataInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataInicialActionPerformed(evt);
            }
        });
        txtDataInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataInicialKeyPressed(evt);
            }
        });

        try {
            txtDataFinal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataFinalFocusLost(evt);
            }
        });
        txtDataFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataFinalActionPerformed(evt);
            }
        });
        txtDataFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataFinalKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout FiltrarLayout = new javax.swing.GroupLayout(Filtrar);
        Filtrar.setLayout(FiltrarLayout);
        FiltrarLayout.setHorizontalGroup(
            FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FiltrarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCentroDeCusto2))
                .addGap(18, 18, 18)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOperacao2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(FiltrarLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(labelOperacao3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FiltrarLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtDataInicial)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelOperacao4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtDataFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addGap(109, 109, 109)
                .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        FiltrarLayout.setVerticalGroup(
            FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FiltrarLayout.createSequentialGroup()
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FiltrarLayout.createSequentialGroup()
                        .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelCentroDeCusto2)
                            .addComponent(labelOperacao2)
                            .addComponent(labelOperacao3))
                        .addGap(4, 4, 4)
                        .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelOperacao4)
                            .addComponent(txtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Filtrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE))
                .addContainerGap())
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Filtrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        painelProdutos.addTab("Movimentação", jPanel2);

        btnPrincipal.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png"))); // NOI18N
        btnPrincipal.setText("Novo Lançamento");
        btnPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrincipalActionPerformed(evt);
            }
        });

        btnCancelarLancamento.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnCancelarLancamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/inativar1.png"))); // NOI18N
        btnCancelarLancamento.setText("Cancelar Lançamento");
        btnCancelarLancamento.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancelarLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarLancamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancelarLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addComponent(painelProdutos)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelProdutos)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelarLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        setSize(new java.awt.Dimension(891, 635));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrincipalActionPerformed
        // TODO add your handling code here
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void btnCancelarLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarLancamentoActionPerformed

    private void tabelaLancamentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaLancamentosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaLancamentosMouseClicked

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // TODO add your handling code here:
        pesquisarLancamentos();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void comboBoxOperacaoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxOperacaoItemStateChanged

    }//GEN-LAST:event_comboBoxOperacaoItemStateChanged

    private void comboBoxOperacaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxOperacaoFocusLost

    }//GEN-LAST:event_comboBoxOperacaoFocusLost

    private void comboBoxOperacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxOperacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacaoActionPerformed

    private void comboBoxOperacaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxOperacaoKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_comboBoxOperacaoKeyReleased

    private void txtDataInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataInicialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicialActionPerformed

    private void txtDataFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinalActionPerformed

    private void txtDataInicialFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataInicialFocusLost
        // TODO add your handling code here:
        String text = txtDataInicial.getText().trim();
        if (text.length() < 10) {
            txtDataInicial.setValue(null);
        } else if (!Func.validaData(txtDataInicial.getText())) {
            txtDataInicial.setValue(null);
        }

    }//GEN-LAST:event_txtDataInicialFocusLost

    private void txtDataFinalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataFinalFocusLost
        // TODO add your handling code here:
        String text = txtDataFinal.getText().trim();
        if (text.length() < 10) {
            txtDataFinal.setValue(null);
        } else if (!Func.validaData(txtDataFinal.getText())) {
            txtDataFinal.setValue(null);
        }
    }//GEN-LAST:event_txtDataFinalFocusLost

    private void comboBoxCentroDeCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCustoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //01
             comboBoxOperacao.requestFocus();
       }
    }//GEN-LAST:event_comboBoxCentroDeCustoKeyPressed

    private void comboBoxOperacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxOperacaoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //02
             txtDataInicial.requestFocus();
       }
    }//GEN-LAST:event_comboBoxOperacaoKeyPressed

    private void txtDataInicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataInicialKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //03
             txtDataFinal.requestFocus();
       }
    }//GEN-LAST:event_txtDataInicialKeyPressed

    private void txtDataFinalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataFinalKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //04
             btnPesquisar.requestFocus();
       }
    }//GEN-LAST:event_txtDataFinalKeyPressed

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
            java.util.logging.Logger.getLogger(ListLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListLancamentoFinanceiroView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Filtrar;
    private javax.swing.JButton btnCancelarLancamento;
    private javax.swing.JButton btnPesquisar;
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JComboBox<String> comboBoxCentroDeCusto;
    private javax.swing.JComboBox<String> comboBoxOperacao;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelCentroDeCusto2;
    private javax.swing.JLabel labelOperacao2;
    private javax.swing.JLabel labelOperacao3;
    private javax.swing.JLabel labelOperacao4;
    private javax.swing.JPanel painel;
    private javax.swing.JTabbedPane painelProdutos;
    private javax.swing.JTable tabelaLancamentos;
    private javax.swing.JFormattedTextField txtDataFinal;
    private javax.swing.JFormattedTextField txtDataInicial;
    // End of variables declaration//GEN-END:variables
}
