/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.model.enums.Unidades;
import com.leonardovechieti.dev.project.repository.ProdutoRepository;
import com.leonardovechieti.dev.project.repository.UsuarioRepository;
import com.leonardovechieti.dev.project.util.Func;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;


/**
 *
 * @author Leonardo
 */
public class CadastroProdutosView extends javax.swing.JFrame {
    /**
     * Creates new form ListProdutosView
     */
    private String id;
    public CadastroProdutosView() {
        initialize();
        btnEstoque.setVisible(false);
    }
    public CadastroProdutosView(String id) {
        initialize();
        btnEstoque.setVisible(true);
        painelInformacoes.setVisible(true);
        labelId.setVisible(true);
        Produto produto = new Produto();
        if (id != null) {
            this.id = id;
            ProdutoRepository produtoRepository = new ProdutoRepository();
            produto = produtoRepository.buscaId(id);
            txtDescricao.setText(produto.getDescricao());
            txtPreco.setText(produto.getPreco());
            checkBoxInativar.setSelected(produto.getInativo());
            checkBoxServico.setSelected(produto.getServico());
            checkBoxHabilitaEstoque.setSelected(produto.getEstoque());
            checkBoxHabilitaProducao.setSelected(produto.getProducao());
            comboBoxUnidade.setSelectedItem(produto.getUnidade());
            labelId.setText("ID: " + id);
            labelDataCriacao.setText(Func.formataData(produto.getDataCriacao()));
            labelDataModificacao.setText(Func.formataData(produto.getDataModificacao()));

            //Exibe usuário
            UsuarioRepository usuarioRepository = new UsuarioRepository();
            labelNomeUsuario.setText(usuarioRepository.buscaId(produto.getUsuario()).getNome());

            //Modifica o ambiente
            setTitle("Alterar produtos e serviços");
            btnPrincipal.setText("Alterar");
            btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/update1.png")));
        }
    }

    private void initialize() {
        initComponents();
        setIcon();
        setComboBoxUnidade();
        FormataBotoes();
        painelInformacoes.setVisible(false);
        labelId.setVisible(false);
        ProdutoRepository produtoRepository = new ProdutoRepository();
    }
    private void FormataBotoes() {
        btnPrincipal.setBackground(new Color(0, 0, 0, 0));
        btnPrincipal.setBorderPainted(false);
        btnPrincipal.setFocusPainted(false);
        btnPrincipal.setContentAreaFilled(false);
        btnPrincipal.setOpaque(false);
        btnPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEstoque.setBackground(new Color(0, 0, 0, 0));
        btnEstoque.setBorderPainted(false);
        btnEstoque.setFocusPainted(false);
        btnEstoque.setContentAreaFilled(false);
        btnEstoque.setOpaque(false);
        btnEstoque.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }
    
    private void cadastrarProduto() {
        // TODO add your handling code here:
        if (validarCampos()) {
            ProdutoRepository produtoRepository = new ProdutoRepository();
            String resposta = produtoRepository.salvar(
                    txtDescricao.getText(),
                    txtPreco.getText(),
                    comboBoxUnidade.getSelectedItem().toString(),
                    checkBoxInativar.isSelected(),
                    checkBoxServico.isSelected(),
                    checkBoxHabilitaEstoque.isSelected(),
                    checkBoxHabilitaProducao.isSelected(),
                    Integer.parseInt(PrincipalView.labelIdUsuario.getText())
            );
            if (resposta.equals("CREATE")) {
                Integer id = produtoRepository.ultimoId();
                new MessageView("Sucesso!", "Produto cadastrado com sucesso", "success" );
                limparCampos();
            } else {
                new MessageView("Erro!", "Erro ao cadastrar produto, verifique os campos!", "erro");
            }
        }
    }

    private void alterarProduto() {
        // TODO add your handling code here:
        if (validarCampos()) {
            ProdutoRepository produtoRepository = new ProdutoRepository();
            String resposta = produtoRepository.editar(
                    id,
                    txtDescricao.getText(),
                    txtPreco.getText(),
                    comboBoxUnidade.getSelectedItem().toString(),
                    checkBoxInativar.isSelected(),
                    checkBoxServico.isSelected(),
                    checkBoxHabilitaEstoque.isSelected(),
                    checkBoxHabilitaProducao.isSelected(),
                    Integer.parseInt(PrincipalView.labelIdUsuario.getText())
            );
            if (resposta.equals("SUCCESS")) {
                new MessageView("Sucesso!", "Produto alterado com sucesso", "success" );
            } else {
                new MessageView("Erro!", "Erro ao alterar produto, verifique os campos!", "erro");
            }
        }
    }
    private void limparCampos() {
        txtDescricao.setText(null);
        txtPreco.setText(null);
        checkBoxInativar.setSelected(false);
        checkBoxServico.setSelected(false);
        checkBoxHabilitaEstoque.setSelected(false);
        checkBoxHabilitaProducao.setSelected(false);
    }
    private boolean validarCampos() {
        if (txtDescricao.getText().equals("")) {
            new MessageView("Atenção", "Preencha o campo descrição", "alert");
        } else if (txtPreco.getText().equals("")) {
            new MessageView("Atenção", "Preencha o campo preço", "alert");
        } else {
            //Remove caracteres especiais
            txtDescricao.setText(Func.formatarString(txtDescricao.getText()));
            //UpperCase
            txtDescricao.setText(txtDescricao.getText().toUpperCase());
            txtPreco.setText(txtPreco.getText().toUpperCase());

            return true;
        }
        return false;
    }

