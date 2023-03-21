<a name="readme-top"></a>

<h1>REST API Financeira</h1> 

<p align="center">
  <img src="https://img.shields.io/static/v1?label=Spring&message=framework&color=blue&style=for-the-badge&logo=Spring"/>
  <img src="https://img.shields.io/static/v1?label=Heroku&message=deploy&color=blue&style=for-the-badge&logo=Heroku"/>
  <img src="https://img.shields.io/static/v1?label=MongoDB&message=database&color=blue&style=for-the-badge&logo=mongodb"/>
  <img src="http://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=openjdk"/>
  <img src="http://img.shields.io/static/v1?label=STATUS&message=CONCLUIDO&color=GREEN&style=for-the-badge"/>
  <img src="http://img.shields.io/static/v1?label=TESTES&message=100%&color=GREEN&style=for-the-badge"/>
  <img src="http://img.shields.io/static/v1?label=License&message=MIT&color=green&style=for-the-badge"/>

### Tópicos 

:small_blue_diamond: [Descrição do projeto](#descrição-do-projeto)

:small_blue_diamond: [Acesso](#acesso)

:small_blue_diamond: [Funcionalidades](#funcionalidades)

:small_blue_diamond: [Pré-Requisitos e como Rodar o Servidor](#pré-requisitos)

:small_blue_diamond: [Tecnologias](#tecnologias)

:small_blue_diamond: [Autor](#autor)

## Descrição do projeto 

<p align="justify">
  API REST desenvolvida para fornecer a configuração de back-end para o aplicativo de controle de finanças de pessoais Finanças Webapp.<br />
  API desenvolvida em Java com o framework Spring, utiliza um sistema de autenticação stateless e banco de dados MongoDB. 
</p>

## Acesso
  As funcionalidades da API podem ser acessadas [através do aplicativo web](https://webapp-financeira.herokuapp.com) que utiliza os endpoints para seu funcionamento.
  
  Caso queira rodar o servidor localmente [siga os passos listados aqui](#pré-requisitos).
  
## Funcionalidades

:heavy_check_mark: Cadastro e autenticação de usuários

:heavy_check_mark: Cadastro de novas despesas e receitas pelos usuários

:heavy_check_mark: Consulta, edição e exclusão de despesas e receitas já cadastradas pelo usuário autenticado

:heavy_check_mark: Consulta de um resumo das despesas, receitas e saldo de cada mês do usuário 

:heavy_check_mark: Geração de usuário demo temporário com despesas e receitas variadas já cadastradas para demonstração

### API REST
  Os endpoints da API REST estão descritos abaixo.
 
#### Cadastrar novo usuário
&nbsp;&nbsp;&nbsp;&nbsp;`POST` /usuarios

#### Criar novo usuário temporário para demonstração
&nbsp;&nbsp;&nbsp;&nbsp;`POST` /usuarios/demo

#### Cadastrar novo usuário
&nbsp;&nbsp;&nbsp;&nbsp;`POST` /usuarios
  
#### Criar novo usuário temporário para demonstração
&nbsp;&nbsp;&nbsp;&nbsp;`POST` /usuarios/demo

#### Excluir usuário temporário
&nbsp;&nbsp;&nbsp;&nbsp;`DELETE` /usuarios/{email}

#### Listar todas as receitas cadastradas do usuario
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /receitas

#### Listar todas as receitas do usuário com uma descrição específica
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /receitas?descricao={descricao}

#### Listar todas as receitas do usuário de um mês específico
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /receitas/{ano}/{mes}

#### Buscar informações detalhadas de uma receita por seu id
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /receitas/{id}

#### Cadastrar nova receita
&nbsp;&nbsp;&nbsp;&nbsp;`POST` /receitas

#### Editar receita pelo id
&nbsp;&nbsp;&nbsp;&nbsp;`PUT` /receitas/{id}

#### Excluir receita pelo id
&nbsp;&nbsp;&nbsp;&nbsp;`DELETE` /receitas/{id}

#### Listar todas as despesas cadastradas do usuario
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /despesas

#### Listar todas as despesas do usuário com uma descrição específica
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /despesas?descricao={descricao}

#### Listar todas as despesas do usuário de um mês específico
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /despesas/{ano}/{mes}

#### Buscar informações detalhadas de uma despesa por seu id
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /despesas/{id}

#### Cadastrar nova despesa
&nbsp;&nbsp;&nbsp;&nbsp;`POST` /despesas

#### Editar despesa pelo id
&nbsp;&nbsp;&nbsp;&nbsp;`PUT` /despesas/{id}

#### Excluir despesa pelo id
&nbsp;&nbsp;&nbsp;&nbsp;`DELETE` /despesas/{id}
 
#### Exibir um resumo das despesas e receitas do mês
&nbsp;&nbsp;&nbsp;&nbsp;`GET` /resumo/{ano}/{mes}

<p align="right">(<a href="#readme-top">voltar para o início</a>)</p>
  
## Pré-Requisitos
  Para rodar o servidor localmente você precisa ter instalado as seguintes ferramentas: [JDK](https://www.java.com/pt-BR/download/), [Git](https://git-scm.com/) e [Maven](https://maven.apache.org/install.html).
  
  Além disso, para criar seu próprio banco de dados, [é preciso ter uma conta grauita no MongoDB Cloud](https://account.mongodb.com/account/register).
  
### Como rodar a aplicação

1. Clone este repositório
```
git clone https://github.com/thomazcm/rest-api-financeira
```

2. Na página do [MongoDB Atlas](https://cloud.mongodb.com/), clique em "Browse Collections" e crie uma nova Database com o nome que deseja usar.

3. Volte até a pagina inicial, clique em "Connect" e em seguida "Connect your Application". Copie a URI para se conectar ao seu banco de dados.

4. Por fim, [crie uma nova JWT key](https://www.javainuse.com/jwtgenerator) para que sejam gerados os tokens de autenticação do seu servidor. 

5. Popule o arquivo env.properties na pasta raiz do repositório com as configurações do seu banco de dados:

```
DB_URI=
DB_DATABASE=
JWT_SECRET=

#Instruções
#DB_URI=Cole aqui a sua URI do MongoDB
#DB_DATABASE=Nome da database que foi criada
#JWT_SECRET=Cole aqui sua JWT key
```

6. Por fim, navegue na linha de comando até a raiz do projeto e execute o comando:
```
mvnw spring-boot:run

## O servidor inciará na porta:8080 - acesse as endpoints por <http://localhost:8080> 
```

<p align="right">(<a href="#readme-top">voltar para o início</a>)</p>

## Tecnologias
As seguintes ferramentas foram usadas na construção do projeto:

- [Spring Boot 2.7.2]() - Framework
- [Maven]() - Gerenciamento de Dependências
- [Spring Security]() e [JJWT]() - Segurança
- [MongoDB Atlas]() - Banco de Dados
- [Mockito 4.5.1]() - Testes de Unidade
- [Postman]() - Testes de API
- [Eclipse]() e [VSCode]() - Editores

<p align="right">(<a href="#readme-top">voltar para o início</a>)</p>

## Licença

Este projeto esta sob a licença [MIT](./LICENSE). Consulte o arquivo LICENSE.md para mais informações.

<p align="right">(<a href="#readme-top">voltar para o início</a>)</p>

## Autor
<b>Thomaz Machado</b>🚀<br />
 <img style="border-radius: 50%;" src="https://avatars.githubusercontent.com/u/71472870?s=460&u=61b426b901b8fe02e12019b1fdb67bf0072d4f00&v=4" width="100px;" alt=""/><br />
Projeto desenvolvido por Thomaz Machado 👊 Entre em contato!  

[![Linkedin Badge](https://img.shields.io/badge/-Thomaz-blue?style=flat-square&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/tgmarinho/)](https://www.linkedin.com/in/tgmarinho/) 
[![Gmail Badge](https://img.shields.io/badge/-thomazcm@gmail.com-c14438?style=flat-square&logo=Gmail&logoColor=white&link=mailto:thomazcm@gmail.com)](mailto:thomazcm@gmail.com)
 
 <p align="right">(<a href="#readme-top">voltar para o início</a>)</p>
