import { useEffect, useState } from "react";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
} from "@/components/ui/card";
import {
  XAxis,
  CartesianGrid,
  Area,
  AreaChart,
} from "recharts";
import { DataService } from "@/services/dataService";
import moment from 'moment';
import { ChartContainer, ChartTooltip, ChartTooltipContent, type ChartConfig } from "@/components/ui/chart";
import { Select, SelectTrigger } from "@radix-ui/react-select";
import { SelectContent, SelectItem, SelectValue } from "@/components/ui/select";
import { ToggleGroup, ToggleGroupItem } from "@/components/ui/toggle-group";

interface DashboardExitChartProps {
  date: string;
  totalSales: number;
}

export default function DashboardExitChart() {
  const [data, setData] = useState<DashboardExitChartProps[]>([]);
  const [timeRange, setTimeRange] = useState("90");

  const chartConfig = {
    total: {
      label: "Total (R$)",
      color: "hsl(var(--chart-1))",
    },
  } satisfies ChartConfig

  useEffect(() => {
    const endDate = moment().format('YYYY-MM-DD');
    const startDate = moment().subtract(parseInt(timeRange), 'days').format('YYYY-MM-DD');

    DataService.get("/reports/sales", {
      startDate,
      endDate,
    }).then((res) => {
      setData(res);
    });
  }, [timeRange]);

  return (
    <Card className="@container/card col-span-full">
      <CardHeader className="relative">
        <CardDescription>Exit by period</CardDescription>
        <div className="absolute right-4 top-4">
          <ToggleGroup
            type="single"
            value={timeRange}
            onValueChange={(value) => {
                if (value) setTimeRange(value);
              }}
            variant="outline"
            className="@[767px]/card:flex hidden"
          >
            <ToggleGroupItem value="90" className="h-8 px-2.5 cursor-pointer">
              Last 3 months
            </ToggleGroupItem>
            <ToggleGroupItem value="30" className="h-8 px-2.5 cursor-pointer">
              Last 30 days
            </ToggleGroupItem>
            <ToggleGroupItem value="7" className="h-8 px-2.5 cursor-pointer">
              Last 7 days
            </ToggleGroupItem>
          </ToggleGroup>
          <Select value={timeRange} onValueChange={setTimeRange}>
            <SelectTrigger
              className="@[767px]/card:hidden flex w-40"
              aria-label="Select a value"
            >
              <SelectValue placeholder="Last 3 months" />
            </SelectTrigger>
            <SelectContent className="rounded-xl">
              <SelectItem value="90" className="rounded-lg">
                Last 3 months
              </SelectItem>
              <SelectItem value="30" className="rounded-lg">
                Last 30 days
              </SelectItem>
              <SelectItem value="7" className="rounded-lg">
                Last 7 days
              </SelectItem>
            </SelectContent>
          </Select>
        </div>
      </CardHeader>
      <CardContent className="px-2 pt-4 sm:px-6 sm:pt-6">
        <ChartContainer
          config={chartConfig}
          className="aspect-auto h-[250px] w-full"
        >
          <AreaChart data={data}>
            <defs>
              <linearGradient id="fillExit" x1="0" y1="0" x2="0" y2="1">
                <stop
                  offset="5%"
                  stopColor="var(--color-primary)"
                  stopOpacity={1.0}
                />
                <stop
                  offset="95%"
                  stopColor="var(--color-primary)"
                  stopOpacity={0.5}
                />
              </linearGradient>
            </defs>
            <CartesianGrid vertical={false} />
            <XAxis
              dataKey="date"
              tickLine={false}
              axisLine={false}
              tickMargin={8}
              minTickGap={32}
              tickFormatter={(value) => {
                const date = new Date(value)
                return date.toLocaleDateString("en-US", {
                  month: "short",
                  day: "numeric",
                })
              }}
            />
            <ChartTooltip
              cursor={false}
              content={
                <ChartTooltipContent
                  labelFormatter={(value: any) => {
                    return new Date(value).toLocaleDateString("en-US", {
                      month: "short",
                      day: "numeric",
                    })
                  }}
                  indicator="dot"
                />
              }
            />
            <Area
              dataKey="total"
              type="natural"
              fill="url(#fillExit)"
              stroke="var(--color-primary)"
              stackId="a"
            />
          </AreaChart>
        </ChartContainer>
      </CardContent>
    </Card>
  );
}