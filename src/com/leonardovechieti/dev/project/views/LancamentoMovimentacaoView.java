/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.model.enums.Operacao;
import com.leonardovechieti.dev.project.repository.CentroDeCustoRepository;
import com.leonardovechieti.dev.project.repository.EstoqueRepository;
import com.leonardovechieti.dev.project.repository.ProdutoRepository;
import net.proteanit.sql.DbUtils;
import org.apache.hadoop.hive.metastore.api.ThriftHiveMetastore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Leonardo
 */
public class LancamentoMovimentacaoView extends javax.swing.JFrame {
    private String id = null;
    private String idProduto = null;
    ResultSet rs = null;
    private java.util.List<Estoque> listaEstoque = new java.util.ArrayList<Estoque>();

    public LancamentoMovimentacaoView() {
        initialize();
        listaLancamentos();
    }

    public LancamentoMovimentacaoView(String id) {
        Produto produto = new Produto();
        produto.setId(Integer.parseInt(id));
        initialize();

    }

    private void buscaTotalDoEstoque(Produto produto) {
        EstoqueRepository estoqueRepository = new EstoqueRepository();
        String total = estoqueRepository.retornaTotalEstoque(produto);
    }

    private void initialize() {
        initComponents();
        setIcon();
        formataBotoes();
        formataTabela();
        setSelectBox();
        tabelaEstoque.setEnabled(false);
        tabelaProdutos.setEnabled(false);
        btnCancelarLancamento.setEnabled(false);
        btnLancarProduto.setEnabled(false);
        painelProdutos.setEnabled(false);
        btnLancarProduto.setVisible(false);
        //Campos desabilitados
        labelDescricao.setVisible(false);
        txtDescricaoMovimentacao.setVisible(false);
        labelValorTotalMovimentacao.setVisible(false);
        txtValorTotalMovimentacao.setVisible(false);


        EstoqueRepository estoqueRepository = new EstoqueRepository();
    }

