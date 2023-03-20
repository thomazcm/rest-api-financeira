<h1>REST API Financeira</h1> 

<p align="center">
  <img src="https://img.shields.io/static/v1?label=Spring&message=framework&color=blue&style=for-the-badge&logo=Spring"/>
  <img src="https://img.shields.io/static/v1?label=Heroku&message=deploy&color=blue&style=for-the-badge&logo=Heroku"/>
  <img src="https://img.shields.io/static/v1?label=MongoDB&message=database&color=blue&style=for-the-badge&logo=mongodb"/>
  <img src="http://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=openjdk"/>
  <img src="http://img.shields.io/static/v1?label=STATUS&message=CONCLUIDO&color=GREEN&style=for-the-badge"/>
  <img src="http://img.shields.io/static/v1?label=TESTES&message=100%&color=GREEN&style=for-the-badge"/>

### Tópicos 

:small_blue_diamond: [Descrição do projeto](#descrição-do-projeto)


:small_blue_diamond: [Acesso](#acesso)

:small_blue_diamond: [Funcionalidades](#funcionalidades)

:small_blue_diamond: [Pré-Requisitos e como Rodar o Servidor](#pré-requisitos)

:small_blue_diamond: [Tecnologias](#Tecnologias)

:small_blue_diamond: [Autor](#autor)

## Descrição do projeto 

<p align="justify">
  API REST desenvolvida para fornecer a configuração de back-end para o aplicativo de controle de finanças de pessoais Finanças Webapp.
  A API foi desenvolvida em Java utilizando o framework Spring, utiliza um sistema de autenticação stateless e o banco de dados MongoDB. 
</p>

## Acesso
  As funcionalidades da API podem ser acessadas [através do aplicativo web](https://webapp-financeira.herokuapp.com) que utiliza os endpoints para seu funcionamento.
  
  Caso queira rodar o servidor localmente [siga os passos listados aqui](#pré-requisitos).
  
## Funcionalidades

:heavy_check_mark: Cadastro e autenticação de usuários

:heavy_check_mark: Cadastro de novas despesas e receitas pelos usuários

:heavy_check_mark: Consulta, edição e exclusão de despesas e receitas já cadastradas pelo usuário autenticado

:heavy_check_mark: Consulta de um resumo das despesas, receitas e saldo de cada mês do usuário 

:heavy_check_mark: Geração de usuário demo com despesas e receitas variadas já cadastradas para demonstração

## Pré-Requisitos
  Para rodar o servidor localmente você precisa ter instalado as seguintes ferramentas: [JDK](https://www.java.com/pt-BR/download/), [Git](https://git-scm.com/) e [Maven](https://maven.apache.org/install.html).
  
  Além disso, para criar seu próprio banco de dados, [é preciso ter uma conta grauita no MongoDB Cloud](https://account.mongodb.com/account/register).
  
## Como rodar a aplicação :arrow_forward:

```
#Clone este repositório
git clone https://github.com/thomazcm/rest-api-financeira

```

Na página do [MongoDB Atlas](https://cloud.mongodb.com/), clique em "Browse Collections" e crie uma nova Database com o nome que deseja usar.

Volte até a pagina inicial, clique em "Connect" e em seguida "Connect your Application". Copie a URI para se conectar ao seu banco de dados.

Por fim, [crie uma nova JWT key](https://www.javainuse.com/jwtgenerator) para que sejam gerados os tokens de autenticação do seu servidor. 

Popule o arquivo env.properties na pasta raiz do repositório com as configurações do seu banco de dados:

```
DB_URI=
DB_DATABASE=
JWT_SECRET=

#Instruções
#DB_URI=Cole aqui a sua URI do MongoDB
#DB_DATABASE=Nome da database que foi criada
#JWT_SECRET=Cole aqui sua JWT key
```

Por fim, navegue na linha de comando até a raiz do projeto e execute o comando:
```
mvnw spring-boot:run

## O servidor inciará na porta:8080 - acesse as endpoints por <http://localhost:8080> 
```

