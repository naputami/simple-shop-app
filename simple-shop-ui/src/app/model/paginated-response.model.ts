export interface PaginatedApiResponse<T> {
    status: string;
    code: number;
    message: string;
    data: T[];
    pageNo: number;
    pageSize: number;
    totalElements: number;
    totalPages: number;
  }