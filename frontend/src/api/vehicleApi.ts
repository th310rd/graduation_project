import { api } from './client';
import type { CreateVehicleRequest, Vehicle } from '../types/vehicle';

export async function createVehicle(payload: CreateVehicleRequest) {
  const { data } = await api.post('/vehicle-service/vehicles', payload);
  return data as Vehicle;
}

export async function getVehicle(id: string) {
  const { data } = await api.get(`/vehicle-service/vehicles/${id}`);
  return data as Vehicle;
}
