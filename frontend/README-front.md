# <h1 align="center" style="font-weight: bold;">Frontend 💻</h1>

<p align="center">
 <a href="#technologies">Technologies</a> • 
 <a href="#getting-started">Getting Started</a> • 
 <a href="#api-endpoints">API Endpoints</a> •
</p>

<p align="center">
    <b>AI agent built with Spring Boot that integrates with Groq API and runs inside a Docker environment.</b>
</p>

## 💻 Technologies

- React
- Vite
- Tailwind CSS
- React Router DOM
- Radix UI
- Zod
- Axios
- Lucide Icons
- Recharts
- Sonner

## 🚀 Getting Started

### Prerequisites

- [Node.js](https://github.com/)
- [Git](https://github.com/)
- [Vite](https://vitejs.dev/)
- [Tailwind CSS](https://tailwindcss.com/)

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

CHATBOT_BACKEND_URL=http://backend:8080/api
CHATBOT_API_KEY=

MYSQL_ROOT_PASSWORD=rootpassword
MYSQL_DATABASE=mydatabase
MYSQL_USER=user
MYSQL_PASSWORD=userpassword

VITE_API_URL=http://localhost/backend/api
VITE_WS_URL=http://localhost/agent/chat
VITE_WS_TOPIC=/topic/messages
VITE_WS_SEND=/app/sendMessage
VITE_LOCAL_STORAGE_KEY=app-data
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

## 📍 Routes

| Route              | Description                                            |
| ------------------ | ------------------------------------------------------ |
| `/`                | The homepage/dashboard of the application.             |
| `/products`        | Displays the list of products available in the system. |
| `/stock-movements` | Shows the stock movement history and details.          |