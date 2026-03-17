export type Vehicle = {
  id: string;
  ownerId: string;
  vin: string;
  licensePlate: string;
  brand: string;
  model: string;
  year: number;
  color: string;
  mileage: number;
  insuranceExpiry: string;
  technicalInspectionExpiry: string;
  status: 'AVAILABLE' | 'RENTED' | 'BLOCKED';
};

export type CreateVehicleRequest = {
  ownerId: string;
  vin: string;
  licensePlate: string;
  brand: string;
  model: string;
  year: number;
  color: string;
  mileage: number;
  insuranceExpiry: string;
  technicalInspectionExpiry: string;
  status: 'AVAILABLE' | 'RENTED' | 'BLOCKED';
};
