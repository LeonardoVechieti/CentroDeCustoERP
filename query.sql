create database dev;
use dev;

create table usuario (
id int primary key auto_increment,
nome varchar(50) not null, 
login varchar(25) not null unique,
senha varchar(25) not null,
ativo boolean,
perfil varchar(50)
);

insert into usuario( nome, login, senha, ativo, perfil)
values ('Leonardo Vechieti', 'leonardo', '1234', true, "admin, user");

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

insert into produto( descricao, preco, unidade, ativo, servico, estoque, producao, usuario)
values ('TECLADO', 100.10 , 'UNIDADE', true, true, false, true, 1);

select * from produto;