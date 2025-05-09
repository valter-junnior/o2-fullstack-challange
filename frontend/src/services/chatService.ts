import { config } from "@/settings/config";
import { Client, type StompSubscription } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export type ChatMessage = {
  id: string;
  sender: "user" | "bot";
  content: string;
};

type MessageCallback = (message: ChatMessage) => void;

class ChatService {
  private stompClient: Client | null = null;
  private subscription: StompSubscription | null = null;
  private reconnectAttempts: number = 0;
  private maxReconnectAttempts: number = 5;
  private reconnectInterval: number = 1000;
  private messageQueue: ChatMessage[] = []; 
  private onMessageReceived: MessageCallback;

  constructor(onMessageReceived: MessageCallback) {
    this.onMessageReceived = onMessageReceived;
  }

  connect() {
    const socket = new SockJS(config.ws.url);
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {},
      onConnect: (frame) => {
        console.log("Conectado ao WebSocket:", frame);
        this.reconnectAttempts = 0;
        this.subscribeToMessages();
        this.flushMessageQueue();
      },
      onStompError: (frame) => {
        console.error("Erro no STOMP:", frame);
        this.reconnect();
      },
      onWebSocketError: (error) => {
        console.error("Erro no WebSocket:", error);
        this.reconnect();
      },
    });

    this.stompClient.activate();
  }

  private reconnect() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      console.log(`Tentando reconectar... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);

      setTimeout(() => {
        console.log("Tentando reconectar...");
        this.connect();
      }, this.reconnectInterval * this.reconnectAttempts);
    } else {
      console.error("Número máximo de tentativas de reconexão atingido.");
    }
  }

  private subscribeToMessages() {
    if (this.stompClient) {
      this.subscription = this.stompClient.subscribe(
        config.ws.topic,
        (message) => {
          if (message.body) {
            try {
              const parsedMessage: ChatMessage = JSON.parse(message.body);
              this.onMessageReceived(parsedMessage);
            } catch (error) {
              console.error("Erro ao processar a mensagem recebida", error);
            }
          }
        }
      );
    }
  }

  sendMessage(message: ChatMessage) {
    if (this.stompClient && this.stompClient.connected) {
      const messageBody = JSON.stringify(message);

      this.stompClient.publish({
        destination: config.ws.send,
        body: messageBody,
      });
    } else {
      console.error("WebSocket não está conectado. Adicionando mensagem na fila.");
      this.messageQueue.push(message); 
      this.reconnect();
    }
  }

  private flushMessageQueue() {
    while (this.messageQueue.length > 0) {
      const message = this.messageQueue.shift(); 
      if (message) {
        this.sendMessage(message);
      }
    }
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}

export function buildChatService(onMessageReceived: MessageCallback): ChatService {
  return new ChatService(onMessageReceived);
}

export default ChatService;

