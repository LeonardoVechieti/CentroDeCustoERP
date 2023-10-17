/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.*;
import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.model.dto.ProdutoDTO;
import com.leonardovechieti.dev.project.model.dto.ReportDTO;
import com.leonardovechieti.dev.project.model.enums.TipoOperacao;
import com.leonardovechieti.dev.project.model.enums.TipoReceita;
import com.leonardovechieti.dev.project.model.enums.Unidades;
import com.leonardovechieti.dev.project.reports.LancamentoFinanceiroReport;
import com.leonardovechieti.dev.project.reports.ProdutoReport;
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
import java.util.ArrayList;


/**
 *
 * @author Leonardo
 */
public class RelatorioProdutos extends javax.swing.JFrame {
    private String id = null;
    java.util.List<ProdutoDTO> listaProduto = new java.util.ArrayList<>();
    ReportDTO reportDTO = new ReportDTO();
    private Operacao operacao = new Operacao();
    private CentroDeCusto centroDeCusto = new CentroDeCusto();
    private Usuario usuario = new Usuario();

    private String unidade=null;
    private boolean status = Boolean.parseBoolean(null);


    public RelatorioProdutos() {
        ArrayList<LancamentoFinanceiroDTO> listaLancamentoFinanceiro = new ArrayList<>();
        initialize();
        painelProdutos.setEnabledAt(1, false);
        painelProdutos.setEnabledAt(2, false);
        this.setVisible(true);

        comboBoxUnidadeGeral.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!comboBoxUnidadeGeral.getSelectedItem().toString().equals("TODOS")) {
                        unidade = comboBoxUnidadeGeral.getSelectedItem().toString();
                    } else {
                        unidade = null;
                    }
                }
            }
        });

        comboBoxUnidadeEstoque.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!comboBoxUnidadeEstoque.getSelectedItem().toString().equals("TODOS")) {
                        unidade = comboBoxUnidadeEstoque.getSelectedItem().toString();
                    } else {
                        unidade = null;
                    }
                }
            }
        });

        comboBoxStatusGeral.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!comboBoxStatusGeral.getSelectedItem().toString().equals("TODAS")) {
                        status = Boolean.parseBoolean(comboBoxStatusGeral.getSelectedItem().toString());
                    } else {
                        status = Boolean.parseBoolean(null);
                    }
                }
            }
        });

        comboBoxStatusEstoque.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!comboBoxStatusEstoque.getSelectedItem().toString().equals("TODAS")) {
                        status = Boolean.parseBoolean(comboBoxStatusEstoque.getSelectedItem().toString());
                    } else {
                        status = Boolean.parseBoolean(null);
                    }
                }
            }
        });
