import type { Vehicle } from '../types/vehicle';

export default function VehicleCard({ vehicle }: { vehicle: Vehicle }) {
  return (
    <div className="bg-white border rounded-lg p-3 text-sm">
      <p className="font-medium">{vehicle.brand} {vehicle.model} ({vehicle.year})</p>
      <p>Plate: {vehicle.licensePlate}</p>
      <p>Status: {vehicle.status}</p>
      <p className="text-slate-500">VIN: {vehicle.vin}</p>
    </div>
  );
}
