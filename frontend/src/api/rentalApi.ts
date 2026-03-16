import { api } from './client';
import type { CreateRentalRequest, Rental } from '../types/rental';

export async function createRental(payload: CreateRentalRequest) {
  const { data } = await api.post('/rental-service/rentals', payload);
  return data as Rental;
}
