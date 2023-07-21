/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.Operacao;
import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.repository.EstoqueRepository;
import com.leonardovechieti.dev.project.repository.ProdutoRepository;
import com.leonardovechieti.dev.project.util.Func;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Leonardo
 */
public class LancamentoFinanceiroProduto extends javax.swing.JFrame {

    //Armazena a referencia de quem chamou a tela
    private LancamentoFinanceiroView lancamento;
    private Operacao operacao;

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    public String id = null;
    
    /**
     * Creates new form ListProdutosView
     */
    public LancamentoFinanceiroProduto() {
        initialize();
    }

    public LancamentoFinanceiroProduto(LancamentoFinanceiroView lancamento, Operacao operacao) {
        this.lancamento = lancamento;
        this.operacao = operacao;
        initialize();
    }

    private void initialize(){
        initComponents();
        setIcon();
        formataTabela();
        formataBotoes();
        //Inicia pesquisando os produtos
        try {
            pesquisarProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviaLancamento(){
        if (validaCampos()){
            //Cria um novo objeto estoque
            Estoque lancamento = new Estoque();
            //Pega o id do produto
            lancamento.setIdProduto(Integer.parseInt(id));
            //Pega a descricao do produto
            lancamento.setDescricaoProduto(txtProduto.getText());
            //Pega a quantidade
            lancamento.setQuantidade(Double.parseDouble(Func.formataPrecoBanco(txtQuantidade.getText())));
            //Pega o valor unitario
            lancamento.setValorUnitario(Double.parseDouble(Func.formataPrecoBanco(txtValorUnitario.getText())));
            //Pega o valor total
            lancamento.setValorTotal(Double.parseDouble(Func.formataPrecoBanco(txtValorTotal.getText())));
            this.lancamento.adicionaEstoque(lancamento);
            this.dispose();
        }
    }
    
    private Boolean validaCampos(){
        if (id == null) {
            MessageView message = new MessageView("Alerta", "Selecione um produto", "alert");
            return false;
        }
        if (txtQuantidade.getText().isEmpty()) {
            MessageView message = new MessageView("Alerta", "Informe a quantidade", "alert");
            return false;
        }
        if (operacao.getOperacao().toString() != "AJUSTE") {
            if (txtValorUnitario.getText().isEmpty()) {
                MessageView message = new MessageView("Alerta", "Informe o valor unitário", "alert");
                return false;
            }
            if (txtValorTotal.getText().isEmpty()) {
                MessageView message = new MessageView("Alerta", "Informe o valor total", "alert");
                return false;
            }
        }

        return true;
    }

    public void pesquisarProdutos() throws SQLException{
        ProdutoRepository produtoRepository = new ProdutoRepository();
        java.util.List<Produto> listaProdutos = new java.util.ArrayList<Produto>();
        listaProdutos = produtoRepository.pesquisar(textPesquisar.getText());

        DefaultTableModel model = (DefaultTableModel) tabelaProdutos.getModel();
        model.setRowCount(0);
        Object[] row = new Object[4];
        for (int i = 0; i < listaProdutos.size(); i++) {
            row[0] = listaProdutos.get(i).getId();
            row[1] = listaProdutos.get(i).getDescricao();
            row[2] = listaProdutos.get(i).getUnidade();
            row[3] = listaProdutos.get(i).getPreco();
            model.addRow(row);
        }
        //Seta o tamanho das colunas
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(230);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(80);

        //Reseta o id do produto selecionado
        id=null;

        //Fecha a conexão
        produtoRepository.fecharConexao();
    }
    
    private void selecionaProduto(){
        pegaId();
        //Pega o nome do produto
        txtProduto.setText(tabelaProdutos.getValueAt(tabelaProdutos.getSelectedRow(), 1).toString());
        //Pega o valor unitário do produto e seta no campo trocando o ponto por virgula
        txtValorUnitario.setText(tabelaProdutos.getValueAt(tabelaProdutos.getSelectedRow(), 3).toString());
        //Reseta o campo quantidade e valor total
        txtQuantidade.setText("");
        txtValorTotal.setText("");
    }
    private void verificaEstoque(int idProduto) {
        if (txtQuantidade.getText().isEmpty()) {
        }
        else if (idProduto == 0) {
            MessageView message = new MessageView("Alerta", "Selecione um produto!", "alert");
        } else {
            ProdutoRepository produtoRepository = new ProdutoRepository();
            Produto produto = produtoRepository.buscaId(String.valueOf(idProduto));
            EstoqueRepository estoqueRepository = new EstoqueRepository();

            //Troca a virgula por ponto
            String quantidadeDigitada = txtQuantidade.getText().replace(",", ".");

            //Verifica se o produto está com estoque habilitado
            //System.out.println("Estoque habilitado: " + produtoRepository.estoqueHabilitado(idProduto));
            if(produtoRepository.estoqueHabilitado(idProduto)){

                Double totalEstoque = estoqueRepository.retornaTotalEstoque(produto);
                //System.out.println("Total estoque que vem do banco: " + totalEstoque);
                //System.out.println("Quantidade digitada: " + quantidadeDigitada);
                //Verifica se a quantidade do estoque é zero ou nula ou negativa
                if (totalEstoque != null && totalEstoque != 0.00 && totalEstoque != 0 && totalEstoque >= 0) {
                    if (Double.parseDouble(quantidadeDigitada) == 0.00) {
                        MessageView message = new MessageView("Alerta", "Quantidade não pode ser zero.", "alert");
                        //Resta o campo quantidade
                        txtQuantidade.setText("");
                        return;
                    }
                    if (Float.parseFloat(quantidadeDigitada) < 0) {
                        MessageView message = new MessageView("Alerta", "Quantidade não pode ser negativa.", "alert");
                        txtQuantidade.setText("");
                        return;
                    }
                    if (Double.parseDouble(quantidadeDigitada) > (totalEstoque)) {
                        MessageView message = new MessageView("Alerta", "Quantidade maior que o estoque atual (" + totalEstoque + ").", "alert");
                        txtQuantidade.setText("");

                    }
                } else {
                    MessageView message = new MessageView("Alerta", "Produto sem estoque, verifique!.", "alert");
                    //Resta o campo quantidade
                    txtQuantidade.setText("");
                    return;
                }
            } else {

                if (Float.parseFloat(quantidadeDigitada) == 0.00) {
                    MessageView message = new MessageView("Alerta", "Quantidade não pode ser zero.", "alert");
                    txtQuantidade.setText("");
                    return;
                }
                if (Float.parseFloat(quantidadeDigitada) < 0) {
                    MessageView message = new MessageView("Alerta", "Quantidade não pode ser negativa.", "alert");
                    txtQuantidade.setText("");
                }
            }
        }
    }

    private void calculaValorTotal(){
        if(!txtQuantidade.getText().isEmpty() && !txtValorUnitario.getText().isEmpty()){
            //Troca a virgula por ponto
            Double quantidadeDigitada = Double.parseDouble(Func.formataPrecoBanco(txtQuantidade.getText()));
            Double valorUnitarioDigitado = Double.parseDouble(Func.formataPrecoBanco(txtValorUnitario.getText()));
            //Calcula o valor total
            Double valorTotal = quantidadeDigitada * valorUnitarioDigitado;
            //System.out.println(quantidadeDigitada + " " + valorUnitarioDigitado + " " + valorTotal);
            String valorTotalFinal = Func.formataPrecoPadrao(valorTotal.toString());
            txtValorTotal.setText(valorTotalFinal);
        }
    }
    public void abreCadastro(){
        if (id != null) {
            CadastroProdutosView cadastro = new CadastroProdutosView(id);
            cadastro.setVisible(true);
        } else if (id == null) {
            MessageView message = new MessageView("Erro", "Selecione um produto para alterar", "error");
        }
    }

    private void setIcon(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/leonardovechieti/dev/project/icon/iconesistema.png")));
    }

    private void formataTabela(){

        //Seta o tamanho das linhas
        tabelaProdutos.setRowHeight(25);

        //Seta o tamanho da fonte
        tabelaProdutos.setFont(new Font("Arial", Font.PLAIN, 14));

        //Seta o tamanho da fonte do cabeçalho
        tabelaProdutos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        //Seta a cor da linha quando selecionada
        tabelaProdutos.setSelectionBackground(new Color(152, 156, 157));

        //Seta a cor da fonte quando selecionada
        tabelaProdutos.setSelectionForeground(Color.black);

        //Bloqueia a edição da tabela
        tabelaProdutos.setDefaultEditor(Object.class, null);
    }

    private void formataBotoes(){
        
        btnNovoCadastro.setBorderPainted(false);
        btnNovoCadastro.setFocusPainted(false);
        btnNovoCadastro.setContentAreaFilled(false);
        btnNovoCadastro.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    private void pegaId(){
        int setar = tabelaProdutos.getSelectedRow();
        id=tabelaProdutos.getModel().getValueAt(setar,0).toString();
        //printar o id
        System.out.println(id);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        textPesquisar = new javax.swing.JTextField();
        btnPesquisar = new javax.swing.JLabel();
        btnNovoCadastro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaProdutos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        LabelProduto2 = new javax.swing.JLabel();
        txtProduto = new javax.swing.JTextField();
        labelValorUnitario = new javax.swing.JLabel();
        txtValorUnitario = new javax.swing.JFormattedTextField();
        labelIdProduto2 = new javax.swing.JLabel();
        LabelQuantidade2 = new javax.swing.JLabel();
        txtQuantidade = new javax.swing.JFormattedTextField();
        btnLancarProduto = new javax.swing.JButton();
        txtValorTotal = new javax.swing.JFormattedTextField();
        labelValorTotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Listar Produtos");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textPesquisar.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        textPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPesquisarKeyReleased(evt);
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

        btnNovoCadastro.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnNovoCadastro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png"))); // NOI18N
        btnNovoCadastro.setText("Novo Cadastro");
        btnNovoCadastro.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnNovoCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoCadastroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPesquisar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(btnNovoCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNovoCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tabelaProdutos.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tabelaProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PRODUTO", "UNIDADE", "PREÇO"
            }
        ) {
            final boolean[] canEdit = new boolean [] {
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
        jScrollPane1.setViewportView(tabelaProdutos);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        jPanel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        LabelProduto2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabelProduto2.setText("Produto:");

        txtProduto.setEditable(false);
        txtProduto.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelValorUnitario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelValorUnitario.setText("Valor Unitário:");

        txtValorUnitario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtValorUnitario.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtValorUnitario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtValorUnitarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorUnitarioFocusLost(evt);
            }
        });

        labelIdProduto2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelIdProduto2.setText("ID");

        LabelQuantidade2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabelQuantidade2.setText("Quantidade:");

        txtQuantidade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtQuantidade.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtQuantidade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantidadeFocusLost(evt);
            }
        });
        txtQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQuantidadeKeyReleased(evt);
            }
        });

        btnLancarProduto.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnLancarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/mais.png"))); // NOI18N
        btnLancarProduto.setText("Lançar Produto");
        btnLancarProduto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLancarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLancarProdutoActionPerformed(evt);
            }
        });

        txtValorTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtValorTotal.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        labelValorTotal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelValorTotal.setText("Valor Total:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(LabelProduto2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(labelIdProduto2))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(LabelQuantidade2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addComponent(btnLancarProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(labelValorUnitario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelValorTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnLancarProduto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelProduto2)
                            .addComponent(txtProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelIdProduto2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelQuantidade2)
                            .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelValorUnitario)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelValorTotal)
                        .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );

        setSize(new java.awt.Dimension(614, 463));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void textPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPesquisarKeyReleased
        try {
            // TODO add your handling code here:
            pesquisarProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_textPesquisarKeyReleased

    private void btnPesquisarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPesquisarMouseClicked
        try {
            // TODO add your handling code here:
            pesquisarProdutos();
        } catch (SQLException ex) {
            Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPesquisarMouseClicked

    private void tabelaProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProdutosMouseClicked
        // TODO add your handling code here:
        //Verifica se foi clicado duas vezes com o botão direito do mouse
        if (evt.getClickCount() == 2) {
            pegaId();
            abreCadastro();
        } 
        selecionaProduto();


    }//GEN-LAST:event_tabelaProdutosMouseClicked

    private void btnNovoCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoCadastroActionPerformed
        // TODO add your handling code here:
        CadastroProdutosView cadastroProdutos = new CadastroProdutosView();
        cadastroProdutos.setVisible(true);
    }//GEN-LAST:event_btnNovoCadastroActionPerformed

    private void btnLancarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLancarProdutoActionPerformed
        // TODO add your handling code here:
        enviaLancamento();
    }//GEN-LAST:event_btnLancarProdutoActionPerformed

    private void txtQuantidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQuantidadeKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtQuantidadeKeyReleased

    private void txtQuantidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantidadeFocusLost
        // TODO add your handling code here:
        verificaEstoque(Integer.parseInt(this.id));
    }//GEN-LAST:event_txtQuantidadeFocusLost

    private void txtValorUnitarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorUnitarioFocusGained
        // TODO add your handling code here:
        calculaValorTotal();
    }//GEN-LAST:event_txtValorUnitarioFocusGained

    private void txtValorUnitarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorUnitarioFocusLost
        // TODO add your handling code here:
        calculaValorTotal();
    }//GEN-LAST:event_txtValorUnitarioFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
            java.util.logging.Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LancamentoFinanceiroProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new LancamentoFinanceiroProduto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelProduto2;
    private javax.swing.JLabel LabelQuantidade2;
    private javax.swing.JButton btnLancarProduto;
    private javax.swing.JButton btnNovoCadastro;
    private javax.swing.JLabel btnPesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelIdProduto2;
    private javax.swing.JLabel labelValorTotal;
    private javax.swing.JLabel labelValorUnitario;
    private javax.swing.JTable tabelaProdutos;
    private javax.swing.JTextField textPesquisar;
    private javax.swing.JTextField txtProduto;
    private javax.swing.JFormattedTextField txtQuantidade;
    private javax.swing.JFormattedTextField txtValorTotal;
    private javax.swing.JFormattedTextField txtValorUnitario;
    // End of variables declaration//GEN-END:variables

}
