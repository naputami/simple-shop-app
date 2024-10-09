export interface Item {
  id: string;
  name: string;
  code: string;
  stock: number;
  price: number;
  lastRestockDate: string;
  isAvailable: boolean;
}
