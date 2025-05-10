# <h1 align="center" style="font-weight: bold;">AI Agent 💻</h1>

<p align="center">
 <a href="#technologies">Technologies</a> • 
 <a href="#getting-started">Getting Started</a> • 
 <a href="#api-endpoints">API Endpoints</a> •
</p>

<p align="center">
    <b>AI agent built with Spring Boot that integrates with Groq API and runs inside a Docker environment.</b>
</p>

---

## 💻 Technologies

- Java 21+
- Spring Boot
- MySQL
- Docker & Docker Compose
- Nginx

---

## 🚀 Getting Started

### Prerequisites

- [Java 17+](https://adoptium.net/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Git](https://git-scm.com/)

### Cloning

```bash
git clone https://github.com/valter-junnior/o2-fullstack-challange
cd o2-fullstack-challange
```

### Config `.env` file

Create a `.env` file at the root using `.env.example` as reference:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://database:3306/mydatabase
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=rootpassword

CHATBOT_BACKEND_URL=http://localhost/backend/api
CHABOT_API_KEY=

MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=mydatabase
MYSQL_USER=user
MYSQL_PASSWORD=userpassword
```

### Groq API Key

For the wizard to work properly, you will need a Groq API Key.
Follow the steps below to generate yours:

1. Visit the Groq website: [https://console.groq.com](https://console.groq.com)
2. Log in to your account.
3. In the main panel, click on **API Keys** in the side menu.
4. Click on the **Generate new key** button
5. In your `.env` file, set the variable:

   ```dotenv
   CHABOT_API_KEY=gsk_your_token_here
   ```

### Running with Docker

```bash
docker-compose --env-file .env up --build
```

This will start:

- Agent on port `8081`
- Backend on port `8082`
- MySQL database on port `3306`
- Nginx on port `80`

---

## 📍 API Endpoints

| Route                    | Description                                                        |
| ------------------------ | ------------------------------------------------------------------ |
| `WebSocket /sendMessage` | Sends message to the AI agent and receives response over WebSocket |

### WebSocket /sendMessage

The `/sendMessage` WebSocket allows you to send commands to the sales and inventory assistant.
The assistant **always** responds in JSON format and only recognizes commands about **sales** and **inventory movements**.

---

**Message sent:**

```json
{
  "content": "Quero cadastrar uma movimentação para o Produto de Id 123, quantidade 10, dia 08/05/2025"
}
```

**Expected AI response:**

```json
{
  "action": "CREATE_STOCK_MOVEMENT",
  "productId": 123,
  "quantity": 10,
  "type": "ENTRY",
  "date": "2025-05-08"
}
```

**Expected API Response:**

```
Movimento de Estoque Criado com Sucesso:
  ID do Movimento: 1
  Produto: Nome do Produto (123)
  Quantidade: 10
  Tipo de Movimento: Entrada
  Data do Movimento: 2025-05-08
```

---

**Example with missing parameter:**

```json
{
  "content": "Quero cadastrar uma movimentação para o Produto de Id 123"
}
```

**Expected AI response:**

```json
{
  "action": "INVALID_PARAMETER",
  "message": "Por favor, informe a quantidade e a data da movimentação."
}
```

**Expected API Response:**

```
Por favor, informe a quantidade e a data da movimentação.
```

---

**Sales query in the period:**

```json
{
  "content": "Quanto vendemos entre 01/01/2025 e 31/01/2025?"
}
```

**Expected AI response:**

```json
{
  "action": "CONSULT_EXIT_STOCK_MOVEMENT",
  "startAt": "2025-01-01",
  "endAt": "2025-01-31"
}
```

**Expected API Response:**

```
Aqui está o relatório solicitado de 2025-01-01 até 2025-01-31
Quantidade de Movimentos de Saída: 10
Valor Total de Movimentos de Saída: R$ 100.00
```

---

**Command not understood:**

```json
{
  "content": "Qual a previsão do tempo amanhã?"
}
```

**Expected AI response:**

```json
{
  "action": "MESSAGE_UNDERSTAND",
  "message": "Não entendi o comando."
}
```

**Expected API Response:**

```
Não entendi o comando.
```