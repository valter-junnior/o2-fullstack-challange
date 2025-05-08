import { SidebarInset, SidebarProvider } from "../ui/sidebar";
import { Toaster } from "../ui/sonner";
import { AppHeader } from "./AppHeader";
import { AppSidebar } from "./AppSidebar";

export default function AppLayout({ children }: { children: React.ReactNode }) {
  return (
    <SidebarProvider>
      <AppSidebar variant="inset" />
      <SidebarInset>
        <AppHeader />
        <div className="flex flex-1 flex-col">
          <div className="@container/main flex flex-1 flex-col gap-2">
            <div className="flex flex-col gap-4 py-4 md:gap-6 md:py-6">
              {children}
            </div>
          </div>
        </div>
        <Toaster />
      </SidebarInset>
    </SidebarProvider>
  );
}
