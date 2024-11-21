# Tools Challenge

Projeto para implementar uma API de Pagamentos de um banco com algumas funcionalidades que serão descritas ao longo deste README.
Escolhi desenvolver utilizando SpringBoot por ser a forma como trabalhei nos últimos projetos que participei e mesmo não sendo necessário, resolvi persistir os dados em um banco de dados para mostrar como tenho trabalhado ultimamente. 



# Tecnologias
  - Java
  - SpringBoot
  - Flyway
  - PostgreSQL
  - JUnit

# Informações importantes

Abra o projeto em alguma IDE com suporte para Java, abra o aquivo pom.xml e coloque para baixar as dependências que contém nele.
Após isso, crie um banco de dados postgreSQL e abra o arquivo ``application.properties`` e configure para o ``nome do banco de dados`` que criou, seu ``nome de usuário`` e ``senha`` do banco de dados. 

     spring.application.name=desafio-tools
     spring.datasource.url = jdbc:postgresql://localhost:5432/desafio_tools
     spring.datasource.username = postgres
     spring.datasource.password = postgres

O flyway criará automaticamente o schema no banco de dados e também a tabela da transação. Feito isso, o sistema estará configurado para aceitar as requisições via ```POSTMAN``` ou ```THUNDER CLIENT do VsCode```.

# API'S

As operações serão as seguintes: URL_BASE: 
- Pagamento: método POST - http://localhost:8080/transacao
  
    OBS: Os campos faltantes no JSON de envio de pagamento serão gerados automaticamente via sistema, que são ``id`` e ``dataHora``. E o número do cartão no envio da transação irá o número todo e no retorno vem criptografado.
  
   JSON:
     ```{
       {
          "transacao": {
            "cartao": "0123456789123456789",
            "descricao": {
              "valor": 123.25,
              "estabelecimento": "PetShop Mundo Cão"
            },
            "formaPagamento": {
              "tipo": "AVISTA",
              "parcelas": 1
            }
          }
        }
    
- Estorno: método PATCH - http://localhost:8080/transacao/estorno/{id}



- Consulta por id: método GET - http://localhost:8080/transacao/{id}
- Consulta todos: método GET - http://localhost:8080/transacao
