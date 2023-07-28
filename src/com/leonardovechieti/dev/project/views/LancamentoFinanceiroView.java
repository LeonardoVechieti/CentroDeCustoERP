/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.*;
import com.leonardovechieti.dev.project.model.enums.TipoOperacao;
import com.leonardovechieti.dev.project.repository.*;
import com.leonardovechieti.dev.project.util.Func;
import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;



/**
 *
 * @author Leonardo
 */
public class LancamentoFinanceiroView extends javax.swing.JFrame {
    private String id = null;
    //private Operacao operacao = null;
    //private CentroDeCusto centroDeCusto= null;
    //private CentroDeCusto centroDeCustoDestino = null;
    ResultSet rs = null;
    private java.util.List<Estoque> listaEstoque = new java.util.ArrayList<Estoque>();

    public LancamentoFinanceiroView() {
        initialize();
        this.setVisible(true);
        //Habilita os botoes
        btnLancarProduto.setEnabled(true);
    }

    public LancamentoFinanceiroView(String id) {
        Produto produto = new Produto();
        produto.setId(Integer.parseInt(id));
        initialize();
    }

    public LancamentoFinanceiroView(Operacao operacao, CentroDeCusto centroDeCusto, CentroDeCusto centroDeCustoDestino) {
        initialize();
        this.setVisible(true);
        //this.operacao = operacao;
        //this.centroDeCusto = centroDeCusto;
        //this.centroDeCustoDestino = centroDeCustoDestino;
        //verificaTipoDeOperacao(operacao);
        //Seta os valores do select box
        comboBoxOperacao.setSelectedItem(operacao.getDescricao());
        comboBoxCentroDeCusto.setSelectedItem(centroDeCusto.getNome());
        comboBoxCentroDeCustoDestino.setSelectedItem(centroDeCustoDestino.getNome());
        //Desabilita os campos
        comboBoxOperacao.setEnabled(false);
        comboBoxCentroDeCusto.setEnabled(false);
        comboBoxCentroDeCustoDestino.setEnabled(false);
        comboBoxCentroDeCustoDestino.setVisible(false);
        labelCentroDeCustoDestino.setVisible(false);
        //Habilita os botoes
        btnLancarProduto.setEnabled(true);
        //Troca o nome do botao
        btnPrincipal.setText("Finalizar Lançamento");
    }

    private void verificaTipoDeOperacao(Operacao operacao) {
        if(operacao.getOperacao().equals("TRANSFERENCIA")){
            comboBoxCentroDeCustoDestino.setVisible(true);
            labelCentroDeCustoDestino.setVisible(true);
        }
    }


