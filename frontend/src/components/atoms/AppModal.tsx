import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
  } from "@/components/ui/dialog";
  
export default function AppModal({
  open,
  toggle,
  children,
  title,
  description,
}: {
  open: boolean;
  toggle: () => void;
  children: React.ReactNode;
  title?: string;
  description?: string;
}) {
  return (
    <Dialog open={open} onOpenChange={toggle}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle> {title}</DialogTitle>
          <DialogDescription>{description}</DialogDescription>
        </DialogHeader>

        {children}
      </DialogContent>
    </Dialog>
  );
}
