import { Button } from "@/components/ui/button";
import { DataService } from "@/services/dataService";
import { toast } from "sonner";

export function ProductDeletePage({
  id,
  onDelete,
}: {
  id: number;
  onDelete: () => void;
}) {
  const handleDelete = async () => {
    await DataService.delete(`/products/${id}`);
    toast.success("Product deleted successfully");
    onDelete();
  };

  return (
    <div className="flex justify-end gap-2 mt-4">
      <Button variant="outline" onClick={onDelete}>
        Cancel
      </Button>
      <Button variant="destructive" onClick={handleDelete}>
        Delete
      </Button>
    </div>
  );
}
