export interface StandardResponse<T> {
    status: string;
    code: number;
    message: string;
    data: T | null 
}