import { Button } from "@/components/ui/button";
import { Send } from "lucide-react";
import { useState } from "react";

interface Props {
  onSend: (text: string) => void;
}

export default function MessageInput({ onSend }: Props) {
  const [input, setInput] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (input.trim() !== "") {
      onSend(input.trim());
      setInput("");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-2 p-2">
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        className="flex-1 border rounded-lg px-3 py-2"
        placeholder="Type a message..."
      />
      <Button
        variant="default"
        type="submit"
        className="bg-primary rounded-lg"
      >
        <Send className="h-4 w-4" />
      </Button>
    </form>
  );
}
