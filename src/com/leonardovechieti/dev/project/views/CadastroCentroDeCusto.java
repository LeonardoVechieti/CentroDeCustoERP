/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonardovechieti.dev.project.views;

import com.leonardovechieti.dev.project.model.CentroDeCusto;
import com.leonardovechieti.dev.project.repository.CentroDeCustoRepository;
import com.leonardovechieti.dev.project.util.Func;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author Leonardo
 */
public class CadastroCentroDeCusto extends javax.swing.JFrame {
    private String id = null;
    ResultSet rs = null;

    //private CentroDeCusto centroDeCusto = new CentroDeCusto();


    public CadastroCentroDeCusto() {
        initialize();
    }
    
    private void initialize() {
        initComponents();
        setIcon();
        formataBotoes();
        formataTabela();
        labelId.setVisible(false);
        try {
            pesquisar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        btnDeletar.setVisible(false);
        btnCancelar.setVisible(false);
        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
    }
    private void formataBotoes() {
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

    private void formataTabela(){

        //Seta o tamanho das linhas
        tabela.setRowHeight(25);

        //Seta o tamanho da fonte
        tabela.setFont(new Font("Arial", Font.PLAIN, 14));

        //Seta o tamanho da fonte do cabeçalho
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        //Seta a cor da linha quando selecionada
        tabela.setSelectionBackground(new Color(152, 156, 157));

        //Seta a cor da fonte quando selecionada
        tabela.setSelectionForeground(Color.black);

        //Bloqueia a edição da tabela
        tabela.setDefaultEditor(Object.class, null);
    }

    private void seleciona(String id){
        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
        CentroDeCusto centroDeCusto = centroDeCustoRepository.buscaId(id);
        labelId.setText(String.valueOf(centroDeCusto.getId()));
        txtNome.setText(centroDeCusto.getNome());
        txtDescricao.setText(centroDeCusto.getDescricao());
        checkBoxInativar.setSelected(centroDeCusto.getInativo());

        //Altera o botao de cadastrar para editar
        btnPrincipal.setText("Editar");
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/update1.png")));
    }
    private void cadastrar()  {
        if (validarCampos()) {
            CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
            CentroDeCusto centroDeCusto = new CentroDeCusto();
            centroDeCusto.setNome(txtNome.getText());
            centroDeCusto.setDescricao(txtDescricao.getText());
            centroDeCusto.setInativo(checkBoxInativar.isSelected());
            String resposta = centroDeCustoRepository.salvar(centroDeCusto);
            if (resposta.equals("SUCCESS")) {
                Integer id = centroDeCustoRepository.ultimoId();
                new MessageView("Sucesso!", "Centro de Custo cadastrado com sucesso!", "success");
                limparCampos();
                try {
                    pesquisar();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                new MessageView("Erro!", "Erro ao cadastrar Centro de Custo, verifique os campos!", "error");
            }
        }
    }
    private void alterar() {
        if (validarCampos()) {
            CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
            CentroDeCusto centroDeCusto = new CentroDeCusto();
            centroDeCusto.setId(Integer.parseInt(labelId.getText()));
            centroDeCusto.setNome(txtNome.getText());
            centroDeCusto.setDescricao(txtDescricao.getText());
            centroDeCusto.setInativo(checkBoxInativar.isSelected());
            String resposta = centroDeCustoRepository.editar(centroDeCusto);

            if (resposta.equals("SUCCESS")) {
                new MessageView("Sucesso!", "Centro de Custo alterado com sucesso!", "success");
                limparCampos();
                try {
                    pesquisar();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                new MessageView("Erro!", "Erro ao alterar Centro de Custo, verifique os campos!", "error");
            }
        }
    }

    //Buscar usuários e preencher a tabela
    private void pesquisar() throws SQLException {
        CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
        ResultSet rs = (ResultSet) centroDeCustoRepository.pesquisar(textPesquisar.getText());
        tabela.setModel(DbUtils.resultSetToTableModel(rs));

        //Seta o tamanho das colunas
        tabela.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(230);

        //Limpa os campos
        limparCampos();

        //Desabilita o botão de deletar
        //btnDeletar.setVisible(false);

        //Fecha a conexão
        centroDeCustoRepository.fecharConexao();


    }

    private void deletar(String id) {
        //Pergunta se o usuário deseja realmente excluir
        int opcao = JOptionPane.showConfirmDialog(null, "Deseja realmente excluir o Centro de Custo?", "Excluir", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            CentroDeCustoRepository centroDeCustoRepository = new CentroDeCustoRepository();
            String resposta = centroDeCustoRepository.excluir(id);
            if (resposta.equals("SUCCESS")) {
                new MessageView("Sucesso!", "Centro de custo deletado com sucesso!", "success" );
                limparCampos();
                try {
                    pesquisar();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                new MessageView("Erro!", "Erro ao deletar usuário, o centro de custo selecionado " +
                        "já está em uso em funcionalidades do sistema!", "error");
            }
        }
    }
    private void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        checkBoxInativar.setSelected(false);
        btnDeletar.setVisible(false);
        btnCancelar.setVisible(false);
        btnPrincipal.setText("Novo cadastro");
        btnPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/add1.png")));
        labelId.setText("");
        this.id = null;
    }
    private void pegaId(){
        int setar = tabela.getSelectedRow();
        this.id = tabela.getModel().getValueAt(setar,0).toString();
        if(id != null)
            btnDeletar.setVisible(true);
            btnCancelar.setVisible(true);
    }
    private boolean validarCampos() {
        if (txtNome.getText().equals("")) {
            new MessageView("Alerta!", "O campo nome é obrigatório!", "alert");
            return false;
        }
        if (txtDescricao.getText().equals("")) {
            new MessageView("Alerta!", "O campo descrição é obrigatório!", "alert");
            return false;
        }
            //Remove caracteres especiais
            txtNome.setText(Func.formatarString(txtNome.getText()));
            txtNome.setText(Func.removeEspaco(txtNome.getText()));
            txtDescricao.setText(Func.formatarString(txtDescricao.getText()));

            //UpperCase
            txtNome.setText(txtNome.getText().toUpperCase());
            txtDescricao.setText(txtDescricao.getText().toUpperCase());

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
        LabeDescricao = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        btnPesquisar = new javax.swing.JLabel();
        textPesquisar = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        btnPrincipal = new javax.swing.JButton();
        btnDeletar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de centro de custo");
        setResizable(false);

        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados Gerais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        LabeNome.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabeNome.setText("Nome:");

        txtNome.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        checkBoxInativar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        checkBoxInativar.setText("Inativar ");

        labelId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        labelId.setText("ID");

        LabeDescricao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        LabeDescricao.setText("Descrição:");

        txtDescricao.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(LabeDescricao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(LabeNome)
                        .addGap(18, 18, 18)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(checkBoxInativar)
                        .addGap(18, 18, 18)
                        .addComponent(labelId)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabeNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkBoxInativar)
                    .addComponent(labelId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(LabeDescricao)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabela.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOME"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabela.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabela.setGridColor(java.awt.SystemColor.scrollbar);
        tabela.setRequestFocusEnabled(false);
        tabela.setRowMargin(2);
        tabela.setSelectionBackground(java.awt.SystemColor.controlHighlight);
        tabela.setShowHorizontalLines(false);
        tabela.setShowVerticalLines(false);
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela);

        btnPesquisar.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/search-file.png"))); // NOI18N
        btnPesquisar.setText("Pesquisar:");
        btnPesquisar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPesquisarMouseClicked(evt);
            }
        });

        textPesquisar.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        textPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textPesquisarKeyReleased(evt);
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
                        .addComponent(btnPesquisar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 583, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Geral", jPanel2);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 13))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Movimentações", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 13))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configurações", jPanel4);

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
            alterar();
        } else {
            cadastrar();
        }
    }//GEN-LAST:event_btnPrincipalActionPerformed

    private void tabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMouseClicked
        // TODO add your handling code here:
        pegaId();
        seleciona(this.id);

    }//GEN-LAST:event_tabelaMouseClicked

    private void btnDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletarActionPerformed
        // TODO add your handling code here:
        deletar(this.id);
    }//GEN-LAST:event_btnDeletarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        limparCampos();
        try {
            pesquisar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnPesquisarMouseClicked(java.awt.event.MouseEvent evt)  {//GEN-FIRST:event_btnPesquisarMouseClicked
        try {
            pesquisar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_btnPesquisarMouseClicked

    private void textPesquisarKeyReleased(java.awt.event.KeyEvent evt)  {//GEN-FIRST:event_textPesquisarKeyReleased
        try {
            pesquisar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_textPesquisarKeyReleased

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
            java.util.logging.Logger.getLogger(CadastroCentroDeCusto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastroCentroDeCusto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastroCentroDeCusto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastroCentroDeCusto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadastroCentroDeCusto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabeDescricao;
    private javax.swing.JLabel LabeNome;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDeletar;
    private javax.swing.JLabel btnPesquisar;
    private javax.swing.JButton btnPrincipal;
    private javax.swing.JCheckBox checkBoxInativar;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelId;
    private javax.swing.JTable tabela;
    private javax.swing.JTextField textPesquisar;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
