import { AppDataTable } from "@/components/atoms/AppDataTable";
import StockMovementFormPage from "./StockMovementFormPage";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import AppModal from "@/components/atoms/AppModal";
import { FilterIcon, PlusIcon } from "lucide-react";
import moment from 'moment';

export default function StockMovementListPage() {
  const [openForm, setOpenForm] = useState(false);
  const [refreshTrigger, setRefreshTrigger] = useState(false);
  const [showFilters, setShowFilters] = useState(false);

  const toggleForm = () => setOpenForm(!openForm);
  const toggleRefresh = () => setRefreshTrigger(!refreshTrigger);

  return (
    <div className="relative mx-5">
      <div className="container mx-auto space-y-5">
        <div className="flex items-center justify-between mb-4">
          <h1 className="text-xl font-semibold">Stock Movement</h1>
          <div className="flex gap-2">
            <Button onClick={toggleForm}>
              <PlusIcon />
            </Button>

            <Button
              variant="outline"
              onClick={() => setShowFilters(!showFilters)}
            >
              <FilterIcon className="w-4 h-4 mr-1" />
            </Button>
          </div>
        </div>
        <AppDataTable
          columns={[
            {
              accessorKey: "id",
              header: "ID",
            },
            {
              accessorKey: "product",
              header: "Product Name",
              cell: (info) => {
                return info.row.original.product?.name || "N/A";
              },
            },
            {
              accessorKey: "type",
              header: "Type",
              cell: (info) => (
                <span
                  className={
                    info.row.original.type === "ENTRY"
                      ? "bg-green-500 text-white px-2 py-1 rounded-md"
                      : "bg-red-500 text-white px-2 py-1 rounded-md"
                  }
                >
                  {info.row.original.type}
                </span>
              ),
            },
            {
              accessorKey: "quantity",
              header: "Quantity",
            },
            {
              accessorKey: "date",
              header: "Date",
              cell: (info) => {
                if (info.row.original.date) {
                  return moment(info.row.original.date).format("dd/MM/yyyy");
                }

                return "N/A";
              },
            },
          ]}
          urlEndpoint="/movements"
          refreshTrigger={refreshTrigger}
          showFilters={showFilters}
        />
      </div>
      <AppModal open={openForm} toggle={toggleForm}>
        <StockMovementFormPage
          callbackSubmit={() => {
            toggleForm();
            toggleRefresh();
          }}
        />
      </AppModal>
    </div>
  );
}
