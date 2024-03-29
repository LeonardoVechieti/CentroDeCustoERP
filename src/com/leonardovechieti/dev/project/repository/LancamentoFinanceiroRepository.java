package com.leonardovechieti.dev.project.repository;

import com.leonardovechieti.dev.project.dao.ModuloConexao;
import com.leonardovechieti.dev.project.model.Estoque;
import com.leonardovechieti.dev.project.model.LancamentoFinanceiro;
import com.leonardovechieti.dev.project.model.dto.EstoqueDTO;
import com.leonardovechieti.dev.project.model.dto.LancamentoFinanceiroDTO;
import com.leonardovechieti.dev.project.model.dto.ReportDTO;
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
        String sql = "insert into lancamentofinanceiro (centroDeCusto, operacao, valorTotal, data, descricao, usuario, idLancamentoAnexo, desconto, descontoTipo) values (?,?,?,?,?,?,?,?,?)";
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
            pst.setString(8, Func.formataPrecoBanco(lancamento.getDesconto()));
            pst.setString(9, lancamento.getDescontoTipo());
            pst.executeUpdate();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            return "ERROR";
        }
    }
    //TODO: Refatorar esse metodo para que ele seja mais generico, e possa ser usado em outros lugares
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
                            //Atualiza o id do anexo no lancamento de transferencia
                            int idAnexo = this.ultimoId();
                            this.atualizaIdAnexo(idLancamento, idAnexo);
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

    private void atualizaIdAnexo(int id, int idAnexo){
        String sql = "update lancamentofinanceiro set idLancamentoAnexo = ? where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idAnexo);
            pst.setInt(2, id);
            pst.executeUpdate();
        } catch (Exception e) {
            new Message("Erro!", "Erro ao atualizar id do anexo!" + e, "error");
        }
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
        //Verifica se o lacamento possui anexo
        int idAnexo = buscaIdLancamentoAnexo(id);
        if (idAnexo != 0) {
            cancelar(idAnexo);
        }
        cancelar(id);
    }
    private void cancelar(int id) {
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
     private int buscaIdLancamentoAnexo(int id) {
        String sql = "select idLancamentoAnexo from lancamentofinanceiro where id = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, String.valueOf(id));
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            new Message("Erro!", "Erro ao buscar lancamento anexo!" + e, "error");
        }
        return 0;
    }

    public List<LancamentoFinanceiroDTO> listarAll(Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA " +
                " from lancamentofinanceiro l\n" +
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
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                ReportDTO reportDTO = new ReportDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarAll(String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA " +
                "from lancamentofinanceiro l\n" +
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
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, dataInicial);
            pst.setString(2, dataFinal);
            pst.setBoolean(3, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA") || rs.getString(8).equals("TRANSFERENCIA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    //todo: Tentativa de correcao do bug de lancamento financeiro quando existe apenas um resultado
//
//    public List<LancamentoFinanceiroDTO> listarAll(String dataInicial, String dataFinal, Boolean cancelado) {
//        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
//                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA " +
//                "from lancamentofinanceiro l\n" +
//                "join usuario u\n" +
//                "on l.usuario = u.id\n" +
//                "join centrodecusto c\n" +
//                "on l.centroDeCusto = c.id\n" +
//                "join operacao o\n" +
//                "on l.operacao = o.id\n" +
//                "where l.data between ? and ?\n" +
//                "and l.cancelado = ?\n" +
//                "order by l.id desc\n" +
//                "limit 100";
//
//        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
//        ReportDTO reportDTO = new ReportDTO();
//        double totalEntrada = 0.0;
//        double totalSaida = 0.0;
//
//        try {
//            pst = conexao.prepareStatement(sql);
//            pst.setString(1, dataInicial);
//            pst.setString(2, dataFinal);
//            pst.setBoolean(3, cancelado);
//            rs = pst.executeQuery();
//
//            while (rs.next()) {
//                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
//                lancamentoFinanceiroDTO.setId(rs.getInt(1));
//                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
//                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
//                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
//                lancamentoFinanceiroDTO.setData(rs.getString(5));
//                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
//                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
//                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
//                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
//                lancamentoFinanceiroDTO.setReceita(rs.getString(10));
//
//                if (rs.getString(10).equals("ENTRADA")) {
//                    totalEntrada += rs.getDouble(6);
//                } else if (rs.getString(10).equals("SAIDA") || rs.getString(8).equals("TRANSFERENCIA")) {
//                    totalSaida += rs.getDouble(6);
//                }
//
//                lancamentoFinanceiroDTO.setReport(reportDTO);
//                lista.add(lancamentoFinanceiroDTO);
//            }
//
//            // Após o loop, calcule os totais
//            double totalFinal = totalEntrada - totalSaida;
//            reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
//            reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
//            reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//
//        return lista;
//    }

    public LancamentoFinanceiroDTO buscaLacamento(int id) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.descricao as DESCRICAO, l.desconto as DESCONTO, l.descontoTipo as TIPO, l.cancelado as CANCELADO," +
                " l.idLancamentoAnexo as ANEXO from lancamentofinanceiro l\n" +
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
                lancamentoFinanceiroDTO.setDescricao(rs.getString(7));
                lancamentoFinanceiroDTO.setDesconto(Func.formataPrecoPadrao(rs.getString(8)));
                lancamentoFinanceiroDTO.setDescontoTipo(rs.getString(9));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(10));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(11));
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
            new Message("Erro!", "Erro ao buscar produto!" + e, "error");
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

    public List<LancamentoFinanceiroDTO> listarPorOperacao(int idOperacao, Boolean cancelado) {
         String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                 " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA " +
                 "from lancamentofinanceiro l\n" +
                "join usuario u\n" +
                "on l.usuario = u.id\n" +
                "join centrodecusto c\n" +
                "on l.centroDeCusto = c.id\n" +
                "join operacao o\n" +
                "on l.operacao = o.id\n" +
                "where l.operacao = ? " +
                "and l.cancelado = ?\n" + "order by l.id desc";
        List<LancamentoFinanceiroDTO> lista = new ArrayList<>();
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idOperacao);
            pst.setBoolean(2, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorOperacao(int idOperacao, String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA" +
                "from lancamentofinanceiro l\n" +
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
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idOperacao);
            pst.setString(2, dataInicial);
            pst.setString(3, dataFinal);
            pst.setBoolean(4, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCusto(int idCentroDeCusto, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA" +
                " from lancamentofinanceiro l\n" +
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
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setBoolean(2, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCusto(int idCentroDeCusto, String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA" +
                " from lancamentofinanceiro l\n" +
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
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setString(2, dataInicial);
            pst.setString(3, dataFinal);
            pst.setBoolean(4, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCustoOperacao(int idCentroDeCusto, int idOperacao, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA" +
                " from lancamentofinanceiro l\n" +
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
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setInt(2, idOperacao);
            pst.setBoolean(3, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
            while (rs.next()) {
                LancamentoFinanceiroDTO lancamentoFinanceiroDTO = new LancamentoFinanceiroDTO();
                lancamentoFinanceiroDTO.setId(rs.getInt(1));
                lancamentoFinanceiroDTO.setOperacao(rs.getString(2));
                lancamentoFinanceiroDTO.setCentro(rs.getString(3));
                lancamentoFinanceiroDTO.setUsuario(rs.getString(4));
                lancamentoFinanceiroDTO.setData(rs.getString(5));
                lancamentoFinanceiroDTO.setValor(Func.formataPrecoPadrao(rs.getString(6)));
                lancamentoFinanceiroDTO.setCancelado(rs.getBoolean(7));
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }
                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }

    public List<LancamentoFinanceiroDTO> listarPorCentroDeCustoOperacao(int idCentroDeCusto, int idOperacao, String dataInicial, String dataFinal, Boolean cancelado) {
        String sql = "select l.id as ID, o.descricao as OPERACAO, c.nome as CENTRO, u.nome as USUARIO, DATE_FORMAT(l.data,'%d/%m/%Y') as DATA," +
                " l.valorTotal as VALOR, l.descricao as DESCRICAO, l.cancelado as CANCELADO, o.operacao as TIPOP, l.idLancamentoAnexo as ANEXO, o.receita as RECEITA" +
                " from lancamentofinanceiro l\n" +
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
        ReportDTO reportDTO = new ReportDTO();
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, idCentroDeCusto);
            pst.setInt(2, idOperacao);
            pst.setString(3, dataInicial);
            pst.setString(4, dataFinal);
            pst.setBoolean(5, cancelado);
            rs = pst.executeQuery();
            double totalEntrada = 0.0;
            double totalSaida = 0.0;
            double totalFinal = 0.0;
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
                lancamentoFinanceiroDTO.setTipoOperacao(rs.getString(8));
                lancamentoFinanceiroDTO.setIdLancamentoAnexo(rs.getInt(9));
                lancamentoFinanceiroDTO.setReceita(rs.getString(10));

                if(rs.getString(10).equals("ENTRADA")) {
                    totalEntrada = totalEntrada + rs.getDouble(6);
                }
                if(rs.getString(10).equals("SAIDA")) {
                    totalSaida = totalSaida + rs.getDouble(6);
                }
                if(rs.getString(10).equals("NENHUM")) {
                    lancamentoFinanceiroDTO.setValor("0.00");
                }

                totalFinal = totalEntrada - totalSaida;
                reportDTO.setTotalEntrada(Func.formataPrecoPadrao(String.valueOf(totalEntrada)));
                reportDTO.setTotalSaida(Func.formataPrecoPadrao(String.valueOf(totalSaida)));
                reportDTO.setTotalFinal(Func.formataPrecoPadrao(String.valueOf(totalFinal)));

                lancamentoFinanceiroDTO.setReport(reportDTO);
                lista.add(lancamentoFinanceiroDTO);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
}

