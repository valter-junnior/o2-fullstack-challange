import type { ChatMessage } from "@/services/chatService";

interface Props {
  messages: ChatMessage[];
}

export default function MessageList({ messages }: Props) {
  return (
    <div className="flex-1 overflow-y-auto mb-2 space-y-2 p-4 border border-gray-300">
      {messages.map((msg) => (
        <div
          key={msg.id}
          className={`p-2 rounded-xl text-white pl-2 pr-5 max-w-[70%] ${
            msg.sender === "user" ? "bg-primary ml-auto" : "bg-gray-500 mr-auto"
          }`}
        >
          {msg.content.split("\n").map((line, index) => (
            <span key={index} className="block break-words">
              {line}
              <br />
            </span>
          ))}
        </div>
      ))}
    </div>
  );
}
