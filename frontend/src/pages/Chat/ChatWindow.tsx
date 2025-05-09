import MessageList from "./MessageList";
import MessageInput from "./MessageInput";
import { X } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { ChatMessage } from "@/services/chatService";

interface Props {
  messages: ChatMessage[];
  onSend: (text: string) => void;
  onClose: () => void;
}

export default function ChatWindow({ messages, onSend, onClose }: Props) {
  return (
    <div className="flex flex-col h-full border-primary">
      <div className="p-2 border-b flex justify-between items-center border-primary">
        <span className="font-semibold">Chat</span>
        <Button
          variant="ghost"
          onClick={onClose}
          className="hover:text-red-500"
        >
          <X className="h-5 w-5" />
        </Button>
      </div>
      <MessageList messages={messages} />
      <MessageInput onSend={onSend} />
    </div>
  );
}
