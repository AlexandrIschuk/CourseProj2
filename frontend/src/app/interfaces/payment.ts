import {Drive} from './drive';

export interface Payment{
  paymentId: number;
  drive: Drive;
  paymentAmount: number;
  paymentMethod: string;
  paymentDate: Date;
  paymentStatus: string;
}
