import { AppDataTable } from "@/components/atoms/AppDataTable";
import ProductFormPage from "./ProductFormPage";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import AppModal from "@/components/atoms/AppModal";
import { ProductDeletePage } from "./ProductDeletePage";
import { PlusIcon } from "lucide-react";

export default function ProductListPage() {
  const [openForm, setOpenForm] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);
  const [initialData, setInitialData] = useState<any>();
  const [refreshTrigger, setRefreshTrigger] = useState(false);

  const toggleForm = () => setOpenForm(!openForm);
  const toggleDelete = () => setOpenDelete(!openDelete);
  const toggleRefresh = () => setRefreshTrigger(!refreshTrigger);

  return (
    <div className="relative mx-5">
      <div className="container mx-auto space-y-5">
        <div className="flex items-center justify-between mb-4">
          <h1 className="text-xl font-semibold">Product</h1>
          <div className="flex gap-2">
            <Button onClick={toggleForm}>
              <PlusIcon />
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
              accessorKey: "name",
              header: "Name",
            },
            {
              accessorKey: "description",
              header: "Description",
            },
            {
              accessorKey: "quantity",
              header: "Quantity",
            },
            {
              accessorKey: "unitPrice",
              header: "Unit Price",
              cell: (info) => `R$ ${
                parseFloat(info.getValue<string>()).toFixed(2)
              }`,
            },
          ]}
          urlEndpoint="/products"
          handleEdit={(data) => {
            setInitialData(data);
            toggleForm();
          }}
          handleDelete={(id) => {
            setInitialData({ id });
            toggleDelete();
          }}
          refreshTrigger={refreshTrigger}
        />
      </div>
      <AppModal open={openForm} toggle={toggleForm}>
        <ProductFormPage
          callbackSubmit={() => {
            toggleForm();
            toggleRefresh();
          }}
          initialData={initialData}
        />
      </AppModal>
      <AppModal
        open={openDelete}
        toggle={toggleDelete}
        title="Delete Product"
        description="Are you sure you want to delete this product? This action cannot be undone."
      >
        <ProductDeletePage
          id={initialData?.id}
          onDelete={() => {
            toggleDelete();
            toggleRefresh();
          }}
        />
      </AppModal>
    </div>
  );
}