    private void setSelectBox(){
        //seta os itens do select box centro de custo
        comboBoxCentroDeCusto.removeAllItems();
        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
        String todosOsNomes = centroDeCustoRepository.todosNomes();
        String[] nomes = todosOsNomes.split(",");
        for (String nome : nomes) {
            comboBoxCentroDeCusto.addItem(nome);
        }
        //seta os itens do select box tipo de movimentacao
        comboBoxOperacao.removeAllItems();
        //Passa os valore do Enum Operacoes para o select box
        for (Operacao operacao : Operacao.values()) {
            comboBoxOperacao.addItem(String.valueOf(operacao));
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
        //Reseta a tabela

        //Passa os valores do array para a tabela
        DefaultTableModel model = (DefaultTableModel) tabelaEstoque.getModel();
        model.setRowCount(0);
        for (Estoque estoque : listaEstoque) {
            model.addRow(new Object[]{estoque.getIdProduto(),estoque.getNomeProduto(), estoque.getQuantidade(), estoque.getValor()});
        }
        adicionaBotaoDeletar();
        //Define o tamanho das colunas
        tabelaEstoque.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabelaEstoque.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaEstoque.getColumnModel().getColumn(2).setPreferredWidth(60);
        tabelaEstoque.getColumnModel().getColumn(3).setPreferredWidth(60);

    }

    private void adicionaBotaoDeletar(){
        //Adiciona mais uma coluna na tabela
        DefaultTableModel model = (DefaultTableModel) tabelaEstoque.getModel();
        //model.addColumn("");
        //Seta o valor de deletar para cada linha
        for (int i = 0; i < tabelaEstoque.getRowCount(); i++) {
            model.setValueAt("Cancelar", i, 4);
        }

        TableColumn buttonColumn = tabelaEstoque.getColumnModel().getColumn(4);

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
                if (coluna == 4) {
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
        tabelaProdutos.setRowHeight(25);

        //Seta o tamanho da fonte
        tabelaEstoque.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaProdutos.setFont(new Font("Arial", Font.PLAIN, 14));

        //Seta o tamanho da fonte do cabeçalho
        tabelaEstoque.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabelaProdutos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        //Seta a cor da linha quando selecionada
        tabelaEstoque.setSelectionBackground(new Color(152, 156, 157));
        tabelaProdutos.setSelectionBackground(new Color(152, 156, 157));

        //Seta a cor da fonte quando selecionada
        tabelaEstoque.setSelectionForeground(Color.black);
        tabelaProdutos.setSelectionForeground(Color.black);

        //Bloqueia a edição da tabela
        tabelaEstoque.setDefaultEditor(Object.class, null);
        tabelaProdutos.setDefaultEditor(Object.class, null);
    }

    public void pesquisarProdutos() throws SQLException {
        ProdutoRepository produtoRepository = new ProdutoRepository();
        rs = (ResultSet) produtoRepository.pesquisar(textPesquisarProdutos.getText());
        tabelaProdutos.setModel(DbUtils.resultSetToTableModel(rs));

        //Seta o tamanho das colunas
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(230);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(80);

        //Reseta o id do produto selecionado
        idProduto=null;

        //Desabilita os botões de alterar e excluir
        //btnAlterar.setEnabled(false);
        //btnDeletar.setEnabled(false);

        //Fecha a conexão
        produtoRepository.fecharConexao();

    }

    private void lancamentoDeProduto() {
        if (txtProduto.getText().equals("")) {
            new MessageView("Alerta!", "Selecione um produto!", "alert");
            return;
        } else if (txtQuantidade.getText().equals("")) {
            new MessageView("Alerta!", "Informe a quantidade!", "alert");
            return;
        } else if (txtValor.getText().equals("")) {
            new MessageView("Alerta!", "Informe o valor!", "alert");
            return;
        } else {
            try {
                //Verifica se o produto já foi lançado
                for (Estoque estoque : listaEstoque) {
                    if (estoque.getIdProduto() == Integer.parseInt(this.idProduto)) {
                        new MessageView("Alerta!", "Produto já lançado!", "alert");
                        return;
                    }
                }
                //Cria um novo estoque
                Estoque estoque = new Estoque();
                estoque.setIdProduto(Integer.parseInt(this.idProduto));
                estoque.setQuantidade(txtQuantidade.getText());
                estoque.setValor(txtValor.getText());
                estoque.setNomeProduto(txtProduto.getText());

                //Adiciona o estoque criado no array de estoques ta tabela inicial
                listaEstoque.add(estoque);
                //System.out.println(listaEstoque.size());
                listaLancamentos();
                new MessageView("Sucesso!", "Movimentação lançada com sucesso!", "success");
            } catch (Exception e) {
                new MessageView("Erro!", "Erro ao lançar movimentação do produto ("+this.idProduto+"). Verifique!", "erro");
            }

        }
    }

    private void verificaEstoqueDoProduto(String idProduto){
        ProdutoRepository produtoRepository = new ProdutoRepository();
        Produto produto = new Produto();
        produto = produtoRepository.buscaId(idProduto);
        
    }

    private void seleciona(String id){
        //Abre o outra tela de gereciamento de estoque

        //Altera o botao de cadastrar para editar
        btnPrincipal.setText("Editar");
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/update1.png")));
    }
    private void novoLancamento()  {
        //Abre o outra tela de gereciamento de estoque

        //Altera o botao de cadastrar para editar
        //btnPrincipal.setText("Cadastrar");
        //btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/save1.png")));
    }
    private void alterarLancamento() {
        //Abre o outra tela de gereciamento de estoque

        //Altera o botao de cadastrar para editar
        //btnPrincipal.setText("Cadastrar");
        //btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/save1.png")));
    }

    private void deletarLancamento(String id) {
        //Pergunta se o usuário deseja realmente excluir
        int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir!", "Excluir", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
//            CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
//            String resposta = centroDeCustoRepository.excluir(id);
//            if (resposta.equals("SUCCESS")) {
//                new MessageView("Sucesso!", "Centro de custo deletado com sucesso!", "success" );
//                limparCampos();
//                try {
//                    pesquisar();
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            } else {
//                new MessageView("Erro!", "Erro ao deletar usuário, o centro de custo selecionado " +
//                        "já está em uso em funcionalidades do sistema!", "error");
//            }
        }
    }
    private void limparCampos() {
        //btnDeletarEstoque.setVisible(false);
        btnLancarProduto.setVisible(false);
        btnPrincipal.setText("Novo Lançamento");
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png")));
        this.id = null;
    }
    private void pegaIdProduto(){
        int setar = tabelaProdutos.getSelectedRow();
        this.idProduto = tabelaProdutos.getModel().getValueAt(setar,0).toString();
        if(this.idProduto != null) {
            System.out.println("idProduto: " + idProduto);
            btnLancarProduto.setVisible(true);
            txtProduto.setText(tabelaProdutos.getModel().getValueAt(setar, 1).toString());
        }
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
        jPanel1 = new javax.swing.JPanel();
        comboBoxCentroDeCusto = new javax.swing.JComboBox<>();
        labelCentroDeCusto = new javax.swing.JLabel();
        txtValorTotalMovimentacao = new javax.swing.JFormattedTextField();
        labelValorTotalMovimentacao = new javax.swing.JLabel();
        labelDescricao = new javax.swing.JLabel();
        txtDescricaoMovimentacao = new javax.swing.JTextField();
        labelOperacao = new javax.swing.JLabel();
        comboBoxOperacao = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        labelCentroDeCustoDestino = new javax.swing.JLabel();
        comboBoxCentroDeCustoDestino = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        LabelProduto = new javax.swing.JLabel();
        txtProduto = new javax.swing.JTextField();
        labelValor = new javax.swing.JLabel();
        txtValor = new javax.swing.JFormattedTextField();
        labelIdProduto = new javax.swing.JLabel();
        LabelQuantidade = new javax.swing.JLabel();
        txtQuantidade = new javax.swing.JFormattedTextField();
        btnLancarProduto = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaProdutos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        textPesquisarProdutos = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JLabel();
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
                "ID PRODUTO", "PRODUTO", "QUANTIDADE", "VALOR", ""
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movimentação", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        comboBoxCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCusto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCusto.setText("Centro de Custo:");

        txtValorTotalMovimentacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtValorTotalMovimentacao.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelValorTotalMovimentacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelValorTotalMovimentacao.setText("Valor Total:");

        labelDescricao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelDescricao.setText("Descrição:");

        txtDescricaoMovimentacao.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelOperacao.setText("Operação:");

        comboBoxOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxOperacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxOperacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxOperacaoActionPerformed(evt);
            }
        });

        jLabel1.setText("ID:");

        labelCentroDeCustoDestino.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelCentroDeCustoDestino.setText("Centro de Custo Destino:");

        comboBoxCentroDeCustoDestino.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxCentroDeCustoDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelCentroDeCusto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(158, 158, 158))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(labelValorTotalMovimentacao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtValorTotalMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelDescricao)
                                    .addComponent(labelOperacao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(labelCentroDeCustoDestino)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(comboBoxCentroDeCustoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtDescricaoMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(99, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxCentroDeCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCentroDeCusto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelOperacao)
                            .addComponent(comboBoxOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelCentroDeCustoDestino)
                            .addComponent(comboBoxCentroDeCustoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelDescricao)
                    .addComponent(txtDescricaoMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtValorTotalMovimentacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelValorTotalMovimentacao))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painelLayout = new javax.swing.GroupLayout(painel);
        painel.setLayout(painelLayout);
        painelLayout.setHorizontalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)))
        );
        painelLayout.setVerticalGroup(
            painelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        painelProdutos.addTab("Movimentação", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        jPanel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        LabelProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabelProduto.setText("Produto:");

        txtProduto.setEditable(false);
        txtProduto.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelValor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelValor.setText("Valor:");

        txtValor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtValor.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelIdProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelIdProduto.setText("ID");

        LabelQuantidade.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabelQuantidade.setText("Quantidade:");

        txtQuantidade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtQuantidade.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        btnLancarProduto.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnLancarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/mais.png"))); // NOI18N
        btnLancarProduto.setText("Lançar Produto");
        btnLancarProduto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLancarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLancarProdutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(LabelQuantidade)
                        .addGap(18, 18, 18)
                        .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(labelValor)
                        .addGap(18, 18, 18)
                        .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(labelIdProduto))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(LabelProduto)
                        .addGap(18, 18, 18)
                        .addComponent(txtProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(125, 125, 125)
                .addComponent(btnLancarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnLancarProduto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelProduto)
                            .addComponent(txtProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelValor)
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelIdProduto)
                            .addComponent(LabelQuantidade)
                            .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tabelaProdutos.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tabelaProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PRODUTO", "UNIDADE", "PREÇO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaProdutos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelaProdutos.setGridColor(java.awt.SystemColor.scrollbar);
        tabelaProdutos.setRequestFocusEnabled(false);
        tabelaProdutos.setRowMargin(2);
        tabelaProdutos.setSelectionBackground(java.awt.SystemColor.controlHighlight);
        tabelaProdutos.setShowHorizontalLines(false);
        tabelaProdutos.setShowVerticalLines(false);
        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaProdutosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaProdutos);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textPesquisarProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        textPesquisarProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPesquisarProdutosKeyReleased(evt);
            }
        });

        btnPesquisar.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/search-file.png"))); // NOI18N
        btnPesquisar.setText("Pesquisar:");
        btnPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPesquisarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPesquisar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textPesquisarProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textPesquisarProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        painelProdutos.addTab("Produtos", jPanel4);

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

        setSize(new java.awt.Dimension(891, 593));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrincipalActionPerformed
        // TODO add your handling code here:
        //se o id for diferente de nulo
        if (id != null) {
            alterarLancamento();
        } else {
            novoLancamento();
        }
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void btnLancarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLancarProdutoActionPerformed
        // TODO add your handling code here:
        lancamentoDeProduto();
    }//GEN-LAST:event_btnLancarProdutoActionPerformed

    private void tabelaEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaEstoqueMouseClicked
        // TODO add your handling code here:

        //seleciona(this.id);
    }//GEN-LAST:event_tabelaEstoqueMouseClicked

    private void tabelaProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProdutosMouseClicked
        // TODO add your handling code here:
        pegaIdProduto();

    }//GEN-LAST:event_tabelaProdutosMouseClicked

    private void btnCancelarLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarLancamentoActionPerformed

    private void textPesquisarProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPesquisarProdutosKeyReleased

        try {
            // TODO add your handling code here:
            pesquisarProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(LancamentoMovimentacaoView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_textPesquisarProdutosKeyReleased

    private void btnPesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPesquisarMouseClicked
        
    }//GEN-LAST:event_btnPesquisarMouseClicked

    private void comboBoxOperacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxOperacaoActionPerformed
        // TODO add your handling code here:
        //Combo tipo
    }//GEN-LAST:event_comboBoxOperacaoActionPerformed

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
            java.util.logging.Logger.getLogger(LancamentoMovimentacaoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LancamentoMovimentacaoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LancamentoMovimentacaoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LancamentoMovimentacaoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new LancamentoMovimentacaoView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelProduto;
    private javax.swing.JLabel LabelQuantidade;
    private javax.swing.JButton btnCancelarLancamento;
    private javax.swing.JButton btnLancarProduto;
    private javax.swing.JLabel btnPesquisar;
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JComboBox<String> comboBoxCentroDeCusto;
    private javax.swing.JComboBox<String> comboBoxCentroDeCustoDestino;
    private javax.swing.JComboBox<String> comboBoxOperacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCentroDeCusto;
    private javax.swing.JLabel labelCentroDeCustoDestino;
    private javax.swing.JLabel labelDescricao;
    private javax.swing.JLabel labelIdProduto;
    private javax.swing.JLabel labelOperacao;
    private javax.swing.JLabel labelValor;
    private javax.swing.JLabel labelValorTotalMovimentacao;
    private javax.swing.JPanel painel;
    private javax.swing.JTabbedPane painelProdutos;
    private javax.swing.JTable tabelaEstoque;
    private javax.swing.JTable tabelaProdutos;
    private javax.swing.JTextField textPesquisarProdutos;
    private javax.swing.JTextField txtDescricaoMovimentacao;
    private javax.swing.JTextField txtProduto;
    private javax.swing.JFormattedTextField txtQuantidade;
    private javax.swing.JFormattedTextField txtValor;
    private javax.swing.JFormattedTextField txtValorTotalMovimentacao;
    // End of variables declaration//GEN-END:variables
}
