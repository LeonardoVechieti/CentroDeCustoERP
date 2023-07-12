create table usuario (
id int primary key auto_increment,
nome varchar(50) not null, 
login varchar(25) not null unique,
senha varchar(25) not null,
inativo boolean,
perfil varchar(250)
);


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


create table centrodecusto(
id int primary key auto_increment,
nome varchar(200) not null,
descricao varchar(200),
inativo boolean
);


create table operacao(
id int primary key auto_increment,
descricao varchar(200) not null,
operacao varchar(200) not null,
inativo boolean
);

create table movimentacao(
id int primary key auto_increment,
idCentroDeCusto int not null,
operacao int not null,
valorTotal decimal(10,2),
data timestamp default current_timestamp,
descricao varchar(200),
foreign key(idCentroDeCusto) references centrodecusto(id),
foreign key(operacao) references operacao(id)
);

create table lanc_financeiros(
id int primary key auto_increment,
idProduto int not null,
idMoviementacao int not null,
idCentroDeCusto int not null,
idOperacao int not null,
quantidade decimal(10,2),
valorUnitario decimal(10,2),
valorTotal decimal(10,2),
data timestamp default current_timestamp,
descricao varchar(200),
foreign key(idProduto) references produto(id),
foreign key(idMoviementacao) references movimentacao(id),
foreign key(idCentroDeCusto) references centrodecusto(id),
foreign key(idOperacao) references operacao(id)
);



