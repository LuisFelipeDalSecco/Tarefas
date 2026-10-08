# TASK 01 — API REST de Gerenciamento de Tarefas

## Contexto

O time precisa disponibilizar uma API REST simples para gerenciamento de tarefas internas.

Neste primeiro desafio, o objetivo é construir somente o **backend**, aplicando os fundamentos de desenvolvimento de APIs REST com **Java + Spring Boot**.

A API deverá permitir criar, consultar, atualizar e excluir tarefas.

---

## Objetivo da Task

Desenvolver uma API REST responsável pelo gerenciamento de tarefas.

A aplicação deve possuir uma arquitetura organizada em camadas e persistir os dados em banco de dados relacional.

---

## Stack esperada

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Maven
- JSON
- Postman, Insomnia ou REST Client para testes

---

## Escopo

A API deverá permitir:

- Criar uma tarefa
- Listar todas as tarefas
- Buscar uma tarefa pelo ID
- Atualizar uma tarefa
- Alterar somente o status de uma tarefa
- Excluir uma tarefa

Não deverá existir frontend.

---

# Modelo de dados

## Tarefa

A entidade `Tarefa` deverá possuir os seguintes campos:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---:|---|
| id | Long | Sim | Gerado automaticamente |
| titulo | String | Sim | Máximo de 100 caracteres |
| descricao | String | Não | Máximo de 500 caracteres |
| status | Enum | Sim | Valor inicial: `PENDENTE` |
| dataCriacao | LocalDateTime | Sim | Gerada automaticamente |
| dataAtualizacao | LocalDateTime | Não | Atualizada sempre que a tarefa sofrer alteração |

---

# Status disponíveis

Criar um enum chamado `StatusTarefa`.

Valores permitidos:

```text
PENDENTE
EM_ANDAMENTO
CONCLUIDA
```

Uma nova tarefa deverá ser criada automaticamente com o status:

```text
PENDENTE
```

O cliente da API não deverá precisar informar o status durante a criação.

---

# Endpoints obrigatórios

## 1. Criar tarefa

### Endpoint

```http
POST /api/tarefas
```

### Request

```json
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Revisar criação de APIs REST"
}
```

### Regras

- `titulo` é obrigatório.
- `titulo` não pode estar vazio.
- `titulo` deve possuir no máximo 100 caracteres.
- `descricao` é opcional.
- `descricao` deve possuir no máximo 500 caracteres.
- O status inicial deve ser `PENDENTE`.
- `dataCriacao` deve ser preenchida automaticamente.

### Resposta esperada

HTTP:

```text
201 Created
```

Exemplo:

```json
{
  "id": 1,
  "titulo": "Estudar Spring Boot",
  "descricao": "Revisar criação de APIs REST",
  "status": "PENDENTE",
  "dataCriacao": "2026-10-07T17:00:00",
  "dataAtualizacao": null
}
```

---

## 2. Listar tarefas

### Endpoint

```http
GET /api/tarefas
```

### Resposta esperada

```text
200 OK
```

Exemplo:

```json
[
  {
    "id": 1,
    "titulo": "Estudar Spring Boot",
    "descricao": "Revisar criação de APIs REST",
    "status": "PENDENTE",
    "dataCriacao": "2026-10-07T17:00:00",
    "dataAtualizacao": null
  }
]
```

Caso não existam tarefas cadastradas, retornar:

```json
[]
```

com status:

```text
200 OK
```

---

## 3. Buscar tarefa por ID

### Endpoint

```http
GET /api/tarefas/{id}
```

Exemplo:

```http
GET /api/tarefas/1
```

### Caso exista

Retornar:

```text
200 OK
```

### Caso não exista

Retornar:

```text
404 Not Found
```

Exemplo de resposta:

```json
{
  "status": 404,
  "erro": "NOT_FOUND",
  "mensagem": "Tarefa não encontrada."
}
```

---

## 4. Atualizar tarefa

### Endpoint

```http
PUT /api/tarefas/{id}
```

### Request

```json
{
  "titulo": "Estudar Spring Boot e JPA",
  "descricao": "Revisar API REST e persistência"
}
```

### Regras

- A tarefa deve existir.
- `titulo` continua obrigatório.
- As mesmas validações da criação devem ser aplicadas.
- O `id` não pode ser alterado.
- `dataCriacao` não pode ser alterada.
- `dataAtualizacao` deve receber a data/hora da alteração.
- Este endpoint não deve alterar o status da tarefa.

### Resposta

```text
200 OK
```

Caso o ID não exista:

```text
404 Not Found
```

---

## 5. Alterar status da tarefa

### Endpoint

```http
PATCH /api/tarefas/{id}/status
```

### Request

```json
{
  "status": "EM_ANDAMENTO"
}
```

### Regras

São aceitos somente:

```text
PENDENTE
EM_ANDAMENTO
CONCLUIDA
```

Caso o valor informado seja inválido, retornar:

```text
400 Bad Request
```

Ao alterar o status, atualizar também:

```text
dataAtualizacao
```

### Resposta

```text
200 OK
```

---

## 6. Excluir tarefa

### Endpoint

```http
DELETE /api/tarefas/{id}
```

### Caso exista

Excluir o registro e retornar:

```text
204 No Content
```

O response body deverá estar vazio.

### Caso não exista

Retornar:

```text
404 Not Found
```

---

# Validações obrigatórias

A API deve validar pelo menos os seguintes cenários:

### Título nulo

