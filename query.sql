create database dev;
use dev;

create table usuario (
id int primary key auto_increment,
nome varchar(50) not null, 
login varchar(25) not null unique,
senha varchar(25) not null,
inativo boolean,
perfil varchar(250)
);

insert into usuario( nome, login, senha, ativo, perfil)
values ('LEONARDO VECHEIT', 'LEONARDO', '1234', false, "ADMIN, USER");

create table produto(
id int primary key auto_increment,
descricao varchar(200) not null,
preco decimal(10,2),
unidade varchar(25),
inativo boolean,
servico boolean,
estoque boolean,
producao boolean,
dataCriacao timestamp default current_timestamp, 
dataModificacao datetime,
usuario int not null, 
foreign key(usuario) references usuario(id)
);

-- Renomeia a coluna ativo para ativoProduto
-- alter table produto change ativo inativo boolean;

insert into produto( descricao, preco, unidade, inativo, servico, estoque, producao, usuario)
values ('TECLADO', 100.10 , 'UNIDADE', false, true, false, true, 1);

select * from produto;

create table centrodecusto(
id int primary key auto_increment,
nome varchar(200) not null,
descricao varchar(200),
inativo boolean
);

insert into centrodecusto( nome, descricao, inativo)
values ('CENTRO DE CUSTO 1', 'CENTRO DE CUSTO 1', false);
insert into centrodecusto( nome, descricao, inativo)
values ('CENTRO DE CUSTO 2', 'CENTRO DE CUSTO 2', false);
insert into centrodecusto( nome, descricao, inativo)
values ('CENTRO DE CUSTO 3', 'CENTRO DE CUSTO 3', false);

create table operacao(
id int primary key auto_increment,
descricao varchar(200) not null,
operacao varchar(200) not null,
receita varchar(200) not null,
inativo boolean
);

-- alter table operacao add receita varchar(200) not null after operacao;
-- update operacao set receita = 'ENTRADA' where id=7;

-- use dev;
-- drop table estoque;
-- drop table lancamentofinanceiro;
CREATE TABLE lancamentofinanceiro (
    id INT PRIMARY KEY AUTO_INCREMENT,
    centroDeCusto INT NOT NULL,
    operacao INT NOT NULL,
    desconto DECIMAL(10, 2),
    descontoTipo VARCHAR(15),
    valorTotal DECIMAL(10, 2),
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descricao VARCHAR(200),
    usuario INT NOT NULL,
    idLancamentoAnexo INT,
    cancelado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (centroDeCusto) REFERENCES centroDeCusto (id),
    FOREIGN KEY (operacao) REFERENCES operacao (id),
    FOREIGN KEY (usuario) REFERENCES usuario (id),
    FOREIGN KEY (idLancamentoAnexo) REFERENCES lancamentofinanceiro (id)
);

create table estoque(
id int primary key auto_increment,
idProduto int not null,
idLancamentoFinanceiro int not null,
idCentroDeCusto int not null,
idOperacao int not null,
quantidade decimal(10,2),
valorUnitario decimal(10,2),
valorTotal decimal(10,2),
data timestamp default current_timestamp,
descricao varchar(200),
cancelado BOOLEAN DEFAULT FALSE,
foreign key(idProduto) references produto(id),
foreign key(idLancamentoFinanceiro) references lancamentofinanceiro(id),
foreign key(idCentroDeCusto) references centrodecusto(id),
foreign key(idOperacao) references operacao(id)
);

-- Adciona campo valor no estoque
-- alter table estoque add valor decimal(10,2);

insert into lancamentofinanceiro( centrodecusto, operacao, valortotal, descricao, usuario)
values (1, 1, 100.10, 'MOVIMENTACAO 1', 1);

insert into estoque( idProduto, idLancamentoFinanceiro, idCentroDeCusto, idOperacao, quantidade, valorUnitario, valorTotal, descricao)
values (1, 1, 1, 1, 10, 10.10, 100.10, 'ESTOQUE 1');

insert into estoque( idProduto, idLancamentoFinanceiro, idCentroDeCusto, idOperacao, quantidade, valorUnitario, valorTotal, descricao)
values (1, 1, 1, 1, 10, 10.10, 100.10, 'ESTOQUE 2');

insert into estoque( idProduto, idLancamentoFinanceiro, idCentroDeCusto, idOperacao, quantidade, valorUnitario, valorTotal, descricao)
values (1, 1, 1, 1, 10, 10.10, 100.10, 'ESTOQUE 3');







select * from estoque;
select * from lancamentofinanceiro;


---- Adapte a sql abaixo para o seu banco de dados
--SUM(CASE WHEN operacao = 'ENTRADA' THEN valorTotal ELSE 0 END) AS total_entradas,
--    SUM(CASE WHEN operacao = 'SAIDA' THEN valorTotal ELSE 0 END) AS total_saidas,
--    SUM(CASE WHEN operacao = 'ENTRADA' THEN valorTotal ELSE -valorTotal END) AS saldo_total
--
--
--
--select
--    SUM(CASE WHEN o.operacao = 'ENTRADA' THEN l.valorTotal ELSE 0 END) AS entradas,
--    SUM(CASE WHEN o.operacao = 'SAIDA' THEN l.valorTotal ELSE 0 END) AS saidas,
--    SUM(CASE WHEN o.operacao = 'ENTRADA' THEN l.valorTotal ELSE -l.valorTotal END) AS saldo
--    from lancamentofinanceiro l
--        join operacao o
--        on l.operacao = o.id
--        where l.cancelado = false
--        order by l.id desc
--        limit 100;
