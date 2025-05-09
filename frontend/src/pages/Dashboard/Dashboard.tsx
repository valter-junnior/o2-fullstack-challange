import { DataService } from "@/services/dataService";
import { useEffect, useState } from "react";
import DashboardDataCards from "./DashboardDataCards";
import DashboardTopProducts from "./DashboardTopProducts";
import DashboardExitChart from "./DashbordExitChart";

export default function Dashboard() {
  const [data, setData] = useState<any>({
    totalPriceProducts: 0,
    totalQuantityProducts: 0,
    totalQuantityExitMovements: 0,
    topProductExits: [],
  });

  useEffect(() => {
    DataService.get("/reports/dashboard", {}).then((res) => {
      setData(res);
    });
  }, []);

  return (
    <div className="*:data-[slot=card]:shadow-xs @xl/main:grid-cols-3 @5xl/main:grid-cols-3 grid grid-cols-1 gap-4 px-4 *:data-[slot=card]:from-primary/5 *:data-[slot=card]:to-card dark:*:data-[slot=card]:bg-card lg:px-6">
      <DashboardDataCards
        totalPriceProducts={data.totalPriceProducts}
        totalQuantityProducts={data.totalQuantityProducts}
        totalQuantityExitMovements={data.totalQuantityExitMovements}
      />
      <DashboardTopProducts topProductExits={data.topProductExits} />
      <DashboardExitChart />
    </div>
  );
}
