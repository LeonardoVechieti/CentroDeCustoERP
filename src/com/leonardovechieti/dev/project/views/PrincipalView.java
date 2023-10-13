/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.Empresa;
import com.leonardovechieti.dev.project.model.Usuario;
import com.leonardovechieti.dev.project.util.Func;

import javax.swing.*;
import javax.swing.plaf.InsetsUIResource;
import java.awt.*;

/**
 *
 * @author Leonardo
 */
public class PrincipalView extends javax.swing.JFrame {

    /**
     * Creates new form PrincipalView
     */
    public Usuario usuario;
    public Empresa empresa;
    
    public PrincipalView() {
        initComponents();
    }
    public PrincipalView(Usuario usuario) {
        initComponents();
        setIcon();
        setUsuario(usuario);
        setEmpresa();
        configuraMenu();
        this.setVisible(true);
    }
    private void setIcon(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/leonardovechieti/dev/project/icon/iconesistema.png")));
    }
    
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
        if(usuario != null){
            labelNomeUsuario.setText(Func.formataNome(usuario.getNome()));
            labelIdUsuario.setText(String.valueOf(usuario.getId()));
            //Seta data atual formatada
            labelDateTime.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(System.currentTimeMillis())));
            atualizaRegras(usuario.getPerfil(), usuario.getRegras());
        }
    }

    private void setEmpresa(){
        try {
            empresa = new com.leonardovechieti.dev.project.config.ConfigParametros().getEmpresa();
            labelNomeEmpresa.setText(empresa.getNome());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar dados da empresa: " + e.getMessage());
        }
    }

    private void atualizaRegras(String perfil, String regras){
        if(perfil.equals("ADMIN")){
            menuCadastrarUsuarios.setVisible(true);
            menuParametrosSistema.setVisible(true);
        }else{
            menuCadastrarUsuarios.setVisible(false);
        }
        if(regras.contains("CAD_PRODUTO")){
            menuCadastrarProdutos.setVisible(true);
            barraCadastrarProdutos.setVisible(true);
        }else{
            menuCadastrarProdutos.setVisible(false);
            barraCadastrarProdutos.setVisible(false);
        }
        if(regras.contains("LIS_PRODUTO")){
            menuListarProdutos.setVisible(true);
            barraListarProdutos.setVisible(true);
        }else{
            menuListarProdutos.setVisible(false);
            barraListarProdutos.setVisible(false);
        }
        if(regras.contains("CAD_CENTRO_DE_CUSTO")){
            menuCadastrarCentroDeCusto.setVisible(true);
            barraCadastroCentroCusto.setVisible(true);
        }else{
            menuCadastrarCentroDeCusto.setVisible(false);
            barraCadastroCentroCusto.setVisible(false);
        }
        if(regras.contains("CAD_OPERACAO")){
            menuCadastrarOpercaoes.setVisible(true);
            barraCadastrarOpercaoes.setVisible(true);
        }else{
            menuCadastrarOpercaoes.setVisible(false);
            barraCadastrarOpercaoes.setVisible(false);
        }
        if(regras.contains("CAD_LANCAMENTO_FINANCEIRO")){
            menuNovoLancamento.setVisible(true);
            barraNovoLancamento.setVisible(true);
        }else{
            menuNovoLancamento.setVisible(false);
            barraNovoLancamento.setVisible(false);
        }
        if(regras.contains("LIS_LANCAMENTO_FINANCEIRO")){
            menuListarLancamento.setVisible(true);
            barraListLancamento.setVisible(true);
        }else{
            menuListarLancamento.setVisible(false);
            barraListLancamento.setVisible(false);
        }
    }

    private void configuraMenu(){
        menuBarraPrincipal.setBackground(Color.decode("#2E2E2E"));
        //menuBarraPrincipal.setForeground(Color.decode("#FFFFFF"));
        menuBarraPrincipal.setBorderPainted(false);
        menuBarraPrincipal.setFocusable(false);
        menuBarraPrincipal.setOpaque(true);
        menuBarraPrincipal.setRequestFocusEnabled(false);
        menuBarraPrincipal.setVerifyInputWhenFocusTarget(false);
        menuBarraPrincipal.setVisible(true);
        menuBarraPrincipal.setBorder(null);
        menuBarraPrincipal.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        menuCadastros.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuMovimentacoes.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuRelatorios.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuSistema.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        //Sub menus
        menuCadastrarProdutos.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuListarProdutos.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuCadastrarCentroDeCusto.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuCadastrarOpercaoes.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuNovoLancamento.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuListarLancamento.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuCadastrarUsuarios.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuProdutosServicos.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        menuParametrosSistema.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        //Remove a borda dos submenus
        menuCadastrarProdutos.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuListarProdutos.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuCadastrarCentroDeCusto.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuCadastrarOpercaoes.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuNovoLancamento.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuListarLancamento.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuCadastrarUsuarios.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuProdutosServicos.setMargin(new InsetsUIResource(0, 0, 0, 0));
        menuParametrosSistema.setMargin(new InsetsUIResource(0, 0, 0, 0));
        //Remove a linha de separacao dos submenus
        menuCadastrarProdutos.setBorderPainted(false);
        menuListarProdutos.setBorderPainted(false);
        menuCadastrarCentroDeCusto.setBorderPainted(false);
        menuCadastrarOpercaoes.setBorderPainted(false);
        menuNovoLancamento.setBorderPainted(false);
        menuListarLancamento.setBorderPainted(false);
        menuCadastrarUsuarios.setBorderPainted(false);
        menuProdutosServicos.setBorderPainted(false);
        menuParametrosSistema.setBorderPainted(false);

        barraAtalhos.setBorderPainted(false);
        barraAtalhos.setFocusable(false);
        barraAtalhos.setOpaque(true);
        barraAtalhos.setRequestFocusEnabled(false);
        barraAtalhos.setVerifyInputWhenFocusTarget(false);
        barraAtalhos.setVisible(true);
        barraAtalhos.setBorder(null);
        barraAtalhos.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        barraCadastrarProdutos.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        barraListarProdutos.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        barraCadastrarOpercaoes.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        barraCadastroCentroCusto.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        barraListLancamento.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        barraNovoLancamento.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));

        rodape.setBackground(Color.decode("#76C2AF"));
        rodape.setForeground(Color.decode("#FFFFFF"));
        rodape.setBorderPainted(false);
        rodape.setFocusable(false);
        rodape.setOpaque(true);
        rodape.setRequestFocusEnabled(false);
        rodape.setVerifyInputWhenFocusTarget(false);
        rodape.setVisible(true);
        rodape.setBorder(null);
        rodape.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        //Troca a cor do texto do rodape para branco
        labelVersao.setForeground(Color.decode("#FFFFFF"));
        labelNomeEmpresa.setForeground(Color.decode("#FFFFFF"));
        labelUsuario.setForeground(Color.decode("#FFFFFF"));
        labelIdUsuario.setForeground(Color.decode("#FFFFFF"));
        jLabel4.setForeground(Color.decode("#FFFFFF"));
        labelNomeUsuario.setForeground(Color.decode("#FFFFFF"));
        labelData.setForeground(Color.decode("#FFFFFF"));
        labelDateTime.setForeground(Color.decode("#FFFFFF"));

        //Espacamento entre os textos do rodape
        labelVersao.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        labelNomeEmpresa.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        labelUsuario.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 2));
        labelData.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 2));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        barraAtalhos = new javax.swing.JToolBar();
        barraCadastrarProdutos = new javax.swing.JButton();
        barraListarProdutos = new javax.swing.JButton();
        barraCadastrarOpercaoes = new javax.swing.JButton();
        barraCadastroCentroCusto = new javax.swing.JButton();
        barraListLancamento = new javax.swing.JButton();
        barraNovoLancamento = new javax.swing.JButton();
        rodape = new javax.swing.JToolBar();
        labelVersao = new javax.swing.JLabel();
        labelNomeEmpresa = new javax.swing.JLabel();
        labelUsuario = new javax.swing.JLabel();
        labelIdUsuario = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelNomeUsuario = new javax.swing.JLabel();
        labelData = new javax.swing.JLabel();
        labelDateTime = new javax.swing.JLabel();
        menuBarraPrincipal = new javax.swing.JMenuBar();
        menuCadastros = new javax.swing.JMenu();
        menuProdutosServicos = new javax.swing.JMenu();
        menuCadastrarProdutos = new javax.swing.JMenuItem();
        menuListarProdutos = new javax.swing.JMenuItem();
        menuCadastrarCentroDeCusto = new javax.swing.JMenuItem();
        menuCadastrarOpercaoes = new javax.swing.JMenuItem();
        menuMovimentacoes = new javax.swing.JMenu();
        menuNovoLancamento = new javax.swing.JMenuItem();
        menuListarLancamento = new javax.swing.JMenuItem();
        menuRelatorios = new javax.swing.JMenu();
        menuSistema = new javax.swing.JMenu();
        menuCadastrarUsuarios = new javax.swing.JMenuItem();
        menuParametrosSistema = new javax.swing.JMenuItem();

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dev - Software");

        barraAtalhos.setRollover(true);

        barraCadastrarProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/produto1.png"))); // NOI18N
        barraCadastrarProdutos.setToolTipText("Cadastrar Produtos e Serviços ");
        barraCadastrarProdutos.setFocusable(false);
        barraCadastrarProdutos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        barraCadastrarProdutos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraCadastrarProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraCadastrarProdutosActionPerformed(evt);
            }
        });
        barraAtalhos.add(barraCadastrarProdutos);

        barraListarProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/produtoList1.png"))); // NOI18N
        barraListarProdutos.setToolTipText("Listar Produtos e Serviços ");
        barraListarProdutos.setFocusable(false);
        barraListarProdutos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        barraListarProdutos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraListarProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraListarProdutosActionPerformed(evt);
            }
        });
        barraAtalhos.add(barraListarProdutos);

        barraCadastrarOpercaoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/integracao.png"))); // NOI18N
        barraCadastrarOpercaoes.setToolTipText("Cadastrar Opercaoes");
        barraCadastrarOpercaoes.setFocusable(false);
        barraCadastrarOpercaoes.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        barraCadastrarOpercaoes.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraCadastrarOpercaoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraCadastrarOpercaoesActionPerformed(evt);
            }
        });
        barraAtalhos.add(barraCadastrarOpercaoes);

        barraCadastroCentroCusto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/statistics.png"))); // NOI18N
        barraCadastroCentroCusto.setToolTipText("Cadastro de Centro de Custo");
        barraCadastroCentroCusto.setFocusable(false);
        barraCadastroCentroCusto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        barraCadastroCentroCusto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraCadastroCentroCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraCadastroCentroCustoActionPerformed(evt);
            }
        });
        barraAtalhos.add(barraCadastroCentroCusto);

        barraListLancamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/garficoelupa.png"))); // NOI18N
        barraListLancamento.setToolTipText("Listar Lançamento Financeiro");
        barraListLancamento.setFocusable(false);
        barraListLancamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        barraListLancamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraListLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraListLancamentoActionPerformed(evt);
            }
        });
        barraAtalhos.add(barraListLancamento);

        barraNovoLancamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/graficobarras.png"))); // NOI18N
        barraNovoLancamento.setToolTipText("Novo Lançamento Financeiro");
        barraNovoLancamento.setFocusable(false);
        barraNovoLancamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        barraNovoLancamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barraNovoLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barraNovoLancamentoActionPerformed(evt);
            }
        });
        barraAtalhos.add(barraNovoLancamento);

        rodape.setRollover(true);

        labelVersao.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        labelVersao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/iconesistema.png"))); // NOI18N
        labelVersao.setText("Versão: 1.0");
        rodape.add(labelVersao);

        labelNomeEmpresa.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        labelNomeEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/empresas.png"))); // NOI18N
        labelNomeEmpresa.setText("Nome da Empresa");
        rodape.add(labelNomeEmpresa);

        labelUsuario.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        labelUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/user3.png"))); // NOI18N
        labelUsuario.setText("Usuário: ");
        rodape.add(labelUsuario);

        labelIdUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelIdUsuario.setText("ID");
        rodape.add(labelIdUsuario);

        jLabel4.setText(" - ");
        rodape.add(jLabel4);

        labelNomeUsuario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelNomeUsuario.setText("Nome do Usuário");
        rodape.add(labelNomeUsuario);

        labelData.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        labelData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/calendar.png"))); // NOI18N
        labelData.setText("Data Atual:  ");
        rodape.add(labelData);

        labelDateTime.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        labelDateTime.setText("Data");
        rodape.add(labelDateTime);

        menuBarraPrincipal.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        menuCadastros.setText("Cadastros");
        menuCadastros.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N

        menuProdutosServicos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/produto1.png"))); // NOI18N
        menuProdutosServicos.setText("Produtos e serviços");
        menuProdutosServicos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        menuCadastrarProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuCadastrarProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/produto1.png"))); // NOI18N
        menuCadastrarProdutos.setText("Cadastrar");
        menuCadastrarProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastrarProdutosActionPerformed(evt);
            }
        });
        menuProdutosServicos.add(menuCadastrarProdutos);

        menuListarProdutos.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuListarProdutos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/produtoList1.png"))); // NOI18N
        menuListarProdutos.setText("Listar");
        menuListarProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuListarProdutosActionPerformed(evt);
            }
        });
        menuProdutosServicos.add(menuListarProdutos);

        menuCadastros.add(menuProdutosServicos);

        menuCadastrarCentroDeCusto.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuCadastrarCentroDeCusto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/statistics.png"))); // NOI18N
        menuCadastrarCentroDeCusto.setText("Centro de Custo");
        menuCadastrarCentroDeCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastrarCentroDeCustoActionPerformed(evt);
            }
        });
        menuCadastros.add(menuCadastrarCentroDeCusto);

        menuCadastrarOpercaoes.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuCadastrarOpercaoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/integracao.png"))); // NOI18N
        menuCadastrarOpercaoes.setText("Operações");
        menuCadastrarOpercaoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastrarOpercaoesActionPerformed(evt);
            }
        });
        menuCadastros.add(menuCadastrarOpercaoes);

        menuBarraPrincipal.add(menuCadastros);

        menuMovimentacoes.setText("Movimentações");
        menuMovimentacoes.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N

        menuNovoLancamento.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuNovoLancamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/graficobarras.png"))); // NOI18N
        menuNovoLancamento.setText("Novo Lançamento Financeiro");
        menuNovoLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNovoLancamentoActionPerformed(evt);
            }
        });
        menuMovimentacoes.add(menuNovoLancamento);

        menuListarLancamento.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuListarLancamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/garficoelupa.png"))); // NOI18N
        menuListarLancamento.setText("Listar Lançamento Financeiro");
        menuListarLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuListarLancamentoActionPerformed(evt);
            }
        });
        menuMovimentacoes.add(menuListarLancamento);

        menuBarraPrincipal.add(menuMovimentacoes);

        menuRelatorios.setText("Relatórios");
        menuRelatorios.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        menuBarraPrincipal.add(menuRelatorios);

        menuSistema.setText("Sistema");
        menuSistema.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N

        menuCadastrarUsuarios.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuCadastrarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/pastapessoaamarela.png"))); // NOI18N
        menuCadastrarUsuarios.setText("Usuários");
        menuCadastrarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCadastrarUsuariosActionPerformed(evt);
            }
        });
        menuSistema.add(menuCadastrarUsuarios);

        menuParametrosSistema.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        menuParametrosSistema.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/pastaconfiguracaoverde.png"))); // NOI18N
        menuParametrosSistema.setText("Parâmetros do Sistema");
        menuParametrosSistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuParametrosSistemaActionPerformed(evt);
            }
        });
        menuSistema.add(menuParametrosSistema);

        menuBarraPrincipal.add(menuSistema);

        setJMenuBar(menuBarraPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraAtalhos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rodape, javax.swing.GroupLayout.DEFAULT_SIZE, 1213, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barraAtalhos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 612, Short.MAX_VALUE)
                .addComponent(rodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void barraCadastrarProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraCadastrarProdutosActionPerformed
        // TODO add your handling code here:
        CadastroProdutosView cadastro = new CadastroProdutosView();
        cadastro.setVisible(true);
    }//GEN-LAST:event_barraCadastrarProdutosActionPerformed

    private void menuCadastrarProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastrarProdutosActionPerformed
        // TODO add your handling code here:
        CadastroProdutosView cadastro = new CadastroProdutosView();
        cadastro.setVisible(true);
    }//GEN-LAST:event_menuCadastrarProdutosActionPerformed

    private void menuListarProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuListarProdutosActionPerformed
        // TODO add your handling code here:
        ListProdutosView listar = new ListProdutosView();
        listar.setVisible(true);
    }//GEN-LAST:event_menuListarProdutosActionPerformed

    private void barraListarProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraListarProdutosActionPerformed
        // TODO add your handling code here:
        ListProdutosView listar = new ListProdutosView();
        listar.setVisible(true);
    }//GEN-LAST:event_barraListarProdutosActionPerformed

    private void menuCadastrarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastrarUsuariosActionPerformed
        // TODO add your handling code here:
        CadastroUsuariosView cadastro = new CadastroUsuariosView();
        cadastro.setVisible(true);
    }//GEN-LAST:event_menuCadastrarUsuariosActionPerformed

    private void menuCadastrarCentroDeCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastrarCentroDeCustoActionPerformed
        // TODO add your handling code here:
        CadastroCentroDeCusto centroDeCusto = new CadastroCentroDeCusto();
        centroDeCusto.setVisible(true);
    }//GEN-LAST:event_menuCadastrarCentroDeCustoActionPerformed

    private void menuNovoLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNovoLancamentoActionPerformed
        // TODO add your handling code here:
        LancamentoFinanceiroView lancamento = new LancamentoFinanceiroView();
        lancamento.setVisible(true);
    }//GEN-LAST:event_menuNovoLancamentoActionPerformed

    private void menuCadastrarOpercaoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCadastrarOpercaoesActionPerformed
        // TODO add your handling code here:
        CadastroOperacoes opercao = new CadastroOperacoes();
        opercao.setVisible(true);
    }//GEN-LAST:event_menuCadastrarOpercaoesActionPerformed

    private void barraNovoLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraNovoLancamentoActionPerformed
        LancamentoFinanceiroView lancamento = new LancamentoFinanceiroView();
        lancamento.setVisible(true);
    }//GEN-LAST:event_barraNovoLancamentoActionPerformed

    private void menuListarLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuListarLancamentoActionPerformed
        // TODO add your handling code here:
        ListLancamentoFinanceiroView lancamento = new ListLancamentoFinanceiroView();
        lancamento.setVisible(true);
    }//GEN-LAST:event_menuListarLancamentoActionPerformed

    private void barraCadastroCentroCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraCadastroCentroCustoActionPerformed
        // TODO add your handling code here:
        CadastroCentroDeCusto centroDeCusto = new CadastroCentroDeCusto();
        centroDeCusto.setVisible(true);
    }//GEN-LAST:event_barraCadastroCentroCustoActionPerformed

    private void barraListLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraListLancamentoActionPerformed
        // TODO add your handling code here:
        ListLancamentoFinanceiroView lancamento = new ListLancamentoFinanceiroView();
        lancamento.setVisible(true);
    }//GEN-LAST:event_barraListLancamentoActionPerformed

    private void barraCadastrarOpercaoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barraCadastrarOpercaoesActionPerformed
        // TODO add your handling code here:
        CadastroOperacoes opercao = new CadastroOperacoes();
        opercao.setVisible(true);
    }//GEN-LAST:event_barraCadastrarOpercaoesActionPerformed

    private void menuParametrosSistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuParametrosSistemaActionPerformed
        // TODO add your handling code here:
        ParametrosSistemaView parametros = new ParametrosSistemaView();
    }//GEN-LAST:event_menuParametrosSistemaActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barraAtalhos;
    private javax.swing.JButton barraCadastrarOpercaoes;
    private javax.swing.JButton barraCadastrarProdutos;
    private javax.swing.JButton barraCadastroCentroCusto;
    private javax.swing.JButton barraListLancamento;
    private javax.swing.JButton barraListarProdutos;
    private javax.swing.JButton barraNovoLancamento;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JLabel labelData;
    private javax.swing.JLabel labelDateTime;
    public static javax.swing.JLabel labelIdUsuario;
    public static javax.swing.JLabel labelNomeEmpresa;
    public static javax.swing.JLabel labelNomeUsuario;
    private javax.swing.JLabel labelUsuario;
    private javax.swing.JLabel labelVersao;
    private javax.swing.JMenuBar menuBarraPrincipal;
    private javax.swing.JMenuItem menuCadastrarCentroDeCusto;
    private javax.swing.JMenuItem menuCadastrarOpercaoes;
    private javax.swing.JMenuItem menuCadastrarProdutos;
    private javax.swing.JMenuItem menuCadastrarUsuarios;
    private javax.swing.JMenu menuCadastros;
    private javax.swing.JMenuItem menuListarLancamento;
    private javax.swing.JMenuItem menuListarProdutos;
    private javax.swing.JMenu menuMovimentacoes;
    private javax.swing.JMenuItem menuNovoLancamento;
    private javax.swing.JMenuItem menuParametrosSistema;
    private javax.swing.JMenu menuProdutosServicos;
    private javax.swing.JMenu menuRelatorios;
    private javax.swing.JMenu menuSistema;
    private javax.swing.JToolBar rodape;
    // End of variables declaration//GEN-END:variables
}
