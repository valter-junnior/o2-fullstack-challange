import "./App.css";
import { Routes, Route, Navigate } from "react-router-dom";
import Dashboard from "./pages/Dashboard/Dashboard";
import ProductListPage from "./pages/Products/ProductListPage";
import AppLayout from "./components/layout/AppLayout";
import StockMovementListPage from "./pages/StockMovements/StockMovementListPage";

export default function App() {
  return (
    <AppLayout>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/products" element={<ProductListPage />} />
        <Route path="/stock-movements" element={<StockMovementListPage />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </AppLayout>
  );
}
