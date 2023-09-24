package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.LancamentoFinanceiro;
import com.leonardovechieti.dev.project.model.dto.EstoqueDTO;
import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.util.Func;
import com.leonardovechieti.dev.project.util.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LancamentoFinanceiroRepository {
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public LancamentoFinanceiroRepository() {
        conexao = ModuloConexao.conector();
    }

    public String save(LancamentoFinanceiro lancamento) {
        String sql = "insert into lancamentofinanceiro (centroDeCusto, operacao, valorTotal, data, descricao, usuario, idLancamentoAnexo) values (?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, lancamento.getIdCentroDeCusto());
            pst.setInt(2, lancamento.getIdOperacao());
            pst.setString(3, Func.formataPrecoBanco(lancamento.getValorTotal()));
            pst.setDate(4, new Date(System.currentTimeMillis()));
            pst.setString(5, lancamento.getDescricao());
            pst.setInt(6, lancamento.getUsuario());
            //Verifica se existe valor em idlancamentoanexado, se não, seta como null, se sim, seta o valor
            if (lancamento.getIdLancamentoAnexo() == 0) {
                pst.setNull(7, Types.INTEGER);
            } else {
                pst.setInt(7, lancamento.getIdLancamentoAnexo());
            }
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }

    public String novoLancamento(LancamentoFinanceiro lancamento, List<Estoque> listaEstoque, int idEstoqueDestino) {
        //Primeira etapa, salva o lancamento
        LancamentoFinanceiroRepository lancamentoFinanceiro = new LancamentoFinanceiroRepository();
        //Copia os dados do lancamento
        LancamentoFinanceiro lancamentoTransferencia = lancamento;
        List<Estoque> listaEstoqueTransferencia = listaEstoque;
        int idLancamento = 0;
        String resposta = this.save(lancamento);

        //Segunda etapa, lanca os itens do estoque
        //Verifica se a resposta é sucesso
        if (resposta.equals("SUCCESS")) {
            idLancamento = this.ultimoId();
            //se a lista não for vazia, salva os itens do estoque
            if (!listaEstoque.isEmpty()) {
                //Resgata o tipo da operação da movimentação
                OperacaoRepository operacao = new OperacaoRepository();
                String tipoOperacaoLancamento = operacao.buscaTipoId(String.valueOf(lancamento.getIdOperacao()));
                //Percorre a lista atualizando o id do lancamento
                //OperacaoRepository operacao = new OperacaoRepository();
                for (Estoque estoque : listaEstoque) {
                    estoque.setIdLancamentoFinanceiro(idLancamento);
                    //Verifica se o tipo da operação
                    String tipoOperacao = operacao.buscaTipoId(String.valueOf(estoque.getIdOperacao()));
                    //Switch para verificar se a operação é entrada ou saida ou transferencia
                    switch (tipoOperacao) {
                        case "ENTRADA":
                            estoque.setQuantidade(estoque.getQuantidade());
                            //Se caso negativo, inverte o sinal remove o sinal
                            if (estoque.getQuantidade().doubleValue() < 0) {
                                estoque.setQuantidade(estoque.getQuantidade().doubleValue() * -1);
                            }
                            break;
                        case "SAIDA":
                            estoque.setQuantidade(estoque.getQuantidade().doubleValue() * -1);
                            break;
                        case "TRANSFERENCIA":
                            estoque.setQuantidade(estoque.getQuantidade().doubleValue() * -1);
                            break;
                        case "AJUSTE":
                            //Todo: Verificar se o ajuste é positivo ou negativo
                            break;
                    }
                } //Fim do for
                //Chama o metodo de salvar estoque
                EstoqueRepository estoqueRepository = new EstoqueRepository();
                String retorno = estoqueRepository.lancarListaEstoque(listaEstoque);
                if (retorno.equals("SUCCESS")) {
                    //Começa o lancamento da entrada caso seja uma transferencia
                    if (tipoOperacaoLancamento.equals("TRANSFERENCIA")) {
                        lancamentoTransferencia.setIdCentroDeCusto(idEstoqueDestino);
                        lancamentoTransferencia.setIdOperacao(1); //Todo: O id está fixo, porem isso não é o mais correto, verificar outra alternativa
                        lancamentoTransferencia.setIdlancamentoanexo(idLancamento);
                        //Percorre a lista atualizando o id da operacao
                        if (!listaEstoqueTransferencia.isEmpty()) {
                            for (Estoque estoque : listaEstoqueTransferencia) {
                                estoque.setIdOperacao(1);
                                estoque.setIdCentroDeCusto(idEstoqueDestino);
                            }
                            //Chama o metodo de salvar a movimentação de transferencia
                            this.novoLancamento(lancamentoTransferencia, listaEstoqueTransferencia, lancamento.getIdCentroDeCusto());
                        }
                    }
                    //int id = lancamentoFinanceiro.ultimoId();
                    //new MessageView("Sucesso!", "Movimentação financeira "+ (id) +" lançada com sucesso!", "success");
                    return "CREATE";
                } else {
                    //Caso erro cancela o lancamento
                    this.excluirLancamento(String.valueOf(idLancamento));
                    return "ERROR";
                }
            } else {
                //Caso a lista esteja vazia, apenas retorna sucesso
                //int id = lancamentoFinanceiro.ultimoId();
                //new MessageView("Sucesso!", "Movimentação financeira "+ (id) +" lançada com sucesso!", "success");
                return "CREATE";
            }
        }
        return "ERROR";
    }

    public String excluirLancamento(String id) {
        //Cancela o lancamento do estoque
        EstoqueRepository estoqueRepository = new EstoqueRepository();
        estoqueRepository.cancelarLancamentoEstoque(id);
        //Cancela o lancamento financeiro
        String sql = "delete from lancamentofinanceiro where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            new Message("Erro!", "Erro ao cancelar lancamento!" + e, "error");
            return "ERROR";
        }
    }

    public void cancelarLancamento(int id) {
        //Cancela o lancamento do estoque
        EstoqueRepository estoqueRepository = new EstoqueRepository();
        estoqueRepository.cancelarLancamentoEstoque(String.valueOf(id));
        //Cancela o lancamento financeiro
        String sql = "update lancamentofinanceiro set cancelado = 1 where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, String.valueOf(id));
            pst.executeUpdate();
        } catch (Exception e) {
            new Message("Erro!", "Erro ao cancelar lancamento!" + e, "error");
        }
    }

    public List<LancamentoFinanceiroDTO> listarAll(Boolean cancelado) {

        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.cancelado = ?\n" +
                "order by l.id desc\n" +
                "limit 100";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setBoolean(1, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarAll(String dataInicial, String dataFinal, Boolean cancelado) {

        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.data between ? and ?\n" +
                "and l.cancelado = ?\n" +
                "order by l.id desc\n" +
                "limit 100";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, dataInicial);
            pst.setString(2, dataFinal);
            pst.setBoolean(3, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public LancamentoFinanceiroDTO buscaLacamento(int id) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, String.valueOf(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));

                //Busca os itens do estoque
                EstoqueRepository estoqueRepository = new EstoqueRepository();
                List<EstoqueDTO> listaEstoqueDTO = estoqueRepository.listarPorLancamentoFinanceiro(id);
                lancamentoFinanceiroDTO.addArrayEstoque((ArrayList<EstoqueDTO>) listaEstoqueDTO);
                return lancamentoFinanceiroDTO;
            }
        } catch (Exception e) {
            new Message("Erro!", "Erro ao buscar produto!" + e, "error");
        }
        return null;
    }

    public ResultSet buscaId(String id) {
        String sql = "select * from lancamentofinanceiro where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
        } catch (Exception e) {
            Message message = new Message("Erro!", "Erro ao buscar produto!" + e, "error");
        }
        return rs;
    }

    //Retorna o ultimo id cadastrado
    public int ultimoId() {
        String sql = "select max(id) from lancamentofinanceiro";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            Message message = new Message("Erro!", "Erro ao buscar produto!" + e, "error");
        }
        return 0;
    }

    public void fecharConexao() throws SQLException {
         conexao.close();
        System.out.println("Conexao fechada!");
    }
    public List<LancamentoFinanceiroDTO> listarPorOperacao(int idOperacao, Boolean cancelado) {
         String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                 " l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.operacao = ? " +
                "and l.cancelado = ?\n" + "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idOperacao);
            pst.setBoolean(2, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorOperacao(int idOperacao, String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.operacao = ?" +
                " and l.data between ? and ? " +
                "and l.cancelado = ?\n" +
                "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idOperacao);
            pst.setString(2, dataInicial);
            pst.setString(3, dataFinal);
            pst.setBoolean(4, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCusto(int idCentroDeCusto, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.centrodecusto = ? " +
                "and l.cancelado = ?\n" +
                "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setBoolean(2, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCusto(int idCentroDeCusto, String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.centrodecusto = ? and l.data between ? and ? " +
                "and l.cancelado = ?\n" +
                "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setString(2, dataInicial);
            pst.setString(3, dataFinal);
            pst.setBoolean(4, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCustoOperacao(int idCentroDeCusto, int idOperacao, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.centrodecusto = ? and l.operacao = ? " +
                "and l.cancelado = ?\n" +
                "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setInt(2, idOperacao);
            pst.setBoolean(3, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCustoOperacao(int idCentroDeCusto, int idOperacao, String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA, l.valorTotal as VALOR, l.descricao as DESCRICAO, l.cancelado as CANCELADO from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.centrodecusto = ? and l.operacao = ? and l.data between ? and ? " +
                "and l.cancelado = ?\n" +
                "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setInt(2, idOperacao);
            pst.setString(3, dataInicial);
            pst.setString(4, dataFinal);
            pst.setBoolean(5, cancelado);
            rs = pst.executeQuery();
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setDescricao(rs.getString(7));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(8));
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
}

