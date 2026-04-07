import {Car} from './car';
import {Tariff} from './tariff';

export interface Drive{
  driveId: number;
  userId: number;
  car: Car;
  tariff: Tariff;
  startRental: Date;
  endRental: Date;
  totalDistance: number;
  totalAmount: number;
  driveStatus: string;
}
