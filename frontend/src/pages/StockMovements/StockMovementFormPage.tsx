import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { DataService } from "@/services/dataService";
import { toast } from "sonner";
import { useEffect, useState } from "react";

const stockMovementSchema = z.object({
  productId: z.number().nonnegative("Product ID is required"),
  date: z.string().nonempty("Date is required"),
  quantity: z.number().min(1, "Quantity must be at least 1"),
  type: z.enum(["ENTRY", "EXIT"], {
    required_error: "Movement type is required",
  }),
});

export type StockMovementFormData = z.infer<typeof stockMovementSchema>;

interface StockMovementFormProps {
  callbackSubmit: () => void;
}

export default function StockMovementFormPage({
  callbackSubmit,
}: StockMovementFormProps) {

  const [products, setProducts] = useState<any[]>([]);

  useEffect(() => {
    DataService.get("/products/all", {}).then((products) => {
      setProducts(products);
    });
  }, []);

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
  } = useForm<StockMovementFormData>({
    resolver: zodResolver(stockMovementSchema),
    defaultValues: {
      productId: 1,
      date: "",
      quantity: 1,
      type: "ENTRY",
    },
  });

  const onSubmit = (data: StockMovementFormData) => {
    DataService.post("/movements", data);
    toast.success("Stock movement saved successfully");
    callbackSubmit();
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
      <div className="grid gap-2">
        <Label htmlFor="productId">Product ID</Label>
        <Select
          onValueChange={(value) => setValue("productId", Number(value))}
          defaultValue={products[0]?.id.toString()}
        >
          <SelectTrigger className="w-full">
            <SelectValue placeholder="Select product" />
          </SelectTrigger>
          <SelectContent>
            {products.map((product) => (
              <SelectItem key={product.id} value={product.id.toString()}>
                {product.name}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        {errors.productId && (
          <p className="text-sm text-red-500">{errors.productId.message}</p>
        )}
      </div>

      <div className="grid gap-2">
        <Label htmlFor="date">Date</Label>
        <Input id="date" type="date" {...register("date")} />
        {errors.date && (
          <p className="text-sm text-red-500">{errors.date.message}</p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-2">
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
          <Label htmlFor="quantity">Type</Label>
          <Select
            onValueChange={(value) =>
              setValue("type", value as "ENTRY" | "EXIT")
            }
            defaultValue={"ENTRY"}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="Select movement type" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="ENTRY">Entry</SelectItem>
              <SelectItem value="EXIT">Exit</SelectItem>
            </SelectContent>
          </Select>
          {errors.type && (
            <p className="text-sm text-red-500">{errors.type.message}</p>
          )}
        </div>
      </div>
      <Button type="submit">{"Create"}</Button>
    </form>
  );
}
