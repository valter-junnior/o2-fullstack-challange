import { useEffect, useRef, useState } from "react";
import { MessageCircle } from "lucide-react";
import ChatWindow from "./ChatWindow";
import { Button } from "@/components/ui/button";
import ChatService, { buildChatService, type ChatMessage } from "@/services/chatService";

export default function Chat() {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const chatServiceRef = useRef<ChatService | null>(null);

  const addMessage = (message: ChatMessage) => {
    const newMessage: ChatMessage = {
      id: message.id ?? new Date().getTime().toString(),
      sender: message.sender ?? "bot",
      content: message.content,
    };

    setMessages((prevMessages) => [...prevMessages, newMessage]);
  };

  useEffect(() => {
    chatServiceRef.current = buildChatService((message: ChatMessage) => {
      addMessage(message);
    });

    chatServiceRef.current.connect();

    return () => {
      chatServiceRef.current?.disconnect();
    };
  }, []);

  const handleSendMessage = (message: string) => {
    const newMessage = {
      sender: "user",
      content: message,
    };

    addMessage(newMessage as ChatMessage);

    chatServiceRef.current?.sendMessage(newMessage as ChatMessage);
  };

  return (
    <>
      {!isOpen && (
        <div className="fixed bottom-4 right-4 z-50">
          <Button
            variant="default"
            onClick={() => setIsOpen(true)}
            className="rounded-full p-3 shadow-lg w-12 h-12"
          >
            <MessageCircle className="w-6 h-6" />
          </Button>
        </div>
      )}
      <div className="fixed bottom-4 right-4 z-1">
        <div
          className={`w-98 h-[60vh] bg-white rounded-lg shadow-lg flex flex-col border border-gray-200 shadow-gray-300 transform transition-transform duration-700 ease-in-out ${
            isOpen
              ? "translate-y-0 opacity-100"
              : "translate-y-120 opacity-100 pointer-events-none hidden"
          }`}
        >
          <ChatWindow
            messages={messages}
            onSend={handleSendMessage}
            onClose={() => setIsOpen(false)}
          />
        </div>
      </div>
    </>
  );
}
