export type ListingPhoto = {
  id: string;
  photoUrl: string;
  sortOrder: number;
};

export type ListingRules = {
  minDriverAge: number;
  minRentalDays: number;
  maxRentalDays: number;
  smokingAllowed: boolean;
  petsAllowed: boolean;
  internationalTravelAllowed: boolean;
};

export type Listing = {
  id: string;
  vehicleId: string;
  ownerId: string;
  title: string;
  description: string;
  city: string;
  pricePerDay: number;
  depositAmount: number;
  instantBooking: boolean;
  deliveryAvailable: boolean;
  photos: ListingPhoto[];
  rules?: ListingRules;
};

export type ListingSummary = {
  id: string;
  vehicleId: string;
  title: string;
  city: string;
  pricePerDay: number;
  deliveryAvailable: boolean;
};
