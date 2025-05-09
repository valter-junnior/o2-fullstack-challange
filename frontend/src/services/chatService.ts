import { config } from '@/settings/config';
import { Client, type StompSubscription } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export type ChatMessage = {
  id: string;
  sender: "user" | "bot";
  message: string;
};

type MessageCallback = (message: string) => void;

class ChatService {
  private stompClient: Client | null = null;
  private subscription: StompSubscription | null = null;

  // Conectar ao WebSocket
  connect(onMessageReceived: MessageCallback) {
    const socket = new SockJS(config.ws.url);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {},
      onConnect: (frame) => {
        console.log('Conectado ao WebSocket:', frame);
        this.subscribeToMessages(onMessageReceived);
      },
      onStompError: (frame) => {
        console.error('Erro no STOMP:', frame);
      },
    });

    this.stompClient.activate();
  }

  // Assinar o tópico para receber mensagens
  private subscribeToMessages(onMessageReceived: MessageCallback) {
    if (this.stompClient) {
      this.subscription = this.stompClient.subscribe(config.ws.topic, (message) => {
        if (message.body) {
          onMessageReceived(message.body);
        }
      });
    }
  }

  // Enviar uma mensagem
  sendMessage(message: string) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: config.ws.send,
        body: message
      });
    } else {
      console.error('WebSocket não está conectado');
    }
  }

  // Desconectar do WebSocket
  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}

export const chatService = new ChatService();