```json
{
  "titulo": null
}
```

Resultado:

```text
400 Bad Request
```

### Título vazio

```json
{
  "titulo": ""
}
```

Resultado:

```text
400 Bad Request
```

### Título maior que 100 caracteres

Resultado:

```text
400 Bad Request
```

### Descrição maior que 500 caracteres

Resultado:

```text
400 Bad Request
```

### ID inexistente

Resultado:

```text
404 Not Found
```

---

# Tratamento de erros

Não retornar stack trace ou mensagens internas do Java para o consumidor da API.

Criar um tratamento global de exceções.

Sugestão:

```text
@RestControllerAdvice
```

A resposta de erro deve seguir um padrão.

Exemplo:

```json
{
  "status": 400,
  "erro": "BAD_REQUEST",
  "mensagem": "Existem campos inválidos.",
  "campos": {
    "titulo": "O título é obrigatório."
  }
}
```

Para recurso inexistente:

```json
{
  "status": 404,
  "erro": "NOT_FOUND",
  "mensagem": "Tarefa não encontrada."
}
```

---

# Arquitetura esperada

A aplicação deve ser organizada, no mínimo, nas seguintes responsabilidades:

```text
controller
service
repository
entity
dto
exception
```

Uma estrutura possível:

```text
src/main/java
└── br.com.seuprojeto.tarefas
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    └── exception
```

Não é obrigatório seguir exatamente essa estrutura, mas as responsabilidades devem estar separadas.

---

# Regras de arquitetura

## Controller

Responsável por:

- Receber requisições HTTP
- Validar os dados recebidos
- Delegar operações para o Service
- Retornar o status HTTP adequado

Evitar colocar regra de negócio dentro do Controller.

---

## Service

Responsável por:

- Regras de negócio
- Busca de registros
- Atualização de entidades
- Lançamento de exceções quando necessário

---

## Repository

Responsável somente pela persistência e consulta dos dados.

Utilizar:

```java
JpaRepository
```

---

## DTO

Não utilizar a entidade JPA diretamente como objeto de entrada da API.

Criar DTOs para receber e retornar informações.

Sugestão:

```text
CriarTarefaRequest
AtualizarTarefaRequest
AtualizarStatusRequest
TarefaResponse
```

Os nomes são apenas sugestões.

---

# Banco de dados

Utilizar PostgreSQL.

Criar uma tabela equivalente a:

```text
TB_TAREFA
```

Não é necessário criar o SQL manualmente caso utilize geração automática do Hibernate durante o desenvolvimento.

O banco deverá persistir os registros entre reinicializações da aplicação.

---

# Regras importantes

Não implementar neste desafio:

- Spring Security
- JWT
- Usuários
- Autenticação
- Docker
- Microserviços
- Frontend
- Mensageria
- Cache

O foco desta task é dominar os fundamentos de uma API REST bem estruturada.

---

# Critérios de aceite

A task será considerada concluída quando:

- [ ] A aplicação iniciar sem erros.
- [ ] Uma tarefa puder ser criada.
- [ ] O status inicial for `PENDENTE`.
- [ ] Todas as tarefas puderem ser listadas.
- [ ] Uma tarefa puder ser consultada pelo ID.
- [ ] Uma tarefa puder ser atualizada.
- [ ] O status puder ser alterado separadamente.
- [ ] Uma tarefa puder ser excluída.
- [ ] IDs inexistentes retornarem `404`.
- [ ] Dados inválidos retornarem `400`.
- [ ] Criação retornar `201`.
- [ ] Exclusão retornar `204`.
- [ ] As validações estiverem funcionando.
- [ ] Houver tratamento global de exceções.
- [ ] O projeto estiver dividido em Controller, Service e Repository.
- [ ] A API utilizar DTOs.
- [ ] Os dados estiverem persistidos no PostgreSQL.

---

# Cenário mínimo de teste

Execute este fluxo:

### 1.

Criar uma tarefa.

```http
POST /api/tarefas
```

### 2.

Consultar a tarefa criada.

```http
GET /api/tarefas/1
```

### 3.

Alterar seu título.

```http
PUT /api/tarefas/1
```

### 4.

Alterar o status para:

```text
EM_ANDAMENTO
```

### 5.

Consultar novamente e verificar a alteração.

### 6.

Alterar o status para:

```text
CONCLUIDA
```

### 7.

Excluir a tarefa.

### 8.

Consultar novamente.

O resultado esperado deverá ser:

```text
404 Not Found
```

---

# Desafio extra — opcional

Somente depois que todos os requisitos principais estiverem funcionando.

Adicionar filtro por status:

```http
GET /api/tarefas?status=PENDENTE
```

Exemplo:

```http
GET /api/tarefas?status=CONCLUIDA
```

Este requisito é opcional e não faz parte da entrega principal.

---

# Entrega esperada

Subir o projeto em um repositório Git contendo:

```text
README.md
pom.xml
src/
```

O `README.md` do projeto deve conter:

- Tecnologias utilizadas
- Como executar a aplicação
- Configuração do banco
- Lista dos endpoints

---

## Observação do responsável técnico

Neste desafio, priorize código simples, legível e bem separado.

Não tente criar uma arquitetura excessivamente sofisticada.

O objetivo é demonstrar domínio dos fundamentos:

```text
HTTP → Controller → Service → Repository → Banco
```

Antes de avançar para APIs mais complexas, essa estrutura precisa estar clara e natural durante o desenvolvimento.
