import { listingApi } from './client';
import type { Listing, ListingRules, ListingSummary } from '../types/listing';

export async function searchListings(city?: string) {
  const { data } = await listingApi.get('/listings/search', {
    params: city ? { city } : undefined,
  });
  return data as ListingSummary[];
}

export async function getListing(id: string) {
  const { data } = await listingApi.get(`/listings/${id}`);
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
  const { data } = await listingApi.post('/listings', payload);
  return data as Listing;
}

export async function addListingPhoto(listingId: string, payload: { photoUrl: string; sortOrder: number }) {
  const { data } = await listingApi.post(`/listings/${listingId}/photos`, payload);
  return data;
}

export async function updateListingRules(listingId: string, payload: ListingRules) {
  const { data } = await listingApi.put(`/listings/${listingId}/rules`, payload);
  return data;
}