    private void initialize() {
        initComponents();
        setIcon();
        formataBotoes();
        formataTabela();
        setSelectBox();
        btnCancelarLancamento.setEnabled(false);
        EstoqueRepository estoqueRepository = new EstoqueRepository();
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

    private void buscaLancamentos() {
        EstoqueRepository estoqueRepository = new EstoqueRepository();
        rs = estoqueRepository.listarAll();
        tabelaEstoque.setModel(DbUtils.resultSetToTableModel(rs));
        adicionaBotaoDeletar();
    }

    private void buscaLancamentosPorProduto(Produto produto) {
        EstoqueRepository estoqueRepository = new EstoqueRepository();
        rs = estoqueRepository.listarPorProduto(produto);
        tabelaEstoque.setModel(DbUtils.resultSetToTableModel(rs));
    }

    private void listaLancamentos() {
        //Passa os valores do array para a tabela
        DefaultTableModel model = (DefaultTableModel) tabelaEstoque.getModel();
        model.setRowCount(0);
        for (Estoque estoque : listaEstoque) {
            model.addRow(new Object[]{
                    estoque.getIdProduto(),estoque.getDescricaoProduto(),
                    Func.formataPrecoPadrao(estoque.getQuantidade().toString()),
                    Func.formataPrecoPadrao(estoque.getValorUnitario().toString()),
                    Func.formataPrecoPadrao(estoque.getValorTotal().toString())
            });
        }
        adicionaBotaoDeletar();
        //Define o tamanho das colunas
        tabelaEstoque.getColumnModel().getColumn(0).setPreferredWidth(20);
        tabelaEstoque.getColumnModel().getColumn(1).setPreferredWidth(190);
        tabelaEstoque.getColumnModel().getColumn(2).setPreferredWidth(60);
        tabelaEstoque.getColumnModel().getColumn(3).setPreferredWidth(65);
        tabelaEstoque.getColumnModel().getColumn(4).setPreferredWidth(65);
    }

    private void adicionaBotaoDeletar(){
        //Adiciona mais uma coluna na tabela
        DefaultTableModel model = (DefaultTableModel) tabelaEstoque.getModel();
        //model.addColumn("");
        //Seta o valor de deletar para cada linha
        for (int i = 0; i < tabelaEstoque.getRowCount(); i++) {
            model.setValueAt("Cancelar", i, 5);
        }

        TableColumn buttonColumn = tabelaEstoque.getColumnModel().getColumn(5);

        // Define o renderer personalizado para exibir o botão
        buttonColumn.setCellRenderer(new ButtonRenderer());
        //buttonColumn.setCellRenderer(new DeleteButtonRenderer());

        // Define a largura da coluna do botão
        buttonColumn.setPreferredWidth(100);

        // Define o listener de clique no botão
        buttonColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        // Adiciona um ouvinte de eventos de clique ao botão deletar
        tabelaEstoque.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int idProduto = Integer.parseInt(tabelaEstoque.getValueAt(tabelaEstoque.getSelectedRow(), 0).toString());
                int linha = tabelaEstoque.rowAtPoint(e.getPoint());
                int coluna = tabelaEstoque.columnAtPoint(e.getPoint());
                if (coluna == 5) {
                    //((DefaultTableModel) tabelaEstoque.getModel()).removeRow(linha);
                    //Remove o item de idProduto do array
                    for (Estoque estoque : listaEstoque) {
                        if (estoque.getIdProduto() == idProduto) {
                            listaEstoque.remove(estoque);
                            break;
                        }
                    }
                    //Atualiza a tabela
                    listaLancamentos();
                    //Mostra o array
                    for (Estoque estoque : listaEstoque) {
                        System.out.println(estoque.getIdProduto());
                    }
                }
            }
        });
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

        btnLancarProduto.setBackground(new Color(0, 0, 0, 0));
        btnLancarProduto.setBorderPainted(false);
        btnLancarProduto.setFocusPainted(false);
        btnLancarProduto.setContentAreaFilled(false);
        btnLancarProduto.setOpaque(false);
        btnLancarProduto.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    private void formataTabela(){
        //Seta o tamanho das linhas
        tabelaEstoque.setRowHeight(25);       
        //Seta o tamanho da fonte
        tabelaEstoque.setFont(new Font("Arial", Font.PLAIN, 14));  
        //Seta o tamanho da fonte do cabeçalho
        tabelaEstoque.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        //Seta a cor da linha quando selecionada
        tabelaEstoque.setSelectionBackground(new Color(152, 156, 157));
        //Seta a cor da fonte quando selecionada
        tabelaEstoque.setSelectionForeground(Color.black);
        //Bloqueia a edição da tabela
        tabelaEstoque.setDefaultEditor(Object.class, null);
    }

  
    
    public void adicionaEstoque(Estoque lancamento) {
        //Verifica se o produto já foi lançado
        for (Estoque estoque : listaEstoque) {
            if (estoque.getIdProduto() == Integer.parseInt(String.valueOf(lancamento.getIdProduto()))) {
                new MessageView("Alerta!", "Produto já lançado!", "alert");
                return;
            }
        }
        //Adiciona o estoque recebido no array
        try {
            listaEstoque.add(lancamento);
            listaLancamentos();
            //new MessageView("Sucesso!", "Movimentação do produto lançada com sucesso!", "success");
        } catch (Exception e) {
            new MessageView("Erro!", "Erro ao lançar produto na movimentação!", "error");
        }
        atualizaValorTotal();
    }

    private void atualizaValorTotal() {
        //Atualiza o valor total
        double valorTotal = 0;
        for (Estoque estoque : listaEstoque) {
            valorTotal += estoque.getValorTotal().doubleValue();
        }
        txtValorTotalMovimentacao.setText(Func.formataPrecoPadrao(String.valueOf(valorTotal)));
    }

    private void finalizarLancamento(){
        //Instancia os objetos utilizados
        OperacaoRepository operacao = new OperacaoRepository();
        CentroDeCustoRepository centroDeCusto = new CentroDeCustoRepository();
        LancamentoFinanceiro lancamento = new LancamentoFinanceiro();
        LancamentoFinanceiroRepository lancamentoFinanceiro = new LancamentoFinanceiroRepository();

        //Percorre a lista de estoque adicionando o id do centro de custo e da operação
        for (Estoque estoque : listaEstoque) {
            estoque.setIdCentroDeCusto(centroDeCusto.buscaCentroDeCustoNome(comboBoxCentroDeCusto.getSelectedItem().toString()).getId());
            estoque.setIdOperacao(operacao.buscaIdDescricao(comboBoxOperacao.getSelectedItem().toString()));
        }
        //Lanca uma movimentação financeira
        lancamento.setIdCentroDeCusto(centroDeCusto.buscaCentroDeCustoNome(comboBoxCentroDeCusto.getSelectedItem().toString()).getId());
        lancamento.setIdOperacao(operacao.buscaIdDescricao(comboBoxOperacao.getSelectedItem().toString()));
        lancamento.setDescricao(txtDescricaoMovimentacao.getText());
        lancamento.setValorTotal(txtValorTotalMovimentacao.getText());
        lancamento.setUsuario(Integer.parseInt(PrincipalView.labelIdUsuario.getText()));
        String retornoLancamento = lancamentoFinanceiro.novoLancamento(
                lancamento,
                listaEstoque,
                centroDeCusto.buscaCentroDeCustoNome(comboBoxCentroDeCustoDestino.getSelectedItem().toString()).getId()
                );
        if(retornoLancamento.equals("CREATE")){
            listaEstoque.clear();
            listaLancamentos();
            atualizaValorTotal();
        }else{
            new MessageView("Erro!", "Erro ao lançar movimentação financeira!", "error");
        }
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
        tabelaEstoque = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        comboBoxCentroDeCusto = new javax.swing.JComboBox<>();
        labelCentroDeCusto2 = new javax.swing.JLabel();
        txtValorTotalMovimentacao = new javax.swing.JFormattedTextField();
        labelValorTotalMovimentacao2 = new javax.swing.JLabel();
        labelDescricao2 = new javax.swing.JLabel();
        labelOperacao2 = new javax.swing.JLabel();
        comboBoxOperacao = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        labelCentroDeCustoDestino = new javax.swing.JLabel();
        comboBoxCentroDeCustoDestino = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescricaoMovimentacao = new javax.swing.JTextArea();
        btnLancarProduto = new javax.swing.JButton();
        btnPrincipal = new javax.swing.JButton();
        btnCancelarLancamento = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lançamento de Movimenteções");
        setResizable(false);

        painelProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        tabelaEstoque.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tabelaEstoque.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID P", "PRODUTO", "QUANTIDADE", "VALOR UNITÁRIO", "VALOR TOTAL", ""
            }
        ));
        tabelaEstoque.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelaEstoque.setGridColor(java.awt.SystemColor.scrollbar);
        tabelaEstoque.setRequestFocusEnabled(false);
        tabelaEstoque.setRowMargin(2);
        tabelaEstoque.setSelectionBackground(java.awt.SystemColor.controlHighlight);
        tabelaEstoque.setShowHorizontalLines(false);
        tabelaEstoque.setShowVerticalLines(false);
        tabelaEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaEstoqueMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaEstoque);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movimentação", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        jPanel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        comboBoxCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCusto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelCentroDeCusto2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCusto2.setText("Centro de Custo:");

        txtValorTotalMovimentacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtValorTotalMovimentacao.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelValorTotalMovimentacao2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelValorTotalMovimentacao2.setText("Valor Total:");

        labelDescricao2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelDescricao2.setText("Descrição:");

        labelOperacao2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao2.setText("Operação:");

        comboBoxOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxOperacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxOperacaoActionPerformed(evt);
            }
        });

        jLabel3.setText("ID:");

        labelCentroDeCustoDestino.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCustoDestino.setText("Destino:");

        comboBoxCentroDeCustoDestino.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCustoDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtDescricaoMovimentacao.setColumns(20);
        txtDescricaoMovimentacao.setLineWrap(true);
        txtDescricaoMovimentacao.setRows(5);
        jScrollPane2.setViewportView(txtDescricaoMovimentacao);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBoxCentroDeCustoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(labelCentroDeCusto2)
                                .addGap(110, 110, 110)
                                .addComponent(labelOperacao2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(93, 93, 93)
                                .addComponent(labelCentroDeCustoDestino)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(labelValorTotalMovimentacao2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValorTotalMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(82, 82, 82))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelDescricao2))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelCentroDeCusto2)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(labelOperacao2)
                        .addComponent(labelCentroDeCustoDestino)))
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxCentroDeCustoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(labelDescricao2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtValorTotalMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelValorTotalMovimentacao2))
                .addContainerGap())
        );

        btnLancarProduto.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnLancarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/mais.png"))); // NOI18N
        btnLancarProduto.setText("Lançar Produto");
        btnLancarProduto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLancarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLancarProdutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(painelLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLancarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(btnLancarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(141, Short.MAX_VALUE))
                    .addGroup(painelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel7.getAccessibleContext().setAccessibleName("Dados da Movimentação");

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
        btnPrincipal.setText("Finalizar Lançamento");
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
        // TODO add your handling code here:
        //se o id for diferente de nulo
        if (id != null) {
            //alterarLancamento();
        } else {
            finalizarLancamento();
        }
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void btnCancelarLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarLancamentoActionPerformed

    private void tabelaEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaEstoqueMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaEstoqueMouseClicked

    private void comboBoxOperacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxOperacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxOperacaoActionPerformed

    private void btnLancarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLancarProdutoActionPerformed
        // TODO add your handling code here:
        LancamentoFinanceiroProduto novoLancamentoProdutoView = new LancamentoFinanceiroProduto(
                this
                );
        novoLancamentoProdutoView.setVisible(true);
        
    }//GEN-LAST:event_btnLancarProdutoActionPerformed

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
            java.util.logging.Logger.getLogger(LancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LancamentoFinanceiroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
  
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LancamentoFinanceiroView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelarLancamento;
    private javax.swing.JButton btnLancarProduto;
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JComboBox<String> comboBoxCentroDeCusto;
    private javax.swing.JComboBox<String> comboBoxCentroDeCustoDestino;
    private javax.swing.JComboBox<String> comboBoxOperacao;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCentroDeCusto2;
    private javax.swing.JLabel labelCentroDeCustoDestino;
    private javax.swing.JLabel labelDescricao2;
    private javax.swing.JLabel labelOperacao2;
    private javax.swing.JLabel labelValorTotalMovimentacao2;
    private javax.swing.JPanel painel;
    private javax.swing.JTabbedPane painelProdutos;
    private javax.swing.JTable tabelaEstoque;
    private javax.swing.JTextArea txtDescricaoMovimentacao;
    private javax.swing.JFormattedTextField txtValorTotalMovimentacao;
    // End of variables declaration//GEN-END:variables
}
