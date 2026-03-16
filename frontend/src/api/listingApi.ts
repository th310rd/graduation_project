import { api } from './client';
import type { Listing, ListingRules, ListingSummary } from '../types/listing';

export async function searchListings(city?: string) {
  const { data } = await api.get('/listing-service/listings/search', {
    params: city ? { city } : undefined,
  });
  return data as ListingSummary[];
}

export async function getListing(id: string) {
  const { data } = await api.get(`/listing-service/listings/${id}`);
  return data as Listing;
}

export async function createListing(payload: {
  vehicleId: string;
  title: string;
  description: string;
  city: string;
  pricePerDay: number;
  depositAmount: number;
  instantBooking: boolean;
  deliveryAvailable: boolean;
}) {
  const { data } = await api.post('/listing-service/listings', payload);
  return data as Listing;
}

export async function addListingPhoto(listingId: string, payload: { photoUrl: string; sortOrder: number }) {
  const { data } = await api.post(`/listing-service/listings/${listingId}/photos`, payload);
  return data;
}

export async function updateListingRules(listingId: string, payload: ListingRules) {
  const { data } = await api.put(`/listing-service/listings/${listingId}/rules`, payload);
  return data;
}