    private void setComboBoxUnidade() {
        //Setando o combobox de unidade de acordo com o enum
        comboBoxUnidade.removeAllItems();
        for (Unidades unidades : Unidades.values()) {
            comboBoxUnidade.addItem(String.valueOf(unidades));
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

        paneRoot = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        paneGeral = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        LabelDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        labelPreco = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        comboBoxUnidade = new javax.swing.JComboBox<>();
        checkBoxServico = new javax.swing.JCheckBox();
        txtPreco = new javax.swing.JFormattedTextField();
        checkBoxInativar = new javax.swing.JCheckBox();
        labelId = new javax.swing.JLabel();
        painelInformacoes = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        labelDataCriacao = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labelNomeUsuario = new javax.swing.JLabel();
        labelDataModificacao = new javax.swing.JLabel();
        paneEstoque = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        checkBoxHabilitaEstoque = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        checkBoxHabilitaProducao = new javax.swing.JCheckBox();
        btnPrincipal = new javax.swing.JButton();
        btnEstoque = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de produtos e serviços");
        setResizable(false);

        paneRoot.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados Gerais", 0, 0, new java.awt.Font("Arial", 0, 12))); // NOI18N

        LabelDescricao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabelDescricao.setText("Descrição:");

        txtDescricao.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDescricaoKeyPressed(evt);
            }
        });

        labelPreco.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelPreco.setText("Preço:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel5.setText("Unidade:");

        comboBoxUnidade.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxUnidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboBoxUnidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comboBoxUnidadeKeyPressed(evt);
            }
        });

        checkBoxServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxServico.setText("Serviço");
        checkBoxServico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkBoxServicoKeyPressed(evt);
            }
        });

        txtPreco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        txtPreco.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtPreco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecoKeyPressed(evt);
            }
        });

        checkBoxInativar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxInativar.setText("Inativar cadastro");
        checkBoxInativar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkBoxInativarKeyPressed(evt);
            }
        });

        labelId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelId.setText("ID");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addComponent(LabelDescricao)
                        .addGap(18, 18, 18)
                        .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(44, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(labelPreco)
                                .addGap(18, 18, 18)
                                .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5))
                            .addComponent(checkBoxInativar))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(labelId)
                                .addGap(67, 67, 67))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(comboBoxUnidade, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(checkBoxServico)
                                .addGap(40, 40, 40))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelDescricao)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelPreco)
                    .addComponent(jLabel5)
                    .addComponent(comboBoxUnidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxServico)
                    .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxInativar)
                    .addComponent(labelId))
                .addGap(15, 15, 15))
        );

        painelInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Informações do cadastro", 0, 0, new java.awt.Font("Arial", 0, 13))); // NOI18N

        jLabel3.setText("Data de cadastro: ");

        labelDataCriacao.setText("10/10/1010");

        jLabel7.setText("Última alteração:");

        jLabel9.setText("Usuário:");

        labelNomeUsuario.setText("Nome do usuário");

        labelDataModificacao.setText("10/10/1010");

        javax.swing.GroupLayout painelInformacoesLayout = new javax.swing.GroupLayout(painelInformacoes);
        painelInformacoes.setLayout(painelInformacoesLayout);
        painelInformacoesLayout.setHorizontalGroup(
            painelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDataCriacao)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(2, 2, 2)
                .addComponent(labelDataModificacao)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNomeUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        painelInformacoesLayout.setVerticalGroup(
            painelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(labelNomeUsuario))
                    .addGroup(painelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(labelDataCriacao)
                        .addComponent(jLabel7)
                        .addComponent(labelDataModificacao)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout paneGeralLayout = new javax.swing.GroupLayout(paneGeral);
        paneGeral.setLayout(paneGeralLayout);
        paneGeralLayout.setHorizontalGroup(
            paneGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painelInformacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        paneGeralLayout.setVerticalGroup(
            paneGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(paneGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        paneRoot.addTab("Geral", jPanel2);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados de estoque", 0, 0, new java.awt.Font("Arial", 0, 13))); // NOI18N

        checkBoxHabilitaEstoque.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxHabilitaEstoque.setText("Habilita controle de estoque");
        checkBoxHabilitaEstoque.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkBoxHabilitaEstoqueKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(checkBoxHabilitaEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkBoxHabilitaEstoque)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Produção", 0, 0, new java.awt.Font("Arial", 0, 13))); // NOI18N

        checkBoxHabilitaProducao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxHabilitaProducao.setText("Habilita controle de produçao");
        checkBoxHabilitaProducao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkBoxHabilitaProducaoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(checkBoxHabilitaProducao, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(296, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(checkBoxHabilitaProducao)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout paneEstoqueLayout = new javax.swing.GroupLayout(paneEstoque);
        paneEstoque.setLayout(paneEstoqueLayout);
        paneEstoqueLayout.setHorizontalGroup(
            paneEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneEstoqueLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(paneEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        paneEstoqueLayout.setVerticalGroup(
            paneEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneEstoqueLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paneRoot.addTab("Estoque", paneEstoque);

        btnPrincipal.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png"))); // NOI18N
        btnPrincipal.setText("Cadastrar");
        btnPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrincipalActionPerformed(evt);
            }
        });

        btnEstoque.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnEstoque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/caixa.png"))); // NOI18N
        btnEstoque.setText("Estoque");
        btnEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstoqueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnEstoque)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addComponent(paneRoot, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(paneRoot)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEstoque))
                .addGap(20, 20, 20))
        );

        setSize(new java.awt.Dimension(614, 463));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrincipalActionPerformed
        // TODO add your handling code here:
        //se o id for diferente de nulo
        if (id != null) {
            alterarProduto();
        } else {
            cadastrarProduto();
        }
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void btnEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstoqueActionPerformed
        ControleDeEstoqueView controleEstoque = new ControleDeEstoqueView(this.id);
        controleEstoque.setVisible(true); 
        
    }//GEN-LAST:event_btnEstoqueActionPerformed

    private void txtDescricaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescricaoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //01
            txtPreco.requestFocus();
        }
    }//GEN-LAST:event_txtDescricaoKeyPressed

    private void txtPrecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //02
            comboBoxUnidade.requestFocus();
        }
    }//GEN-LAST:event_txtPrecoKeyPressed

    private void comboBoxUnidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comboBoxUnidadeKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //03
            checkBoxServico.requestFocus();
        }
    }//GEN-LAST:event_comboBoxUnidadeKeyPressed

    private void checkBoxServicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkBoxServicoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //04
            checkBoxInativar.requestFocus();
        }
    }//GEN-LAST:event_checkBoxServicoKeyPressed

    private void checkBoxInativarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkBoxInativarKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //05
            //Troca para o painel de estoque
            paneRoot.setSelectedIndex(1);
            checkBoxHabilitaEstoque.requestFocus();
        }
    }//GEN-LAST:event_checkBoxInativarKeyPressed

    private void checkBoxHabilitaEstoqueKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkBoxHabilitaEstoqueKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //06
            checkBoxHabilitaProducao.requestFocus();
        }
    }//GEN-LAST:event_checkBoxHabilitaEstoqueKeyPressed

    private void checkBoxHabilitaProducaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkBoxHabilitaProducaoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){ //07
            btnPrincipal.requestFocus();
        }
    }//GEN-LAST:event_checkBoxHabilitaProducaoKeyPressed

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
            java.util.logging.Logger.getLogger(CadastroProdutosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroProdutosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroProdutosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroProdutosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadastroProdutosView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelDescricao;
    private javax.swing.JButton btnEstoque;
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JCheckBox checkBoxHabilitaEstoque;
    private javax.swing.JCheckBox checkBoxHabilitaProducao;
    private javax.swing.JCheckBox checkBoxInativar;
    private javax.swing.JCheckBox checkBoxServico;
    private javax.swing.JComboBox<String> comboBoxUnidade;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel labelDataCriacao;
    private javax.swing.JLabel labelDataModificacao;
    private javax.swing.JLabel labelId;
    private javax.swing.JLabel labelNomeUsuario;
    private javax.swing.JLabel labelPreco;
    private javax.swing.JPanel painelInformacoes;
    private javax.swing.JPanel paneEstoque;
    private javax.swing.JPanel paneGeral;
    private javax.swing.JTabbedPane paneRoot;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JFormattedTextField txtPreco;
    // End of variables declaration//GEN-END:variables
}
