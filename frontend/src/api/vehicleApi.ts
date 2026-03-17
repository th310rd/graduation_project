import { vehicleApi } from './client';
import type { CreateVehicleRequest, Vehicle } from '../types/vehicle';

export async function createVehicle(payload: CreateVehicleRequest) {
  const { data } = await vehicleApi.post('/vehicles', payload);
  return data as Vehicle;
}
