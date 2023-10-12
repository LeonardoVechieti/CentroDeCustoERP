/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.Usuario;
import com.leonardovechieti.dev.project.repository.UsuarioRepository;
import com.leonardovechieti.dev.project.util.Func;
import net.proteanit.sql.DbUtils;

import java.awt.*;
import java.sql.ResultSet;


/**
 *
 * @author Leonardo
 */
public class CadastroUsuariosView extends javax.swing.JFrame {
    private String id = null;
    ResultSet rs = null;

    public CadastroUsuariosView() {
        initialize();
    }
    
    private void initialize() {
        initComponents();
        setIcon();
        FormataBotoes();
        FormataTabela();
        labelId.setVisible(false);
        buscarUsuarios();
        btnDeletar.setVisible(false);
        btnCancelar.setVisible(false);
        UsuarioRepository usuarioRepository = new UsuarioRepository();
    }
    private void FormataBotoes() {
        btnPrincipal.setBackground(new Color(0, 0, 0, 0));
        btnPrincipal.setBorderPainted(false);
        btnPrincipal.setFocusPainted(false);
        btnPrincipal.setContentAreaFilled(false);
        btnPrincipal.setOpaque(false);
        btnPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnDeletar.setBackground(new Color(0, 0, 0, 0));
        btnDeletar.setBorderPainted(false);
        btnDeletar.setFocusPainted(false);
        btnDeletar.setContentAreaFilled(false);
        btnDeletar.setOpaque(false);
        btnDeletar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCancelar.setBackground(new Color(0, 0, 0, 0));
        btnCancelar.setBorderPainted(false);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setContentAreaFilled(false);
        btnCancelar.setOpaque(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    private void FormataTabela(){

        //Seta o tamanho das linhas
        tabelaUsuarios.setRowHeight(25);

        //Seta o tamanho da fonte
        tabelaUsuarios.setFont(new Font("Arial", Font.PLAIN, 14));

        //Seta o tamanho da fonte do cabeçalho
        tabelaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        //Seta a cor da linha quando selecionada
        tabelaUsuarios.setSelectionBackground(new Color(152, 156, 157));

        //Seta a cor da fonte quando selecionada
        tabelaUsuarios.setSelectionForeground(Color.black);

        //Bloqueia a edição da tabela
        tabelaUsuarios.setDefaultEditor(Object.class, null);
    }

    private void selecionaUsuario(String id){
        //limparCampos();
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        Usuario usuario = usuarioRepository.buscaId(Integer.parseInt(id));
        labelId.setText(String.valueOf(usuario.getId()));
        txtNome.setText(usuario.getNome());
        txtLogin.setText(usuario.getLogin());
        txtSenha.setText(usuario.getSenha());
        checkBoxInativar.setSelected(usuario.getInativo());
        setRegras(usuario.getPerfil(), usuario.getRegras());
        //Altera o botao de cadastrar para editar
        btnPrincipal.setText("Editar");

    }
    private void setRegras(String perfil, String regras){
        if(perfil.equals("ADMIN")){
            checkBoxAdmin.setSelected(true);
        } else {
            checkBoxAdmin.setSelected(false);
        }
        if(regras.contains("CAD_PRODUTO")){
            boxCadProduto.setSelected(true);
        } else {
            boxCadProduto.setSelected(false);
        }
        if(regras.contains("CAD_CENTRO_DE_CUSTO")){
            boxCadCentro.setSelected(true);
        } else {
            boxCadCentro.setSelected(false);
        }
        if(regras.contains("CAD_OPERACAO")){
            boxCadOperacao.setSelected(true);
        } else {
            boxCadOperacao.setSelected(false);
        }
        if(regras.contains("LIS_PRODUTO")){
            boxLisProduto.setSelected(true);
        } else {
            boxLisProduto.setSelected(false);
        }
        if(regras.contains("CAD_EMPRESA")){
            boxCadEmpresa.setSelected(true);
        } else {
            boxCadEmpresa.setSelected(false);
        }
        if(regras.contains("CAD_LANCAMENTO_FINANCEIRO")){
            boxNovoLancamento.setSelected(true);
        } else {
            boxNovoLancamento.setSelected(false);
        }
        if(regras.contains("LIS_LANCAMENTO_FINANCEIRO")){
            boxLisLancamento.setSelected(true);
        } else {
            boxLisLancamento.setSelected(false);
        }
        if(regras.contains("REL_PRODUTO_ESTOQUE")){
            boxRelEstoque.setSelected(true);
        } else {
            boxRelEstoque.setSelected(false);
        }
        if(regras.contains("REL_LANCAMENTO_FINANCEIRO")){
            boxRelLancamento.setSelected(true);
        } else {
            boxRelLancamento.setSelected(false);
        }

    }

    private void cadastrarUsuario() {
        // TODO add your handling code here:
        if (validarCampos()) {
            UsuarioRepository usuarioRepository = new UsuarioRepository();
            String resposta = usuarioRepository.cadastrar(
                    txtNome.getText(),
                    txtLogin.getText(),
                    txtSenha.getText(),
                    checkBoxInativar.isSelected(),
                    this.perfil(),
                    this.regras()
            );
            if (resposta.equals("CREATE")) {
                Integer id = usuarioRepository.ultimoId();
                new MessageView("Sucesso!", "Usuário cadastrado com sucesso, ID: " + id, "success" );
                limparCampos();
                } else {
                 new MessageView("Erro!", "Erro ao cadastrar usuário, verifique os campos!", "error");
                }
            }
        }

    private String perfil() {
        if(checkBoxAdmin.isSelected()){
            return "ADMIN";
    }
        return "USER";
    }

    private String regras() {
        String regras = "";
        if(boxCadProduto.isSelected()){
            regras += "CAD_PRODUTO,";
        }
        if(boxCadCentro.isSelected()){
            regras += "CAD_CENTRO_DE_CUSTO,";
        }
        if(boxCadOperacao.isSelected()){
            regras += "CAD_OPERACAO,";
        }
        if(boxLisProduto.isSelected()){
            regras += "LIS_PRODUTO,";
        }
        if(boxCadEmpresa.isSelected()){
            regras += "CAD_EMPRESA,";
        }
        if(boxNovoLancamento.isSelected()){
            regras += "CAD_LANCAMENTO_FINANCEIRO,";
        }
        if(boxLisLancamento.isSelected()){
            regras += "LIS_LANCAMENTO_FINANCEIRO,";
        }
        if(boxRelEstoque.isSelected()){
            regras += "REL_PRODUTO_ESTOQUE,";
        }
        if(boxRelLancamento.isSelected()){
            regras += "REL_LANCAMENTO_FINANCEIRO,";
        }
        return regras;

    }

    private void alterarUsuario() {
        // TODO add your handling code here:
        if (validarCampos()) {
            UsuarioRepository usuarioRepository = new UsuarioRepository();
            String resposta = usuarioRepository.alterar(
                    Integer.parseInt(labelId.getText()),
                    txtNome.getText(),
                    txtLogin.getText(),
                    txtSenha.getText(),
                    checkBoxInativar.isSelected(),
                    this.perfil(),
                    this.regras()
            );
            if (resposta.equals("SUCCESS")) {
                new MessageView("Sucesso!", "Usuário alterado com sucesso!", "success" );
            } else {
                new MessageView("Erro!", "Erro ao alterar usuário, verifique os campos!", "error");
            }
        }
    }
    //Buscar usuários e preencher a tabela
    private void buscarUsuarios() {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        rs = (ResultSet) usuarioRepository.buscaUsuarios();
        tabelaUsuarios.setModel(DbUtils.resultSetToTableModel(rs));

        //Seta o tamanho das colunas
        tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(230);
        tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(100);

        //Reseta o id do produto selecionado
        id=null;

        //Desabilita o botão de deletar
        btnDeletar.setVisible(false);

        //Fecha a conexão
        usuarioRepository.fecharConexao();

    }

    private void Deletar(){
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        String resposta = usuarioRepository.excluir(Integer.parseInt(labelId.getText()));
        if (resposta.equals("SUCCESS")) {
            new MessageView("Sucesso!", "Usuário deletado com sucesso!", "success" );
            limparCampos();
            buscarUsuarios();
        } else {
            new MessageView("Erro!", "Erro ao deletar, usuário já está sendo utilizado!", "error");
        }
    }
    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        checkBoxInativar.setSelected(false);
        checkBoxAdmin.setSelected(false);
        btnDeletar.setVisible(false);
        btnCancelar.setVisible(false);
        btnPrincipal.setText("Novo cadastro");
        //labelId.setText("");
        boxCadProduto.setSelected(false);
        boxCadCentro.setSelected(false);
        boxCadOperacao.setSelected(false);
        boxLisProduto.setSelected(false);
        boxCadEmpresa.setSelected(false);
        boxNovoLancamento.setSelected(false);
        boxLisLancamento.setSelected(false);
        boxRelEstoque.setSelected(false);
        boxRelLancamento.setSelected(false);
        buscarUsuarios();
    }
    private void PegaId(){
        int setar = tabelaUsuarios.getSelectedRow();
        this.id = tabelaUsuarios.getModel().getValueAt(setar,0).toString();
        if(id != null)
            btnDeletar.setVisible(true);
            btnCancelar.setVisible(true);
    }
    private boolean validarCampos() {
        if (txtNome.getText().equals("")) {
            new MessageView("Erro!", "O campo nome é obrigatório!", "error");
            return false;
        }
        if (txtLogin.getText().equals("")) {
            new MessageView("Erro!", "O campo login é obrigatório!", "error");
            return false;
        }
        if (txtSenha.getText().equals("")) {
            new MessageView("Erro!", "O campo senha é obrigatório!", "error");
            return false;
        }
            //Remove caracteres especiais
            txtNome.setText(Func.formatarString(txtNome.getText()));
            txtLogin.setText(Func.formatarString(txtLogin.getText()));
            //Senha aceita caracteres especiais

            //UpperCase
            txtNome.setText(txtNome.getText().toUpperCase());
            txtLogin.setText(txtLogin.getText().toUpperCase());

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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        LabeNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        checkBoxInativar = new javax.swing.JCheckBox();
        labelId = new javax.swing.JLabel();
        LabeNome1 = new javax.swing.JLabel();
        LabeNome2 = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        checkBoxAdmin = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaUsuarios = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        boxCadProduto = new javax.swing.JCheckBox();
        boxCadCentro = new javax.swing.JCheckBox();
        boxCadOperacao = new javax.swing.JCheckBox();
        boxLisProduto = new javax.swing.JCheckBox();
        boxCadEmpresa = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        boxNovoLancamento = new javax.swing.JCheckBox();
        boxLisLancamento = new javax.swing.JCheckBox();
        boxPermiteCancelamento = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        boxRelLancamento = new javax.swing.JCheckBox();
        boxRelEstoque = new javax.swing.JCheckBox();
        btnPrincipal = new javax.swing.JButton();
        btnDeletar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de usuários e permissões");
        setResizable(false);

        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados Gerais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        LabeNome.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabeNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        checkBoxInativar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxInativar.setText("Inativar cadastro");

        labelId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelId.setText("ID");

        LabeNome1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabeNome1.setText("Login:");

        LabeNome2.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabeNome2.setText("Senha:");

        txtLogin.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        txtSenha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSenhaActionPerformed(evt);
            }
        });
        txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSenhaKeyPressed(evt);
            }
        });

        checkBoxAdmin.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxAdmin.setText("Administrador");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(LabeNome)
                                .addGap(18, 18, 18)
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(LabeNome1)
                                .addGap(18, 18, 18)
                                .addComponent(txtLogin)
                                .addGap(18, 18, 18)
                                .addComponent(LabeNome2)
                                .addGap(18, 18, 18)
                                .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(58, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(checkBoxAdmin)
                        .addGap(18, 18, 18)
                        .addComponent(checkBoxInativar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelId)
                        .addGap(99, 99, 99))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabeNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabeNome1)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabeNome2)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkBoxAdmin)
                    .addComponent(checkBoxInativar)
                    .addComponent(labelId))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabelaUsuarios.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tabelaUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOME", "LOGIN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaUsuarios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabelaUsuarios.setGridColor(java.awt.SystemColor.scrollbar);
        tabelaUsuarios.setRequestFocusEnabled(false);
        tabelaUsuarios.setRowMargin(2);
        tabelaUsuarios.setSelectionBackground(java.awt.SystemColor.controlHighlight);
        tabelaUsuarios.setShowHorizontalLines(false);
        tabelaUsuarios.setShowVerticalLines(false);
        tabelaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaUsuarios);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Geral", jPanel2);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 13))); // NOI18N

        boxCadProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxCadProduto.setText("Produtos e Serviços");
        boxCadProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxCadProdutoActionPerformed(evt);
            }
        });

        boxCadCentro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxCadCentro.setText("Centro de Custo");
        boxCadCentro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxCadCentroActionPerformed(evt);
            }
        });

        boxCadOperacao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxCadOperacao.setText("Operações");
        boxCadOperacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxCadOperacaoActionPerformed(evt);
            }
        });

        boxLisProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxLisProduto.setText("Listar Produtos");
        boxLisProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxLisProdutoActionPerformed(evt);
            }
        });

        boxCadEmpresa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxCadEmpresa.setText("Empresas");
        boxCadEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxCadEmpresaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(boxCadOperacao)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxCadProduto)
                            .addComponent(boxCadCentro))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(boxCadEmpresa)
                            .addComponent(boxLisProduto))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxCadProduto)
                    .addComponent(boxLisProduto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(boxCadCentro)
                    .addComponent(boxCadEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boxCadOperacao))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movimentações", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 13))); // NOI18N

        boxNovoLancamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxNovoLancamento.setText("Novo Lançamento");
        boxNovoLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxNovoLancamentoActionPerformed(evt);
            }
        });

        boxLisLancamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxLisLancamento.setText("Listar Lançamentos");

        boxPermiteCancelamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxPermiteCancelamento.setText("Permite Cancelamentos");
        boxPermiteCancelamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //todo: implementar cancelamento
               // boxPermiteCancelamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(boxLisLancamento)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(boxNovoLancamento)
                        .addGap(38, 38, 38)
                        .addComponent(boxPermiteCancelamento)))
                .addContainerGap(192, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxNovoLancamento)
                    .addComponent(boxPermiteCancelamento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boxLisLancamento))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Relatórios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 13))); // NOI18N

        boxRelLancamento.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxRelLancamento.setText("Lançamento Financeiro");
        boxRelLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxRelLancamentoActionPerformed(evt);
            }
        });

        boxRelEstoque.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        boxRelEstoque.setText("Estoque Produtos");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(boxRelEstoque)
                    .addComponent(boxRelLancamento))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(boxRelLancamento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boxRelEstoque))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Permissões", jPanel4);

        btnPrincipal.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png"))); // NOI18N
        btnPrincipal.setText("Cadastrar");
        btnPrincipal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrincipalActionPerformed(evt);
            }
        });

        btnDeletar.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/delete1.png"))); // NOI18N
        btnDeletar.setText("Deletar");
        btnDeletar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/inativar1.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeletar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        setSize(new java.awt.Dimension(614, 463));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrincipalActionPerformed
        // TODO add your handling code here:
        //se o id for diferente de nulo
        if (id != null) {
            alterarUsuario();
        } else {
            cadastrarUsuario();
        }
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void txtSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSenhaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSenhaActionPerformed

    private void txtSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSenhaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSenhaKeyPressed

    private void tabelaUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaUsuariosMouseClicked
        // TODO add your handling code here:
        PegaId();
        selecionaUsuario(this.id);

    }//GEN-LAST:event_tabelaUsuariosMouseClicked

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        // TODO add your handling code here:
        Deletar();
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        limparCampos();
        buscarUsuarios();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void boxCadProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxCadProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxCadProdutoActionPerformed

    private void boxCadCentroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxCadCentroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxCadCentroActionPerformed

    private void boxCadOperacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxCadOperacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxCadOperacaoActionPerformed

    private void boxNovoLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxNovoLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxNovoLancamentoActionPerformed

    private void boxRelLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxRelLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxRelLancamentoActionPerformed

    private void boxLisProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxLisProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxLisProdutoActionPerformed

    private void boxCadEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxCadEmpresaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxCadEmpresaActionPerformed

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
            java.util.logging.Logger.getLogger(CadastroUsuariosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroUsuariosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroUsuariosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroUsuariosView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new CadastroUsuariosView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabeNome;
    private javax.swing.JLabel LabeNome1;
    private javax.swing.JLabel LabeNome2;
    private javax.swing.JCheckBox boxCadCentro;
    private javax.swing.JCheckBox boxCadEmpresa;
    private javax.swing.JCheckBox boxCadOperacao;
    private javax.swing.JCheckBox boxCadProduto;
    private javax.swing.JCheckBox boxLisLancamento;
    private javax.swing.JCheckBox boxLisProduto;
    private javax.swing.JCheckBox boxNovoLancamento;
    private javax.swing.JCheckBox boxPermiteCancelamento;
    private javax.swing.JCheckBox boxRelEstoque;
    private javax.swing.JCheckBox boxRelLancamento;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JCheckBox checkBoxAdmin;
    private javax.swing.JCheckBox checkBoxInativar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelId;
    private javax.swing.JTable tabelaUsuarios;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNome;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables
}
