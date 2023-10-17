/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.*;
import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.model.dto.ReportDTO;
import com.leonardovechieti.dev.project.model.enums.TipoReceita;
import com.leonardovechieti.dev.project.reports.LancamentoFinanceiroReport;
import com.leonardovechieti.dev.project.repository.*;
import com.leonardovechieti.dev.project.util.Func;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;


/**
 *
 * @author Leonardo
 */
public class RelatorioLancamentoFinanceiroView extends javax.swing.JFrame {
    private String id = null;
    java.util.List<LancamentoFinanceiroDTO> listaLancamentoFinanceiro = new java.util.ArrayList<>();
    ReportDTO reportDTO = new ReportDTO();

    private Operacao operacao = new Operacao();
    private CentroDeCusto centroDeCusto = new CentroDeCusto();

    private Usuario usuario = new Usuario();

    String tipoReceita = null;

    public RelatorioLancamentoFinanceiroView() {
        ArrayList<LancamentoFinanceiroDTO> listaLancamentoFinanceiro = new ArrayList<>();
        initialize();
        pesquisarLancamentos();
        this.setVisible(true);

        comboBoxCentroDeCusto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    if (!comboBoxCentroDeCusto.getSelectedItem().toString().equals("TODOS")) {
                        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
                        centroDeCusto = centroDeCustoRepository.buscaCentroDeCustoNome(comboBoxCentroDeCusto.getSelectedItem().toString());
                    } else {
                        centroDeCusto.setId(0);
                    }
                }
            }
        });
        comboBoxOperacao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    if (!comboBoxOperacao.getSelectedItem().toString().equals("TODAS")) {
                        OperacaoRepository operacaoRepository = new OperacaoRepository();
                        operacao = operacaoRepository.buscaOperacaoDescricao(comboBoxOperacao.getSelectedItem().toString());
                    } else {
                        operacao.setId(0);
                    }
                }
            }
        });
        comboBoxReceitaDespesa.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    if (!comboBoxReceitaDespesa.getSelectedItem().toString().equals("TODAS")) {
                        tipoReceita = comboBoxReceitaDespesa.getSelectedItem().toString();
                    } else {
                        tipoReceita = null;
                    }
                }
            }
        });
        comboBoxUsuário.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    if (!comboBoxUsuário.getSelectedItem().toString().equals("TODOS")) {
                        UsuarioRepository usuarioRepository = new UsuarioRepository();
                        usuario = usuarioRepository.buscaId(usuarioRepository.buscaPorNome(comboBoxUsuário.getSelectedItem().toString()).getId());
                    } else {
                        usuario.setId(0);
                    }
                }
            }
        });
    }

    private void pesquisarLancamentos() {
        String dataInicial = null;
        String dataFinal = null;
        //Reseta a lista de lancamentos
        listaLancamentoFinanceiro = new java.util.ArrayList<>();
        //Verifica se o campo data inicial e valido e o campo data final está vazio, se sim, seta a data final como a data inicial
        if (!txtDataInicial.getText().equals("  /  /    ") && txtDataFinal.getText().equals("  /  /    ")) {
            txtDataFinal.setText(txtDataInicial.getText());
            dataInicial = Func.formataDataBanco(txtDataInicial.getText());
            dataFinal = Func.formataDataBanco(txtDataFinal.getText());
        } else if (!txtDataInicial.getText().equals("  /  /    ") && !txtDataFinal.getText().equals("  /  /    ")) {
            dataInicial = Func.formataDataBanco(txtDataInicial.getText());
            dataFinal = Func.formataDataBanco(txtDataFinal.getText());
        }
        LancamentoFinanceiroRepository lancamentoFinanceiroRepository = new LancamentoFinanceiroRepository();
        listaLancamentoFinanceiro = lancamentoFinanceiroRepository.listar(
                centroDeCusto.getId(),
                operacao.getId(),
                dataInicial,
                dataFinal,
                boxCancelado.isSelected(),
                tipoReceita,
                usuario.getId()
        );
    }

    private void initialize() {
        initComponents();
        setIcon();
        formataBotoes();
        setSelectBox();
    }

    private void setSelectBox(){
        //Seta o tipo de relatorio
        comboBoxTipoRelatorio.removeAllItems();
        comboBoxTipoRelatorio.addItem("102-Relação_em_Lista");

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
        //seta os itens do select box tipo de receita
        comboBoxReceitaDespesa.removeAllItems();
        comboBoxReceitaDespesa.addItem("TODAS");
        for (TipoReceita tipoReceita : TipoReceita.values()) {
            comboBoxReceitaDespesa.addItem(tipoReceita.toString());
        }
        //seta os itens do select box usuario
        comboBoxUsuário.removeAllItems();
        comboBoxUsuário.addItem("TODOS");
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        String todosOsUsuarios = usuarioRepository.nomesUsuario();
        String[] usuarios = todosOsUsuarios.split(",");
        for (String usuario : usuarios) {
            comboBoxUsuário.addItem(usuario);
        }
    }
    private void formataBotoes() {

        btnImprimir.setBackground(new Color(0, 0, 0, 0));
        btnImprimir.setBorderPainted(false);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setOpaque(false);
        btnImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }
    private boolean validaCampos() {
        return true;
    }

    private void configuraReport(){
        //Passa os dados do filtro para o reportDTO para serem usados no relatorio
        reportDTO.setNomeReport("LancamentoFinanceiroReport");
        reportDTO.setFiltroOperacao(comboBoxOperacao.getSelectedItem().toString());
        reportDTO.setFiltroCentro(comboBoxCentroDeCusto.getSelectedItem().toString());
        reportDTO.setFiltroDataInicial(txtDataInicial.getText());
        reportDTO.setFiltroDataFinal(txtDataFinal.getText());
        reportDTO.setFiltroCancelado(boxCancelado.isSelected() ? "SIM" : "NÃO");
        reportDTO.setFiltroReceita(comboBoxReceitaDespesa.getSelectedItem().toString());
        reportDTO.setFiltroUsuario(comboBoxUsuário.getSelectedItem().toString());
    }
    private void gerarRelatorio(){
        pesquisarLancamentos();
        configuraReport();
        if (listaLancamentoFinanceiro.size() > 0) {
            listaLancamentoFinanceiro.get(0).setReport(reportDTO);
        }
        LancamentoFinanceiroReport report = new LancamentoFinanceiroReport();
        report.listarLancamentosFinanceiros(listaLancamentoFinanceiro);
    }

    private void limparCampos() {
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
        Filtrar = new javax.swing.JPanel();
        comboBoxCentroDeCusto = new javax.swing.JComboBox<>();
        labelCentroDeCusto2 = new javax.swing.JLabel();
        labelOperacao2 = new javax.swing.JLabel();
        comboBoxOperacao = new javax.swing.JComboBox<>();
        labelOperacao3 = new javax.swing.JLabel();
        labelOperacao4 = new javax.swing.JLabel();
        txtDataInicial = new javax.swing.JFormattedTextField();
        txtDataFinal = new javax.swing.JFormattedTextField();
        boxCancelado = new javax.swing.JCheckBox();
        comboBoxReceitaDespesa = new javax.swing.JComboBox<>();
        labelReceitaDespesa = new javax.swing.JLabel();
        comboBoxUsuário = new javax.swing.JComboBox<>();
        labelOperacao5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        comboBoxTipoRelatorio = new javax.swing.JComboBox<>();
        labelCentroDeCusto3 = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relatório Movimenteções Financeiras");
        setResizable(false);

        painelProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        Filtrar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        Filtrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

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
        labelOperacao2.setText("Usuário de Lançamento:");

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

        boxCancelado.setText("Cancelado");

        comboBoxReceitaDespesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxReceitaDespesa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxReceitaDespesa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxReceitaDespesaKeyPressed(evt);
            }
        });

        labelReceitaDespesa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelReceitaDespesa.setText("Receita ou Despesa:");

        comboBoxUsuário.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUsuário.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUsuário.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxUsuárioItemStateChanged(evt);
            }
        });
        comboBoxUsuário.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxUsuárioFocusLost(evt);
            }
        });
        comboBoxUsuário.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxUsuárioActionPerformed(evt);
            }
        });
        comboBoxUsuário.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUsuárioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                comboBoxUsuárioKeyReleased(evt);
            }
        });

        labelOperacao5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao5.setText("Operação:");

        javax.swing.GroupLayout FiltrarLayout = new javax.swing.GroupLayout(Filtrar);
        Filtrar.setLayout(FiltrarLayout);
        FiltrarLayout.setHorizontalGroup(
            FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FiltrarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCentroDeCusto2)
                    .addComponent(labelReceitaDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxReceitaDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FiltrarLayout.createSequentialGroup()
                        .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxOperacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelOperacao2, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(labelOperacao5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(FiltrarLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(labelOperacao3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(FiltrarLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtDataInicial)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelOperacao4))
                    .addComponent(comboBoxUsuário, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxCancelado))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        FiltrarLayout.setVerticalGroup(
            FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FiltrarLayout.createSequentialGroup()
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCentroDeCusto2)
                    .addComponent(labelOperacao3)
                    .addComponent(labelOperacao5))
                .addGap(4, 4, 4)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOperacao4)
                    .addComponent(txtDataInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReceitaDespesa)
                    .addComponent(labelOperacao2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxReceitaDespesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxUsuário, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxCancelado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Filtrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Filtrar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        painelProdutos.addTab("Geral", jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Relatório", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 15))); // NOI18N
        jPanel1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        comboBoxTipoRelatorio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxTipoRelatorio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxTipoRelatorio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxTipoRelatorioKeyPressed(evt);
            }
        });

        labelCentroDeCusto3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCusto3.setText("Tipo de Relatório:");

        btnImprimir.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/print1.png"))); // NOI18N
        btnImprimir.setText(" Gerar Relatório");
        btnImprimir.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCentroDeCusto3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxTipoRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCentroDeCusto3)
                    .addComponent(comboBoxTipoRelatorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelProdutos)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelProdutos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(738, 415));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
                comboBoxReceitaDespesa.requestFocus();
       }
    }//GEN-LAST:event_txtDataFinalKeyPressed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        gerarRelatorio();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void comboBoxReceitaDespesaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxReceitaDespesaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxReceitaDespesaKeyPressed

    private void comboBoxUsuárioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxUsuárioItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioItemStateChanged

    private void comboBoxUsuárioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxUsuárioFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioFocusLost

    private void comboBoxUsuárioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxUsuárioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioActionPerformed

    private void comboBoxUsuárioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuárioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioKeyPressed

    private void comboBoxUsuárioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuárioKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioKeyReleased

    private void comboBoxTipoRelatorioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxTipoRelatorioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxTipoRelatorioKeyPressed

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
            java.util.logging.Logger.getLogger(RelatorioLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatorioLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatorioLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatorioLancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RelatorioLancamentoFinanceiroView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Filtrar;
    private javax.swing.JCheckBox boxCancelado;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JComboBox<String> comboBoxCentroDeCusto;
    private javax.swing.JComboBox<String> comboBoxOperacao;
    private javax.swing.JComboBox<String> comboBoxReceitaDespesa;
    private javax.swing.JComboBox<String> comboBoxTipoRelatorio;
    private javax.swing.JComboBox<String> comboBoxUsuário;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel labelCentroDeCusto2;
    private javax.swing.JLabel labelCentroDeCusto3;
    private javax.swing.JLabel labelOperacao2;
    private javax.swing.JLabel labelOperacao3;
    private javax.swing.JLabel labelOperacao4;
    private javax.swing.JLabel labelOperacao5;
    private javax.swing.JLabel labelReceitaDespesa;
    private javax.swing.JPanel painel;
    private javax.swing.JTabbedPane painelProdutos;
    private javax.swing.JFormattedTextField txtDataFinal;
    private javax.swing.JFormattedTextField txtDataInicial;
    // End of variables declaration//GEN-END:variables
}
