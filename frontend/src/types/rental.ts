export type CreateRentalRequest = {
  vehicleId: string;
  guestId: string;
  startDateTime: string;
  endDateTime: string;
};

export type Rental = {
  id: string;
  vehicleId: string;
  guestId: string;
  startDateTime: string;
  endDateTime: string;
  status: string;
};
