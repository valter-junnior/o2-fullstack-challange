import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

import { DataService } from "@/services/dataService";
import { toast } from "sonner";

const productSchema = z.object({
  id: z.number().nullable(),
  name: z
    .string()
    .nonempty("Name is required")
    .max(255, "Name must be at most 255 characters"),
  description: z.string().nonempty("Description is required"),
  quantity: z.number().min(0, "Quantity must be at least 0"),
  unitPrice: z.number().gt(0, "Price must be greater than 0"),
  category: z.string().nonempty("Category is required"),
});

export type ProductFormData = z.infer<typeof productSchema>;

interface ProductFormProps {
  initialData?: ProductFormData;
  callbackSubmit: () => void;
}

export default function ProductFormPage({
  initialData,
  callbackSubmit,
}: ProductFormProps) {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<ProductFormData>({
    resolver: zodResolver(productSchema),
    defaultValues: initialData || {
      id: null,
      name: "",
      description: "",
      quantity: 0,
      unitPrice: 0,
      category: "",
    },
  });

  const onSubmit = (data: ProductFormData) => {
    if (initialData) {
      DataService.put(`/products/${initialData.id}`, data);
    } else {
      DataService.post("/products", data);
    }
    toast.success("Product saved successfully");
    callbackSubmit();
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid gap-2">
        <Label htmlFor="name">Name</Label>
        <Input id="name" {...register("name")} />
        {errors.name && (
          <p className="text-sm text-red-500">{errors.name.message}</p>
        )}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="description">Description</Label>
        <Textarea id="description" {...register("description")} />
        {errors.description && (
          <p className="text-sm text-red-500">{errors.description.message}</p>
        )}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="quantity">Quantity</Label>
        <Input
          id="quantity"
          type="number"
          {...register("quantity", { valueAsNumber: true })}
        />
        {errors.quantity && (
          <p className="text-sm text-red-500">{errors.quantity.message}</p>
        )}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="unitPrice">Unit Price (R$)</Label>
        <Input
          id="unitPrice"
          type="number"
          step="0.01"
          {...register("unitPrice", { valueAsNumber: true })}
        />
        {errors.unitPrice && (
          <p className="text-sm text-red-500">{errors.unitPrice.message}</p>
        )}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="category">Category</Label>
        <Input id="category" {...register("category")} />
        {errors.category && (
          <p className="text-sm text-red-500">{errors.category.message}</p>
        )}
      </div>

      <Button type="submit">{initialData ? "Update" : "Create"}</Button>
    </form>
  );
}
