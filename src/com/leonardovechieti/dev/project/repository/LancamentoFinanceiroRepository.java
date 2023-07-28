package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.LancamentoFinanceiro;
import com.leonardovechieti.dev.project.util.Func;
import com.leonardovechieti.dev.project.util.Message;
import com.leonardovechieti.dev.project.views.MessageView;

import java.sql.*;
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
                        for (Estoque estoque : listaEstoqueTransferencia) {
                            estoque.setIdOperacao(1);
                            estoque.setIdCentroDeCusto(idEstoqueDestino);
                        }
                        //Chama o metodo de salvar a movimentação de transferencia
                        this.novoLancamento(lancamentoTransferencia, listaEstoqueTransferencia, lancamento.getIdCentroDeCusto());
                    }
                    int id = lancamentoFinanceiro.ultimoId();
                    new MessageView("Sucesso!", "Movimentação financeira "+ (id) +" lançada com sucesso!", "success");
                } else {
                    //Caso erro cancela o lancamento
                    this.cancelarLancamento(String.valueOf(idLancamento));
                    return "ERROR";
                }
            }else{
                return "ERROR";
            }
        } else {
            return "ERROR";
        }

        int id = lancamentoFinanceiro.ultimoId();
        //new MessageView("Sucesso!", "Movimentação financeira "+ (id) +" lançada com sucesso!", "success");
        return "CREATE";
     }


    public String cancelarLancamento(String id) {
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
            Message message = new Message("Erro!", "Erro ao cancelar lancamento!" + e, "error");
            return "ERROR";
        }
    }

    public ResultSet listarAll() {
        String sql = "select id as ID, centrodecusto as CENTRODECUSTO, operacao as OPERACAO, valortotal as VALORTOTAL, data as DATA, descricao as DESCRICAO, usuario as USUARIO from lancamentofinanceiro order by id desc";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        return rs;
    }

    public LancamentoFinanceiro buscaId(String id) {
        String sql = "select * from lancamentofinanceiro where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                LancamentoFinanceiro lancamentoFinanceiro = new LancamentoFinanceiro();
                lancamentoFinanceiro.setId(rs.getInt(1));
                lancamentoFinanceiro.setIdCentroDeCusto(rs.getInt(2));
                lancamentoFinanceiro.setIdOperacao(rs.getInt(3));
                lancamentoFinanceiro.setValorTotal(Func.formataPrecoPadrao(rs.getString(4)));
                lancamentoFinanceiro.setData(rs.getString(5));
                lancamentoFinanceiro.setDescricao(rs.getString(6));
                lancamentoFinanceiro.setUsuario(rs.getInt(7));
                return lancamentoFinanceiro;
            }
        } catch (Exception e) {
            Message message = new Message("Erro!", "Erro ao buscar produto!" + e, "error");
        }
        return null;
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

}