//        comboBoxOperacao.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if(e.getStateChange() == ItemEvent.SELECTED){
//                    if (!comboBoxOperacao.getSelectedItem().toString().equals("TODAS")) {
//                        OperacaoRepository operacaoRepository = new OperacaoRepository();
//                        operacao = operacaoRepository.buscaOperacaoDescricao(comboBoxOperacao.getSelectedItem().toString());
//                    } else {
//                        operacao.setId(0);
//                    }
//                }
//            }
//        });
//        comboBoxStatus.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                if(e.getStateChange() == ItemEvent.SELECTED){
//                    if (!comboBoxStatus.getSelectedItem().toString().equals("TODAS")) {
//                        tipoReceita = comboBoxStatus.getSelectedItem().toString();
//                    } else {
//                        tipoReceita = null;
//                    }
//                }
//            }
//        });
        comboBoxUsuárioGeral.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!comboBoxUsuárioGeral.getSelectedItem().toString().equals("TODOS")) {
                        UsuarioRepository usuarioRepository = new UsuarioRepository();
                        usuario = usuarioRepository.buscaId(usuarioRepository.buscaPorNome(comboBoxUsuárioGeral.getSelectedItem().toString()).getId());
                    } else {
                        usuario.setId(0);
                    }
                }
            }
        });

        comboBoxUsuárioEstoque.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (!comboBoxUsuárioEstoque.getSelectedItem().toString().equals("TODOS")) {
                        UsuarioRepository usuarioRepository = new UsuarioRepository();
                        usuario = usuarioRepository.buscaId(usuarioRepository.buscaPorNome(comboBoxUsuárioEstoque.getSelectedItem().toString()).getId());
                    } else {
                        usuario.setId(0);
                    }
                }
            }
        });

        comboBoxTipoRelatorio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged (ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (comboBoxTipoRelatorio.getSelectedItem().toString().equals("101-Relação_em_Lista")) {
                        //Desabilita demais abas
                        painelProdutos.setEnabledAt(1, false);
                        painelProdutos.setEnabledAt(2, false);
                        //Habilita aba selecionada
                        painelProdutos.setEnabledAt(0, true);
                        //Seta o foco na aba selecionada
                        painelProdutos.setSelectedIndex(0);
                    }
                    if (comboBoxTipoRelatorio.getSelectedItem().toString().equals("102-Relação_Estoque")) {
                        //Desabilita demais abas
                        painelProdutos.setEnabledAt(0, false);
                        painelProdutos.setEnabledAt(2, false);
                        //Habilita aba selecionada
                        painelProdutos.setEnabledAt(1, true);
                        //Seta o foco na aba selecionada
                        painelProdutos.setSelectedIndex(1);
                    }
                    btnImprimir.setEnabled(true);
                } else {
                    btnImprimir.setEnabled(false);
                }
            }
         });

    }

    private void pesquisarProduto() {
        String dataInicial = null;
        String dataFinal = null;
        //Reseta a lista de lancamentos
        listaProduto = new java.util.ArrayList<>();

        ProdutoRepository produtoRepository = new ProdutoRepository();
        System.out.println("Cheguei aqui");
        if(comboBoxTipoRelatorio.getSelectedItem().toString().equals("101-Relação_em_Lista")){
            //Verifica se o campo data inicial e valido e o campo data final está vazio, se sim, seta a data final como a data inicial
            if (!txtDataInicialGeral.getText().equals("  /  /    ") && txtDataFinalGeral.getText().equals("  /  /    ")) {
                txtDataFinalGeral.setText(txtDataInicialGeral.getText());
                dataInicial = Func.formataDataBanco(txtDataInicialGeral.getText());
                dataFinal = Func.formataDataBanco(txtDataFinalGeral.getText());
            } else if (!txtDataInicialGeral.getText().equals("  /  /    ") && !txtDataFinalGeral.getText().equals("  /  /    ")) {
                dataInicial = Func.formataDataBanco(txtDataInicialGeral.getText());
                dataFinal = Func.formataDataBanco(txtDataFinalGeral.getText());
            }
            listaProduto = produtoRepository.buscaProduto(
                    dataInicial,
                    dataFinal,
                    unidade,
                    boxEstoqueGeral.isSelected(),
                    boxProducaoGeral.isSelected(),
                    boxServicosGeral.isSelected(),
                    status,
                    usuario.getId(),
                    boxOrdemAlfabeticaGeral.isSelected()
            );
        }
        if(comboBoxTipoRelatorio.getSelectedItem().toString().equals("102-Relação_Estoque")){
            //Verifica se o campo data inicial e valido e o campo data final está vazio, se sim, seta a data final como a data inicial
            if (!txtDataInicialEstoque.getText().equals("  /  /    ") && txtDataFinalEstoque.getText().equals("  /  /    ")) {
                txtDataFinalEstoque.setText(txtDataInicialEstoque.getText());
                dataInicial = Func.formataDataBanco(txtDataInicialEstoque.getText());
                dataFinal = Func.formataDataBanco(txtDataFinalEstoque.getText());
            } else if (!txtDataInicialEstoque.getText().equals("  /  /    ") && !txtDataFinalEstoque.getText().equals("  /  /    ")) {
                dataInicial = Func.formataDataBanco(txtDataInicialEstoque.getText());
                dataFinal = Func.formataDataBanco(txtDataFinalEstoque.getText());
            }
            System.out.println("unidade: "+unidade);
            listaProduto = produtoRepository.buscaProduto(
                    dataInicial,
                    dataFinal,
                    unidade,
                    boxEstoqueEstoque.isSelected(),
                    Boolean.parseBoolean(null),
                    Boolean.parseBoolean(null),
                    status,
                    usuario.getId(),
                    boxOrdemAlfabeticaEstoque.isSelected()
            );
        }
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
        comboBoxTipoRelatorio.addItem("101-Relação_em_Lista");
        comboBoxTipoRelatorio.addItem("102-Relação_Estoque");

        //seta os itens do select box centro de custo
        comboBoxUnidadeGeral.removeAllItems();
        comboBoxUnidadeGeral.addItem("TODOS");
        comboBoxUnidadeEstoque.removeAllItems();
        comboBoxUnidadeEstoque.addItem("TODOS");
        //Traz dados do enum para o select box
        for (Unidades unidade : Unidades.values()) {
            comboBoxUnidadeGeral.addItem(unidade.toString());
            comboBoxUnidadeEstoque.addItem(unidade.toString());
        }
//        //seta os itens do select box tipo de opercao
//        comboBoxOperacao.removeAllItems();
//        comboBoxOperacao.addItem("TODAS");
//        OperacaoRepository operacaoRepository = new OperacaoRepository();
//        String todasAsOperacoes = operacaoRepository.todasDescricao();
//        String[] operacoes = todasAsOperacoes.split(",");
//        for (String operacao : operacoes) {
//            comboBoxOperacao.addItem(operacao);
//        }
        //seta os itens do select box tipo de receita
        comboBoxStatusGeral.removeAllItems();
        comboBoxStatusGeral.addItem("TODAS");
        comboBoxStatusGeral.addItem("ATIVOS");
        comboBoxStatusGeral.addItem("INATIVOS");
        comboBoxStatusEstoque.removeAllItems();
        comboBoxStatusEstoque.addItem("TODAS");
        comboBoxStatusEstoque.addItem("ATIVOS");
        comboBoxStatusEstoque.addItem("INATIVOS");

        //seta os itens do select box usuario
        comboBoxUsuárioGeral.removeAllItems();
        comboBoxUsuárioGeral.addItem("TODOS");
        comboBoxUsuárioEstoque.removeAllItems();
        comboBoxUsuárioEstoque.addItem("TODOS");
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        String todosOsUsuarios = usuarioRepository.nomesUsuario();
        String[] usuarios = todosOsUsuarios.split(",");
        for (String usuario : usuarios) {
            comboBoxUsuárioGeral.addItem(usuario);
            comboBoxUsuárioEstoque.addItem(usuario);
        }
        //seta o status
        comboBoxStatusGeral.removeAllItems();
        comboBoxStatusGeral.addItem("TODOS");
        comboBoxStatusGeral.addItem("ATIVOS");
        comboBoxStatusGeral.addItem("INATIVOS");
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
        if(comboBoxTipoRelatorio.getSelectedItem().toString().equals("101-Relação_em_Lista")){
            reportDTO.setNomeReport("101-Produto");
            reportDTO.setFiltroOperacao("TODAS");
            reportDTO.setFiltroCentro("TODOS");
            reportDTO.setFiltroDataInicial(txtDataInicialGeral.getText());
            reportDTO.setFiltroDataFinal(txtDataFinalGeral.getText());
            reportDTO.setFiltroCancelado("TODOS");
            reportDTO.setFiltroUnidade(comboBoxUnidadeGeral.getSelectedItem().toString());
            reportDTO.setFiltroStatus(comboBoxStatusGeral.getSelectedItem().toString());
            reportDTO.setFiltroUsuario(comboBoxUsuárioGeral.getSelectedItem().toString());
        }

    }
    private void gerarRelatorio(){
        if (comboBoxTipoRelatorio.getSelectedItem().toString().equals("101-Relação_em_Lista")) {
            pesquisarProduto();
            configuraReport();
            if (listaProduto.size() > 0) {
                listaProduto.get(0).setReport(reportDTO);
            }
            //Printa dados da lista
            for (ProdutoDTO produtoDTO : listaProduto) {
                System.out.println(produtoDTO.getDescricao());
            }
            ProdutoReport report = new ProdutoReport();
            report.listarProdutos(listaProduto);
        }
        if (comboBoxTipoRelatorio.getSelectedItem().toString().equals("102-Relação_Estoque")) {
            pesquisarProduto();
            configuraReport();
            if (listaProduto.size() > 0) {
                listaProduto.get(0).setReport(reportDTO);
            }
            //Printa dados da lista
            for (ProdutoDTO produtoDTO : listaProduto) {
                System.out.println(produtoDTO.getDescricao());
            }
            ProdutoReport report = new ProdutoReport();
            report.listarEstoque(listaProduto);
        }
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        painelProdutos = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        painel = new javax.swing.JPanel();
        Filtrar = new javax.swing.JPanel();
        comboBoxUnidadeGeral = new javax.swing.JComboBox<>();
        labelUnidade = new javax.swing.JLabel();
        labelOperacao2 = new javax.swing.JLabel();
        labelOperacao3 = new javax.swing.JLabel();
        labelOperacao4 = new javax.swing.JLabel();
        txtDataInicialGeral = new javax.swing.JFormattedTextField();
        txtDataFinalGeral = new javax.swing.JFormattedTextField();
        boxEstoqueGeral = new javax.swing.JCheckBox();
        comboBoxStatusGeral = new javax.swing.JComboBox<>();
        labelStatus = new javax.swing.JLabel();
        comboBoxUsuárioGeral = new javax.swing.JComboBox<>();
        boxServicosGeral = new javax.swing.JCheckBox();
        boxProducaoGeral = new javax.swing.JCheckBox();
        boxOrdemAlfabeticaGeral = new javax.swing.JCheckBox();
        painel2 = new javax.swing.JPanel();
        Filtrar4 = new javax.swing.JPanel();
        comboBoxUnidadeEstoque = new javax.swing.JComboBox<>();
        labelUnidade2 = new javax.swing.JLabel();
        labelOperacao8 = new javax.swing.JLabel();
        labelOperacao9 = new javax.swing.JLabel();
        labelOperacao14 = new javax.swing.JLabel();
        txtDataInicialEstoque = new javax.swing.JFormattedTextField();
        txtDataFinalEstoque = new javax.swing.JFormattedTextField();
        boxEstoqueEstoque = new javax.swing.JCheckBox();
        comboBoxStatusEstoque = new javax.swing.JComboBox<>();
        labelStatus2 = new javax.swing.JLabel();
        comboBoxUsuárioEstoque = new javax.swing.JComboBox<>();
        boxOrdemAlfabeticaEstoque = new javax.swing.JCheckBox();
        Filtrar2 = new javax.swing.JPanel();
        comboBoxCentroDeCusto2 = new javax.swing.JComboBox<>();
        labelCentroDeCusto5 = new javax.swing.JLabel();
        labelOperacao10 = new javax.swing.JLabel();
        comboBoxOperacao2 = new javax.swing.JComboBox<>();
        labelOperacao11 = new javax.swing.JLabel();
        labelOperacao12 = new javax.swing.JLabel();
        txtDataInicial2 = new javax.swing.JFormattedTextField();
        txtDataFinal2 = new javax.swing.JFormattedTextField();
        boxCancelado2 = new javax.swing.JCheckBox();
        comboBoxReceitaDespesa2 = new javax.swing.JComboBox<>();
        labelReceitaDespesa2 = new javax.swing.JLabel();
        comboBoxUsuário2 = new javax.swing.JComboBox<>();
        labelOperacao13 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        comboBoxTipoRelatorio = new javax.swing.JComboBox<>();
        labelCentroDeCusto3 = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relatório Produtos");
        setResizable(false);

        painelProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        Filtrar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        Filtrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        comboBoxUnidadeGeral.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUnidadeGeral.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUnidadeGeral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUnidadeGeralKeyPressed(evt);
            }
        });

        labelUnidade.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelUnidade.setText("Unidade");

        labelOperacao2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao2.setText("Usuário Modificação");

        labelOperacao3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao3.setText("Data Modificação:");

        labelOperacao4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao4.setText("a");

        try {
            txtDataInicialGeral.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataInicialGeral.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataInicialGeralFocusLost(evt);
            }
        });
        txtDataInicialGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataInicialGeralActionPerformed(evt);
            }
        });
        txtDataInicialGeral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataInicialGeralKeyPressed(evt);
            }
        });

        try {
            txtDataFinalGeral.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataFinalGeral.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataFinalGeralFocusLost(evt);
            }
        });
        txtDataFinalGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataFinalGeralActionPerformed(evt);
            }
        });
        txtDataFinalGeral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataFinalGeralKeyPressed(evt);
            }
        });

        boxEstoqueGeral.setText("Estoque");
        boxEstoqueGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxEstoqueGeralActionPerformed(evt);
            }
        });

        comboBoxStatusGeral.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxStatusGeral.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxStatusGeral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxStatusGeralKeyPressed(evt);
            }
        });

        labelStatus.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelStatus.setText("Status:");

        comboBoxUsuárioGeral.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUsuárioGeral.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUsuárioGeral.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxUsuárioGeralItemStateChanged(evt);
            }
        });
        comboBoxUsuárioGeral.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxUsuárioGeralFocusLost(evt);
            }
        });
        comboBoxUsuárioGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxUsuárioGeralActionPerformed(evt);
            }
        });
        comboBoxUsuárioGeral.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUsuárioGeralKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                comboBoxUsuárioGeralKeyReleased(evt);
            }
        });

        boxServicosGeral.setText("Serviços");
        boxServicosGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxServicosGeralActionPerformed(evt);
            }
        });

        boxProducaoGeral.setText("Produção");
        boxProducaoGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxProducaoGeralActionPerformed(evt);
            }
        });

        boxOrdemAlfabeticaGeral.setText("Ordem Alfabética");
        boxOrdemAlfabeticaGeral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxOrdemAlfabeticaGeralActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FiltrarLayout = new javax.swing.GroupLayout(Filtrar);
        Filtrar.setLayout(FiltrarLayout);
        FiltrarLayout.setHorizontalGroup(
            FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FiltrarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FiltrarLayout.createSequentialGroup()
                        .addComponent(comboBoxUsuárioGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(boxEstoqueGeral)
                        .addGap(18, 18, 18)
                        .addComponent(boxProducaoGeral)
                        .addGap(18, 18, 18)
                        .addComponent(boxServicosGeral)
                        .addGap(18, 18, 18)
                        .addComponent(boxOrdemAlfabeticaGeral))
                    .addComponent(labelOperacao2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FiltrarLayout.createSequentialGroup()
                        .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxUnidadeGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelUnidade))
                        .addGap(10, 10, 10)
                        .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(FiltrarLayout.createSequentialGroup()
                                .addComponent(labelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(labelOperacao3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(FiltrarLayout.createSequentialGroup()
                                .addComponent(comboBoxStatusGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(txtDataInicialGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelOperacao4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDataFinalGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 69, Short.MAX_VALUE))
        );
        FiltrarLayout.setVerticalGroup(
            FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FiltrarLayout.createSequentialGroup()
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUnidade)
                    .addComponent(labelOperacao3)
                    .addComponent(labelStatus))
                .addGap(4, 4, 4)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxUnidadeGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOperacao4)
                    .addComponent(txtDataInicialGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataFinalGeral, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxStatusGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelOperacao2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FiltrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxUsuárioGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxEstoqueGeral)
                    .addComponent(boxProducaoGeral)
                    .addComponent(boxServicosGeral)
                    .addComponent(boxOrdemAlfabeticaGeral))
                .addContainerGap(17, Short.MAX_VALUE))
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
                .addContainerGap(78, Short.MAX_VALUE))
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

        Filtrar4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        Filtrar4.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        comboBoxUnidadeEstoque.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUnidadeEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUnidadeEstoque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUnidadeEstoqueKeyPressed(evt);
            }
        });

        labelUnidade2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelUnidade2.setText("Unidade");

        labelOperacao8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao8.setText("Usuário Modificação");

        labelOperacao9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao9.setText("Data Modificação:");

        labelOperacao14.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao14.setText("a");

        try {
            txtDataInicialEstoque.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataInicialEstoque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataInicialEstoqueFocusLost(evt);
            }
        });
        txtDataInicialEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataInicialEstoqueActionPerformed(evt);
            }
        });
        txtDataInicialEstoque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataInicialEstoqueKeyPressed(evt);
            }
        });

        try {
            txtDataFinalEstoque.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataFinalEstoque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataFinalEstoqueFocusLost(evt);
            }
        });
        txtDataFinalEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataFinalEstoqueActionPerformed(evt);
            }
        });
        txtDataFinalEstoque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataFinalEstoqueKeyPressed(evt);
            }
        });

        boxEstoqueEstoque.setText("Apenas Estoque Habilitado");
        boxEstoqueEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxEstoqueEstoqueActionPerformed(evt);
            }
        });

        comboBoxStatusEstoque.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxStatusEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxStatusEstoque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxStatusEstoqueKeyPressed(evt);
            }
        });

        labelStatus2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelStatus2.setText("Status:");

        comboBoxUsuárioEstoque.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUsuárioEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUsuárioEstoque.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxUsuárioEstoqueItemStateChanged(evt);
            }
        });
        comboBoxUsuárioEstoque.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxUsuárioEstoqueFocusLost(evt);
            }
        });
        comboBoxUsuárioEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxUsuárioEstoqueActionPerformed(evt);
            }
        });
        comboBoxUsuárioEstoque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUsuárioEstoqueKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                comboBoxUsuárioEstoqueKeyReleased(evt);
            }
        });

        boxOrdemAlfabeticaEstoque.setText("Ordem Alfabética");
        boxOrdemAlfabeticaEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxOrdemAlfabeticaEstoqueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Filtrar4Layout = new javax.swing.GroupLayout(Filtrar4);
        Filtrar4.setLayout(Filtrar4Layout);
        Filtrar4Layout.setHorizontalGroup(
            Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Filtrar4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Filtrar4Layout.createSequentialGroup()
                        .addComponent(comboBoxUsuárioEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(boxEstoqueEstoque)
                        .addGap(18, 18, 18)
                        .addComponent(boxOrdemAlfabeticaEstoque))
                    .addComponent(labelOperacao8, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(Filtrar4Layout.createSequentialGroup()
                        .addGroup(Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboBoxUnidadeEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelUnidade2))
                        .addGap(10, 10, 10)
                        .addGroup(Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Filtrar4Layout.createSequentialGroup()
                                .addComponent(labelStatus2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(labelOperacao9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Filtrar4Layout.createSequentialGroup()
                                .addComponent(comboBoxStatusEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(23, 23, 23)
                                .addComponent(txtDataInicialEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelOperacao14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDataFinalEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 133, Short.MAX_VALUE))
        );
        Filtrar4Layout.setVerticalGroup(
            Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Filtrar4Layout.createSequentialGroup()
                .addGroup(Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelUnidade2)
                    .addComponent(labelOperacao9)
                    .addComponent(labelStatus2))
                .addGap(4, 4, 4)
                .addGroup(Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxUnidadeEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOperacao14)
                    .addComponent(txtDataInicialEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataFinalEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxStatusEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelOperacao8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Filtrar4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxUsuárioEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxEstoqueEstoque)
                    .addComponent(boxOrdemAlfabeticaEstoque))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painel2Layout = new javax.swing.GroupLayout(painel2);
        painel2.setLayout(painel2Layout);
        painel2Layout.setHorizontalGroup(
            painel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Filtrar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        painel2Layout.setVerticalGroup(
            painel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Filtrar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        painelProdutos.addTab("Estoque", painel2);

        Filtrar2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        Filtrar2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        comboBoxCentroDeCusto2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCusto2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxCentroDeCusto2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxCentroDeCusto2KeyPressed(evt);
            }
        });

        labelCentroDeCusto5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCusto5.setText("Unidade");

        labelOperacao10.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao10.setText("Usuário de Lançamento:");

        comboBoxOperacao2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxOperacao2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxOperacao2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxOperacao2ItemStateChanged(evt);
            }
        });
        comboBoxOperacao2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxOperacao2FocusLost(evt);
            }
        });
        comboBoxOperacao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxOperacao2ActionPerformed(evt);
            }
        });
        comboBoxOperacao2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxOperacao2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                comboBoxOperacao2KeyReleased(evt);
            }
        });

        labelOperacao11.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao11.setText("Data:");

        labelOperacao12.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao12.setText("a");

        try {
            txtDataInicial2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataInicial2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataInicial2FocusLost(evt);
            }
        });
        txtDataInicial2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataInicial2ActionPerformed(evt);
            }
        });
        txtDataInicial2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataInicial2KeyPressed(evt);
            }
        });

        try {
            txtDataFinal2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataFinal2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDataFinal2FocusLost(evt);
            }
        });
        txtDataFinal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataFinal2ActionPerformed(evt);
            }
        });
        txtDataFinal2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDataFinal2KeyPressed(evt);
            }
        });

        boxCancelado2.setText("Cancelado");

        comboBoxReceitaDespesa2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxReceitaDespesa2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxReceitaDespesa2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxReceitaDespesa2KeyPressed(evt);
            }
        });

        labelReceitaDespesa2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelReceitaDespesa2.setText("Receita ou Despesa:");

        comboBoxUsuário2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUsuário2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUsuário2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBoxUsuário2ItemStateChanged(evt);
            }
        });
        comboBoxUsuário2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                comboBoxUsuário2FocusLost(evt);
            }
        });
        comboBoxUsuário2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxUsuário2ActionPerformed(evt);
            }
        });
        comboBoxUsuário2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUsuário2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                comboBoxUsuário2KeyReleased(evt);
            }
        });

        labelOperacao13.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao13.setText("Operação:");

        javax.swing.GroupLayout Filtrar2Layout = new javax.swing.GroupLayout(Filtrar2);
        Filtrar2.setLayout(Filtrar2Layout);
        Filtrar2Layout.setHorizontalGroup(
            Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Filtrar2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboBoxCentroDeCusto2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelCentroDeCusto5)
                    .addComponent(labelReceitaDespesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxReceitaDespesa2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Filtrar2Layout.createSequentialGroup()
                        .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(comboBoxOperacao2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelOperacao10, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(labelOperacao13, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(Filtrar2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(labelOperacao11, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(Filtrar2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtDataInicial2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelOperacao12))
                    .addComponent(comboBoxUsuário2, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDataFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxCancelado2))
                .addContainerGap(139, Short.MAX_VALUE))
        );
        Filtrar2Layout.setVerticalGroup(
            Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Filtrar2Layout.createSequentialGroup()
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelCentroDeCusto5)
                    .addComponent(labelOperacao11)
                    .addComponent(labelOperacao13))
                .addGap(4, 4, 4)
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxCentroDeCusto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxOperacao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelOperacao12)
                    .addComponent(txtDataInicial2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDataFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReceitaDespesa2)
                    .addComponent(labelOperacao10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Filtrar2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxReceitaDespesa2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxUsuário2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxCancelado2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        painelProdutos.addTab("Movimentação", Filtrar2);

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

    private void txtDataInicialGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataInicialGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicialGeralActionPerformed

    private void txtDataFinalGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataFinalGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinalGeralActionPerformed

    private void txtDataInicialGeralFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataInicialGeralFocusLost
        // TODO add your handling code here:
        String text = txtDataInicialGeral.getText().trim();
        if (text.length() < 10) {
            txtDataInicialGeral.setValue(null);
        } else if (!Func.validaData(txtDataInicialGeral.getText())) {
            txtDataInicialGeral.setValue(null);
        }

    }//GEN-LAST:event_txtDataInicialGeralFocusLost

    private void txtDataFinalGeralFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataFinalGeralFocusLost
        // TODO add your handling code here:
        String text = txtDataFinalGeral.getText().trim();
        if (text.length() < 10) {
            txtDataFinalGeral.setValue(null);
        } else if (!Func.validaData(txtDataFinalGeral.getText())) {
            txtDataFinalGeral.setValue(null);
        }
    }//GEN-LAST:event_txtDataFinalGeralFocusLost

    private void comboBoxUnidadeGeralKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUnidadeGeralKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //01
             //comboBoxOperacao.requestFocus();
       }
    }//GEN-LAST:event_comboBoxUnidadeGeralKeyPressed

    private void txtDataInicialGeralKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataInicialGeralKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //03
             txtDataFinalGeral.requestFocus();
       }
    }//GEN-LAST:event_txtDataInicialGeralKeyPressed

    private void txtDataFinalGeralKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataFinalGeralKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //04
                comboBoxStatusGeral.requestFocus();
       }
    }//GEN-LAST:event_txtDataFinalGeralKeyPressed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        gerarRelatorio();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void comboBoxStatusGeralKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxStatusGeralKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxStatusGeralKeyPressed

    private void comboBoxUsuárioGeralItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxUsuárioGeralItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioGeralItemStateChanged

    private void comboBoxUsuárioGeralFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxUsuárioGeralFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioGeralFocusLost

    private void comboBoxUsuárioGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxUsuárioGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioGeralActionPerformed

    private void comboBoxUsuárioGeralKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuárioGeralKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioGeralKeyPressed

    private void comboBoxUsuárioGeralKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuárioGeralKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioGeralKeyReleased

    private void comboBoxTipoRelatorioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxTipoRelatorioKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxTipoRelatorioKeyPressed

    private void comboBoxCentroDeCusto2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxCentroDeCusto2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxCentroDeCusto2KeyPressed

    private void comboBoxOperacao2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxOperacao2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacao2ItemStateChanged

    private void comboBoxOperacao2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxOperacao2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacao2FocusLost

    private void comboBoxOperacao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxOperacao2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacao2ActionPerformed

    private void comboBoxOperacao2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxOperacao2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacao2KeyPressed

    private void comboBoxOperacao2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxOperacao2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacao2KeyReleased

    private void txtDataInicial2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataInicial2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicial2FocusLost

    private void txtDataInicial2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataInicial2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicial2ActionPerformed

    private void txtDataInicial2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataInicial2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicial2KeyPressed

    private void txtDataFinal2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataFinal2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinal2FocusLost

    private void txtDataFinal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataFinal2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinal2ActionPerformed

    private void txtDataFinal2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataFinal2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinal2KeyPressed

    private void comboBoxReceitaDespesa2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxReceitaDespesa2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxReceitaDespesa2KeyPressed

    private void comboBoxUsuário2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxUsuário2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuário2ItemStateChanged

    private void comboBoxUsuário2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxUsuário2FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuário2FocusLost

    private void comboBoxUsuário2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxUsuário2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuário2ActionPerformed

    private void comboBoxUsuário2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuário2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuário2KeyPressed

    private void comboBoxUsuário2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuário2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuário2KeyReleased

    private void boxEstoqueGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxEstoqueGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxEstoqueGeralActionPerformed

    private void boxServicosGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxServicosGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxServicosGeralActionPerformed

    private void boxProducaoGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxProducaoGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxProducaoGeralActionPerformed

    private void comboBoxUnidadeEstoqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUnidadeEstoqueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUnidadeEstoqueKeyPressed

    private void txtDataInicialEstoqueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataInicialEstoqueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicialEstoqueFocusLost

    private void txtDataInicialEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataInicialEstoqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicialEstoqueActionPerformed

    private void txtDataInicialEstoqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataInicialEstoqueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataInicialEstoqueKeyPressed

    private void txtDataFinalEstoqueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDataFinalEstoqueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinalEstoqueFocusLost

    private void txtDataFinalEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataFinalEstoqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinalEstoqueActionPerformed

    private void txtDataFinalEstoqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataFinalEstoqueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataFinalEstoqueKeyPressed

    private void boxEstoqueEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxEstoqueEstoqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxEstoqueEstoqueActionPerformed

    private void comboBoxStatusEstoqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxStatusEstoqueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxStatusEstoqueKeyPressed

    private void comboBoxUsuárioEstoqueItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBoxUsuárioEstoqueItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioEstoqueItemStateChanged

    private void comboBoxUsuárioEstoqueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_comboBoxUsuárioEstoqueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioEstoqueFocusLost

    private void comboBoxUsuárioEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxUsuárioEstoqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioEstoqueActionPerformed

    private void comboBoxUsuárioEstoqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuárioEstoqueKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioEstoqueKeyPressed

    private void comboBoxUsuárioEstoqueKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUsuárioEstoqueKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxUsuárioEstoqueKeyReleased

    private void boxOrdemAlfabeticaEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxOrdemAlfabeticaEstoqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxOrdemAlfabeticaEstoqueActionPerformed

    private void boxOrdemAlfabeticaGeralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxOrdemAlfabeticaGeralActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxOrdemAlfabeticaGeralActionPerformed

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
            java.util.logging.Logger.getLogger(RelatorioProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatorioProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatorioProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatorioProdutos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RelatorioProdutos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Filtrar;
    private javax.swing.JPanel Filtrar2;
    private javax.swing.JPanel Filtrar4;
    private javax.swing.JCheckBox boxCancelado2;
    private javax.swing.JCheckBox boxEstoqueEstoque;
    private javax.swing.JCheckBox boxEstoqueGeral;
    private javax.swing.JCheckBox boxOrdemAlfabeticaEstoque;
    private javax.swing.JCheckBox boxOrdemAlfabeticaGeral;
    private javax.swing.JCheckBox boxProducaoGeral;
    private javax.swing.JCheckBox boxServicosGeral;
    private javax.swing.JButton btnImprimir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboBoxCentroDeCusto2;
    private javax.swing.JComboBox<String> comboBoxOperacao2;
    private javax.swing.JComboBox<String> comboBoxReceitaDespesa2;
    private javax.swing.JComboBox<String> comboBoxStatusEstoque;
    private javax.swing.JComboBox<String> comboBoxStatusGeral;
    private javax.swing.JComboBox<String> comboBoxTipoRelatorio;
    private javax.swing.JComboBox<String> comboBoxUnidadeEstoque;
    private javax.swing.JComboBox<String> comboBoxUnidadeGeral;
    private javax.swing.JComboBox<String> comboBoxUsuário2;
    private javax.swing.JComboBox<String> comboBoxUsuárioEstoque;
    private javax.swing.JComboBox<String> comboBoxUsuárioGeral;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel labelCentroDeCusto3;
    private javax.swing.JLabel labelCentroDeCusto5;
    private javax.swing.JLabel labelOperacao10;
    private javax.swing.JLabel labelOperacao11;
    private javax.swing.JLabel labelOperacao12;
    private javax.swing.JLabel labelOperacao13;
    private javax.swing.JLabel labelOperacao14;
    private javax.swing.JLabel labelOperacao2;
    private javax.swing.JLabel labelOperacao3;
    private javax.swing.JLabel labelOperacao4;
    private javax.swing.JLabel labelOperacao8;
    private javax.swing.JLabel labelOperacao9;
    private javax.swing.JLabel labelReceitaDespesa2;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JLabel labelStatus2;
    private javax.swing.JLabel labelUnidade;
    private javax.swing.JLabel labelUnidade2;
    private javax.swing.JPanel painel;
    private javax.swing.JPanel painel2;
    private javax.swing.JTabbedPane painelProdutos;
    private javax.swing.JFormattedTextField txtDataFinal2;
    private javax.swing.JFormattedTextField txtDataFinalEstoque;
    private javax.swing.JFormattedTextField txtDataFinalGeral;
    private javax.swing.JFormattedTextField txtDataInicial2;
    private javax.swing.JFormattedTextField txtDataInicialEstoque;
    private javax.swing.JFormattedTextField txtDataInicialGeral;
    // End of variables declaration//GEN-END:variables
}
