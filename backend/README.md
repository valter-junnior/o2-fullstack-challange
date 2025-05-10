# <h1 align="center" style="font-weight: bold;">Backend 💻</h1>

<p align="center">
 <a href="#technologies">Technologies</a> • 
 <a href="#getting-started">Getting Started</a> • 
 <a href="#api-endpoints">API Endpoints</a> •
</p>

<p align="center">
    <b>Backend built with Spring Boot that integrates with Groq API and runs inside a Docker environment.</b>
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

## API Endpoints

| Route                             | Description                                                        |
| --------------------------------- | ------------------------------------------------------------------ |
| `WebSocket /sendMessage`          | Sends message to the AI agent and receives response over WebSocket |
| `GET /api/products`               | Get paginated list of products                                     |
| `GET /api/products/all`           | Get all products (no pagination)                                   |
| `POST /api/products`              | Create a new product                                               |
| `PUT /api/products/{id}`          | Update a product by ID                                             |
| `DELETE /api/products/{id}`       | Delete a product by ID                                             |
| `GET /api/reports/dashboard`      | Get dashboard summary (total price, quantity, exits, top products) |
| `GET /api/reports/sales`          | Get sales report between two dates                                 |
| `GET /api/movements`              | Get paginated stock movements (optional date filter)               |
| `POST /api/movements`             | Register a new stock movement                                      |
| `GET /api/movements/product/{id}` | Get movements for a specific product                               |
| `GET /api/movements/total-exit`   | Get total exit movements (optional date filter)                    |

---

### Products (`/api/products`)

* **GET /api/products** → Get paginated list
* **GET /api/products/all** → Get all products
* **POST /api/products** → Create new product
* **PUT /api/products/{id}** → Update product by ID
* **DELETE /api/products/{id}** → Delete product by ID

---

### Reports (`/api/reports`)

* **GET /api/reports/dashboard** → Get dashboard summary:
  total price, total quantity, total exit movements, top products

* **GET /api/reports/sales?startDate=YYYY-MM-DD\&endDate=YYYY-MM-DD** → Get sales per period

---

### Stock Movements (`/api/movements`)

* **GET /api/movements?page=0\&size=10** → Get paginated stock movements
  Optional: `startAt`, `endAt` (ISO date)

* **POST /api/movements** → Register new movement
  Body:

  ```json
  {
    "productId": 123,
    "quantity": 10,
    "type": "ENTRY" or "EXIT",
    "date": "2025-05-08"
  }
  ```

* **GET /api/movements/product/{productId}** → Get movements by product ID

* **GET /api/movements/total-exit?startAt=YYYY-MM-DD\&endAt=YYYY-MM-DD** → Get total exit movements by period
