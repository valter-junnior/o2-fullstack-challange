import {
  type ColumnDef,
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { useEffect, useState } from "react";
import { DataService, type Paginable } from "@/services/dataService";
import { Button } from "../ui/button";
import { PencilIcon, TrashIcon } from "lucide-react";

interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[];
  urlEndpoint: string;
  refreshTrigger?: any;
  showFilters?: boolean;
  handleEdit?: (data: any) => void;
  handleDelete?: (id: any) => void;
}

export function AppDataTable<TData, TValue>({
  columns,
  urlEndpoint,
  refreshTrigger,
  showFilters,
  handleEdit,
  handleDelete,
}: DataTableProps<TData, TValue>) {
  const [response, setResponse] = useState<Paginable<any>>();
  const [data, setData] = useState<any[]>([]);
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const [startAt, setStartAt] = useState<string>("");
  const [endAt, setEndAt] = useState<string>("");

  const fetchData = () => {
    DataService.get(urlEndpoint, { page, size: pageSize, startAt, endAt })
      .then((res) => {
        setResponse(res);
        setData(res.content);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  };

  useEffect(() => {
    fetchData();
  }, [page, pageSize, refreshTrigger]);

  const fullColumns = [
    ...columns,
    ...(handleEdit || handleDelete
      ? [
          {
            id: "actions",
            header: "Ações",
            cell: ({ row }: any) => (
              <div className="flex gap-2">
                {handleEdit && (
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => handleEdit(row.original)}
                  >
                    <PencilIcon className="w-4 h-4 mr-1" />
                    Editar
                  </Button>
                )}
                {handleDelete && (
                  <Button
                    size="sm"
                    variant="destructive"
                    onClick={() => handleDelete(row.original.id)}
                  >
                    <TrashIcon className="w-4 h-4 mr-1" />
                    Deletar
                  </Button>
                )}
              </div>
            ),
          },
        ]
      : []),
  ];

  const table = useReactTable({
    data,
    columns: fullColumns,
    getCoreRowModel: getCoreRowModel(),
    manualPagination: true,
    pageCount: response?.totalPages,
    state: {
      pagination: {
        pageIndex: page,
        pageSize: pageSize,
      },
    },
    onPaginationChange: (updater) => {
      const newPagination =
        typeof updater === "function"
          ? updater({ pageIndex: page, pageSize })
          : updater;
      setPage(newPagination.pageIndex);
      setPageSize(newPagination.pageSize);
    },
  });

  return (
    <div>
      <div
        className={`flex gap-4 mb-4 transition-all duration-300 ${
          showFilters
            ? "max-h-40 opacity-100"
            : "max-h-0 opacity-0 overflow-hidden"
        }`}
      >
        <div>
          <label className="block text-sm font-medium">Start at</label>
          <input
            type="date"
            value={startAt}
            onChange={(e) => setStartAt(e.target.value)}
            className="border rounded px-2 py-1 w-full"
          />
        </div>
        <div>
          <label className="block text-sm font-medium">End at</label>
          <input
            type="date"
            value={endAt}
            onChange={(e) => setEndAt(e.target.value)}
            className="border rounded px-2 py-1 w-full"
          />
        </div>
        <Button onClick={fetchData} className="self-end">
          Filtrar
        </Button>
      </div>

      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder
                        ? null
                        : flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow
                  key={row.id}
                  data-state={row.getIsSelected() && "selected"}
                >
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}
                    </TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell
                  colSpan={fullColumns.length}
                  className="h-24 text-center"
                >
                  No results.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex items-center justify-between space-x-4 py-4">
        <div>
          Página {(response?.pageable?.pageNumber ?? 0) + 1} de{" "}
          {response?.totalPages}
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
            disabled={page === 0}
            className="cursor-pointer"
          >
            Anterior
          </Button>

          {Array.from({ length: Math.min(response?.totalPages ?? 0, 5) }).map(
            (_, idx) => {
              const totalPages = response?.totalPages ?? 0;
              let startPage = Math.max(0, page - 2);
              let endPage = startPage + 4;

              if (endPage >= totalPages) {
                endPage = totalPages - 1;
                startPage = Math.max(0, endPage - 4);
              }

              const pageNumber = startPage + idx;

              return (
                <Button
                  key={pageNumber}
                  variant="outline"
                  size="sm"
                  onClick={() => setPage(pageNumber)}
                  disabled={page === pageNumber}
                  className="cursor-pointer"
                >
                  {pageNumber + 1}
                </Button>
              );
            }
          )}

          <Button
            variant="outline"
            size="sm"
            onClick={() =>
              setPage((prev) =>
                prev < (response?.totalPages ?? 0) - 1 ? prev + 1 : prev
              )
            }
            disabled={page >= (response?.totalPages ?? 0) - 1}
            className="cursor-pointer"
          >
            Próxima
          </Button>

          <select
            value={pageSize}
            onChange={(e) => setPageSize(Number(e.target.value))}
            className="border rounded px-2 py-1 cursor-pointer"
          >
            {[10, 20, 50].map((size) => (
              <option key={size} value={size}>
                {size} por página
              </option>
            ))}
          </select>
        </div>
      </div>
    </div>
  );
}
