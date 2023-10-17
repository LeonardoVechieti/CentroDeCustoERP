package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.model.dto.ProdutoDTO;
import com.leonardovechieti.dev.project.model.dto.ReportDTO;
import com.leonardovechieti.dev.project.util.Func;
import com.leonardovechieti.dev.project.util.Message;
import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.views.MessageView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public ProdutoRepository() {
        conexao = ModuloConexao.conector();
    }

    public String salvar(String descricao, String preco, String unidade, Boolean inativo, Boolean servico, Boolean estoque, Boolean producao, int usuario) {
        String sql = "insert into produto (descricao, preco, unidade, inativo, servico, estoque, producao, dataModificacao, usuario) values (?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            pst.setString(2, Func.formataPrecoBanco(preco));
            pst.setString(3, unidade);
            pst.setBoolean(4, inativo);
            pst.setBoolean(5, servico);
            pst.setBoolean(6, estoque);
            pst.setBoolean(7, producao);
            pst.setDate(8, new Date(System.currentTimeMillis()));
            pst.setInt(9, usuario);
            pst.executeUpdate();
            return "CREATE";
        } catch (Exception e) {
            Message message = new Message("Erro!", "Erro ao cadastrar produto!" + e, "error");
            return "ERROR";
        }
    }

    public String editar(String id, String descricao, String preco, String unidade, Boolean inativo, Boolean servico, Boolean estoque, Boolean producao, int usuario) {
        String sql = "update produto set descricao=?, preco=?, unidade=?, inativo=?, servico=?, estoque=?, producao=?, dataModificacao=?, usuario=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            pst.setString(2, Func.formataPrecoBanco(preco));
            pst.setString(3, unidade);
            pst.setBoolean(4, inativo);
            pst.setBoolean(5, servico);
            pst.setBoolean(6, estoque);
            pst.setBoolean(7, producao);
            pst.setDate(8, new Date(System.currentTimeMillis()));
            pst.setInt(9, usuario);
            pst.setString(10, id);

            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
        return "SUCCESS";
    }

    public String excluir(String id) {
        String sql = "delete from produto where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.executeUpdate();
            return "DELETE";
        } catch (Exception e) {
            MessageView messageView = new MessageView("Erro!", "Produto já possui movimentação, não é possível excluir, mas pode ser inativado a qualquer momento!", "error");
            return "ERROR";
        }
    }

    public ResultSet listarAll() {
        String sql = "select * from produto";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public List<Produto> pesquisarTodos(String nome) {
        String sql = "select id as ID, descricao as PRODUTO, unidade as UNIDADE, preco as PREÇO from produto where descricao like ?";;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        //Percorre o ResultSet e adiciona os dados em um ArrayList
        java.util.List<Produto> listaProdutos = new java.util.ArrayList<Produto>();

        try {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt(1));
                produto.setDescricao(rs.getString(2));
                produto.setUnidade(rs.getString(3));
                produto.setPreco(Func.formataPrecoPadrao(rs.getString(4)));
                listaProdutos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProdutos;
    }
    public List<Produto> pesquisar(String nome) {
        String sql = "select id as ID, descricao as PRODUTO, unidade as UNIDADE, preco as PREÇO from produto where inativo = false and descricao like ?";;
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        //Percorre o ResultSet e adiciona os dados em um ArrayList
        java.util.List<Produto> listaProdutos = new java.util.ArrayList<Produto>();

        try {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt(1));
                produto.setDescricao(rs.getString(2));
                produto.setUnidade(rs.getString(3));
                produto.setPreco(Func.formataPrecoPadrao(rs.getString(4)));
                listaProdutos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaProdutos;
    }

    public Produto buscaId(String id) {
        String sql = "select * from produto where id= ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt(1));
                produto.setDescricao(rs.getString(2));

                //Formatando o preco do banco para o padrao
                produto.setPreco(Func.formataPrecoPadrao(rs.getString(3)));

                produto.setUnidade(rs.getString(4));
                produto.setInativo(rs.getBoolean(5));
                produto.setServico(rs.getBoolean(6));
                produto.setEstoque(rs.getBoolean(7));
                produto.setProducao(rs.getBoolean(8));
                produto.setDataCriacao(rs.getString(9));
                produto.setDataModificacao(rs.getString(10));
                produto.setUsuario(rs.getInt(11));
                return produto;
            }
        } catch (Exception e) {
            new Message("Erro!", "Erro ao buscar produto!" + e, "error");
        }
        return null;
    }

    //Retorna o ultimo id cadastrado
    public int ultimoId() {
        String sql = "select max(id) from produto";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                fecharConexao();
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public Boolean estoqueHabilitado(int idProduto){
        String sql = "select estoque from produto where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idProduto);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public void fecharConexao() throws SQLException {
         conexao.close();
        System.out.println("Conexao fechada!");
    }

//                dataInicial,
//                dataFinal,
//                boxEstoque.isSelected(),
//                boxProducao.isSelected(),
//                boxServicos.isSelected(),
//                comboBoxStatus.getSelectedItem().toString(),
//                usuario.getId()
// produtoDTO.setPreco(Func.formataPrecoPadrao(rs.getString(3)));
//                produtoDTO.setUnidade(rs.getString(4));
//                produtoDTO.setInativo(rs.getBoolean(5));
//                produtoDTO.setServico(rs.getBoolean(6));
//                produtoDTO.setEstoque(rs.getBoolean(7));
//                produtoDTO.setProducao(rs.getBoolean(8));
//                produtoDTO.setDataCriacao(rs.getString(9));
//                produtoDTO.setDataModificacao(rs.getString(10));
//                produtoDTO.setUsuario(rs.getInt(11));
//                produtoDTO.setNomeUsuario(rs.getString(12));
    public List<ProdutoDTO> buscaProduto(
            String dataInicial,
            String dataFinal,
            String unidade,
            boolean estoque,
            boolean producao,
            boolean servicos,
            boolean status,
            int idUsuario,
            boolean orderAlfabetica
    ) {
        String sql = "select p.id as ID, p.descricao as DESCRICAO, p.preco as PRECO, p.unidade as UNIDADE, p.inativo as INATIVO," +
                " p.servico as SERVICO, p.estoque as ESTOQUE, p.producao as PRODUCAO, DATE_FORMAT(p.dataCriacao,'%d/%m/%Y') as DATACRIA," +
                " u.id as IDUSUARIO, u.nome as USUARIO, DATE_FORMAT(p.dataModificacao,'%d/%m/%Y') as DATAMODIFICA " +
                " from produto p\n" +
                " inner join usuario u on u.id = p.usuario\n";

        if(estoque) {
            sql = sql + "where p.estoque = true\n";
        }
        if(producao) {
            sql = sql + "where p.producao = true\n";
        }
        if(servicos) {
            sql = sql + "where p.servico = true\n";
        }
        if(unidade != null) {
            sql = sql + "where p.unidade = ?\n";
        }
        if(status) {
            sql = sql + "where p.inativo = false\n";
        }
        if(dataInicial != null && dataFinal != null) {
            sql = sql + "and p.dataModificacao between ? and ?\n";
        }
        if(idUsuario != 0) {
            sql = sql + "and p.usuario = ?\n";
        }
        if(orderAlfabetica) {
            sql = sql + "order by p.descricao asc";
        } else {
            sql = sql + "order by p.dataModificacao asc";
        }

        List<ProdutoDTO> lista = new ArrayList<>();
        ReportDTO reportDTO = new ReportDTO();
        try {
            int i = 1;
            pst = conexao.prepareStatement(sql);
            if(unidade != null) {
                pst.setString(i, unidade);
                i++;
            }
            if(dataInicial != null && dataFinal != null) {
                pst.setString(i, dataInicial);
                i++;
                pst.setString(i, dataFinal);
                i++;
            }
            if(idUsuario != 0) {
                pst.setInt(i, idUsuario);
                i++;
            }

            rs = pst.executeQuery();
            while (rs.next()) {
                ProdutoDTO produtoDTO = new ProdutoDTO();
                produtoDTO.setId(rs.getInt(1));
                produtoDTO.setDescricao(rs.getString(2));
                produtoDTO.setPreco(Func.formataPrecoPadrao(rs.getString(3)));
                produtoDTO.setUnidade(rs.getString(4));
                produtoDTO.setInativo(rs.getBoolean(5));
                produtoDTO.setServico(rs.getBoolean(6));
                produtoDTO.setEstoque(rs.getBoolean(7));
                produtoDTO.setProducao(rs.getBoolean(8));
                produtoDTO.setDataCriacao(rs.getString(9));
                produtoDTO.setUsuario(rs.getInt(10));
                produtoDTO.setNomeUsuario(rs.getString(11));
                produtoDTO.setDataModificacao(rs.getString(12));
                produtoDTO.setReport(reportDTO);
                //Seta o saldo do produto e valor total x quantidade
                Produto produto = new Produto(rs.getInt(1));
                produtoDTO.setSaldoEstoque(new EstoqueRepository().retornaTotalEstoque(produto).toString());
                produtoDTO.setValorSaldoTotal(Func.formataPrecoPadrao(String.valueOf(new
                        EstoqueRepository().retornaTotalEstoque(produto) * (rs.getString(3) == null ? 0 : Double.parseDouble(rs.getString(3)
                )))));

                lista.add(produtoDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
}
