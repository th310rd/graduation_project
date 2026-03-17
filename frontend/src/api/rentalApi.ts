import { rentalApi } from './client';
import type { CreateRentalRequest, Rental } from '../types/rental';

export async function createRental(payload: CreateRentalRequest) {
  const { data } = await rentalApi.post('/rentals', payload);
  return data as Rental;
}
