import {
  Card,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

export default function DashboardDataCards({totalPriceProducts, totalQuantityProducts, totalQuantityExitMovements} : any) {
    return (
        <>
        <Card className="@container/card">
        <CardHeader className="relative">
          <CardDescription>Total value stock</CardDescription>
          <CardTitle className="@[250px]/card:text-3xl text-2xl font-semibold tabular-nums">
            R$ {totalPriceProducts.toFixed(2)}
          </CardTitle>
        </CardHeader>
      </Card>
      <Card className="@container/card">
        <CardHeader className="relative">
          <CardDescription>Total quantity stock</CardDescription>
          <CardTitle className="@[250px]/card:text-3xl text-2xl font-semibold tabular-nums">
            {totalQuantityProducts.toFixed(2)}
          </CardTitle>
        </CardHeader>
      </Card>
      <Card className="@container/card">
        <CardHeader className="relative">
          <CardDescription>Total quantity exit</CardDescription>
          <CardTitle className="@[250px]/card:text-3xl text-2xl font-semibold tabular-nums">
            {totalQuantityExitMovements.toFixed(2)}
          </CardTitle>
        </CardHeader>
      </Card>
      </>
    )
}