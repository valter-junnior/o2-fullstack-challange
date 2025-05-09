import { AppTable } from "@/components/atoms/AppTable";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
} from "@/components/ui/card";

export default function DashboardTopProducts({ topProductExits }: any) {
  return (
    <Card className="@container/card col-span-full">
      <CardHeader className="relative">
        <CardDescription>Top 5 products exits</CardDescription>
      </CardHeader>
      <CardContent>
        <AppTable
          columns={[
            {
              accessorKey: "productId",
              header: "ID",
            },
            {
              accessorKey: "productName",
              header: "Product",
            },
            {
              accessorKey: "totalQuantity",
              header: "Quantity",
            },
            {
              accessorKey: "totalPrice",
              header: "Total",
              cell: (info) => `R$ ${info.getValue().toFixed(2)}`,
            },
          ]}
          data={topProductExits}
        />
      </CardContent>
    </Card>
  );
}
