import api from "@/settings/api";

export interface Paginable<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  pageable: {
    pageNumber: number;
    pageSize: number;
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
}

export const DataService = {
  async get(url: string, params: any) {
    const response = await api.get(url, { params });

    return response.data;
  },

  async post(url: string, data: any) {
    const response = await api.post(url, data);
    return response.data;
  },

  async put(url: string, data: any) {
    const response = await api.put(url, data);
    return response.data;
  },

  async delete(url: string) {
    const response = await api.delete(url);
    return response.data;
  }
};
