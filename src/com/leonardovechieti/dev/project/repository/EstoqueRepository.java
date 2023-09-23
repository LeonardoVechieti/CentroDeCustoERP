package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.Produto;
import com.leonardovechieti.dev.project.model.dto.EstoqueDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public EstoqueRepository() {
        conexao = ModuloConexao.conector();
    }

    public String lancar(Estoque estoque) {
        String sql = "insert into estoque (idProduto, idLancamentoFinanceiro, idCentroDeCusto, idOperacao, quantidade, valorUnitario, valorTotal, descricao) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, estoque.getIdProduto());
            pst.setInt(2, estoque.getIdLancamentoFinanceiro());
            pst.setInt(3, estoque.getIdCentroDeCusto());
            pst.setInt(4, estoque.getIdOperacao());
            pst.setString(5, String.valueOf(estoque.getQuantidade()));
            pst.setString(6, String.valueOf(estoque.getValorUnitario()));
            pst.setString(7, String.valueOf(estoque.getValorTotal()));
            pst.setString(8, estoque.getDescricao());
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String editar(Estoque estoque) {
        String sql = "update estoque set idProduto=?, idLancamentoFinanceiro=?, idCentroDeCusto=?, quantidade=?, operacao=?, descricao=? where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String excluir(String id) {
        String sql = "delete from estoque where id=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }

    }

    public ResultSet listarAll() {
        //Lista do mais recente para o mais antigo
        String sql = "select id as ID,idOperacao as OPERACAO, quantidade as QUANTIDADE, idCentroDeCusto as CENTRO from estoque order by id desc";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Wrapper pesquisar(String nome) {
        String sql = "select id as ID, nome as NOME, descricao as DESCRICAO, inativo as INATIVO from centrodecusto where nome like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, "%" + nome + "%");
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Estoque buscaId(String id) {
        String sql = "select * from estoque where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return new Estoque(rs.getInt("id"), rs.getInt("idProduto"), rs.getInt("idLancamentoFinanceiro"), rs.getInt("idCentroDeCusto"), rs.getInt("idOperacao"), rs.getBigDecimal("quantidade"), rs.getBigDecimal("valorUnitario"), rs.getBigDecimal("valorTotal"), rs.getString("data"), rs.getString("descricao"));
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //Retorna o ultimo id cadastrado
    public int ultimoId() {
        String sql = "select max(id) from estoque";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;

    }

    public void fecharConexao() throws SQLException {
        conexao.close();
        System.out.println("Conexao fechada!");
    }

    public ArrayList<EstoqueDTO> listarPorLancamentoFinanceiro(int id) {
        ArrayList<EstoqueDTO> listaEstoqueDTO = new ArrayList<>();
        String sql = "select e.id as ID, p.id as IDPRODUTO, p.descricao as PRODUTO, o.descricao as OPERACAO, c.nome as CENTRO," +
                " e.quantidade as QUANTIDADE,  e.valorUnitario as VALOR_UNITARIO, e.valorTotal as VALOR_TOTAL from estoque e\n" +
                "join produto p\n" +
                "on e.idProduto = p.id\n" +
                "join centrodecusto c\n" +
                "on e.idCentroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on e.idOperacao = o.id\n" +
                "where e.idLancamentoFinanceiro = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                EstoqueDTO estoqueDTO = new EstoqueDTO();
                estoqueDTO.setId(rs.getInt("ID"));
                estoqueDTO.setIdProduto(rs.getInt("IDPRODUTO"));
                estoqueDTO.setProduto(rs.getString("PRODUTO"));
                estoqueDTO.setOperacao(rs.getString("OPERACAO"));
                estoqueDTO.setCentro(rs.getString("CENTRO"));
                estoqueDTO.setQuantidade(rs.getString("QUANTIDADE"));
                estoqueDTO.setValorUnitario(rs.getString("VALOR_UNITARIO"));
                estoqueDTO.setValorTotal(rs.getString("VALOR_TOTAL"));
                listaEstoqueDTO.add(estoqueDTO);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        //Printa a lista
        for (EstoqueDTO estoqueDTO : listaEstoqueDTO) {
            System.out.println(estoqueDTO.getProduto());
        }
        return listaEstoqueDTO;
    }

    public ResultSet listarPorProduto(Produto produto) {
        String sql = "select e.id as ID, o.descricao as OPERACAO, e.quantidade as QUANTIDADE, c.nome as CENTRO, DATE_FORMAT(e.data,'%d/%m/%Y') as DATA from estoque e\n" +
                "join produto p\n" +
                "on e.idProduto = p.id\n" +
                "join centrodecusto c\n" +
                "on e.idCentroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on e.idOperacao = o.id\n" +
                "where e.idProduto = ? order by e.id desc";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, produto.getId());
            rs = pst.executeQuery();

        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public Double retornaTotalEstoque(Produto produto) {
        String sql = "select sum(quantidade) from estoque where idProduto = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, produto.getId());
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0.0;
    }


    public String lancarListaEstoque(List<Estoque> listaEstoque) {
        if (listaEstoque.isEmpty()) {
            return "ERROR";
        }
        for (Estoque estoque : listaEstoque) {
            lancar(estoque);
        }
        return "SUCCESS";
    }

    public void cancelarLancamentoEstoque(String idLancamentoFinanceiro) {
        String sql = "delete from estoque where idLancamentoFinanceiro = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(idLancamentoFinanceiro));
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}