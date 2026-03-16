import { FormEvent, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import SearchBar from '../components/SearchBar';
import ListingCard from '../components/ListingCard';
import VehicleCard from '../components/VehicleCard';
import { searchListings } from '../api/listingApi';
import { createVehicle } from '../api/vehicleApi';
import type { Vehicle } from '../types/vehicle';

export default function HomePage() {
  const [city, setCity] = useState('');
  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [vehicleForm, setVehicleForm] = useState({
    ownerId: localStorage.getItem('userId') || '',
    vin: '',
    licensePlate: '',
    brand: '',
    model: '',
    year: 2022,
    color: 'White',
    mileage: 10000,
    insuranceExpiry: '2027-01-01',
    technicalInspectionExpiry: '2027-01-01',
    status: 'AVAILABLE' as const,
  });

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['listings', city],
    queryFn: () => searchListings(city || undefined),
  });

  const createVehicleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const created = await createVehicle(vehicleForm);
    setVehicle(created);
  };

  return (
    <div className="max-w-6xl mx-auto p-4">
      <h1 className="text-2xl font-semibold mb-4">Marketplace Listings</h1>
      <SearchBar onSearch={(nextCity) => { setCity(nextCity); refetch(); }} />

      <div className="grid lg:grid-cols-2 gap-6 mb-8">
        <div className="bg-white border rounded-xl p-4">
          <h2 className="font-semibold mb-2">Create Vehicle (Demo Flow)</h2>
          <form onSubmit={createVehicleSubmit} className="grid grid-cols-2 gap-2 text-sm">
            <input className="border rounded p-2 col-span-2" placeholder="Owner ID" value={vehicleForm.ownerId} onChange={(e) => setVehicleForm({ ...vehicleForm, ownerId: e.target.value })} />
            <input className="border rounded p-2" placeholder="VIN" value={vehicleForm.vin} onChange={(e) => setVehicleForm({ ...vehicleForm, vin: e.target.value })} />
            <input className="border rounded p-2" placeholder="Plate" value={vehicleForm.licensePlate} onChange={(e) => setVehicleForm({ ...vehicleForm, licensePlate: e.target.value })} />
            <input className="border rounded p-2" placeholder="Brand" value={vehicleForm.brand} onChange={(e) => setVehicleForm({ ...vehicleForm, brand: e.target.value })} />
            <input className="border rounded p-2" placeholder="Model" value={vehicleForm.model} onChange={(e) => setVehicleForm({ ...vehicleForm, model: e.target.value })} />
            <button className="col-span-2 bg-slate-900 text-white rounded p-2">Create Vehicle</button>
          </form>
          {vehicle && <div className="mt-3"><VehicleCard vehicle={vehicle} /></div>}
        </div>
      </div>

      {isLoading ? <p>Loading...</p> : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
          {(data || []).map((listing) => <ListingCard key={listing.id} listing={listing} />)}
        </div>
      )}
    </div>
  );
}
