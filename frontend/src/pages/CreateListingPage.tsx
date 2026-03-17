import { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createListing } from '../api/listingApi';

export default function CreateListingPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    vehicleId: '',
    title: '',
    description: '',
    city: 'Tashkent',
    pricePerDay: 120,
    depositAmount: 500,
    instantBooking: true,
    deliveryAvailable: true,
  });

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    const listing = await createListing(form);
    navigate(`/listings/${listing.id}`);
  };

  return (
    <div className="max-w-xl mx-auto mt-8 bg-white border rounded-xl p-6">
      <h1 className="text-xl font-semibold mb-4">Create Listing</h1>
      <form onSubmit={submit} className="space-y-3">
        {['vehicleId', 'title', 'description', 'city'].map((field) => (
          <input
            key={field}
            className="w-full border rounded px-3 py-2"
            placeholder={field}
            value={(form as any)[field]}
            onChange={(e) => setForm({ ...form, [field]: e.target.value })}
          />
        ))}
        <input className="w-full border rounded px-3 py-2" type="number" placeholder="pricePerDay" value={form.pricePerDay} onChange={(e) => setForm({ ...form, pricePerDay: Number(e.target.value) })} />
        <input className="w-full border rounded px-3 py-2" type="number" placeholder="depositAmount" value={form.depositAmount} onChange={(e) => setForm({ ...form, depositAmount: Number(e.target.value) })} />
        <label className="flex items-center gap-2"><input type="checkbox" checked={form.instantBooking} onChange={(e) => setForm({ ...form, instantBooking: e.target.checked })} /> Instant booking</label>
        <label className="flex items-center gap-2"><input type="checkbox" checked={form.deliveryAvailable} onChange={(e) => setForm({ ...form, deliveryAvailable: e.target.checked })} /> Delivery available</label>
        <button className="w-full bg-indigo-600 text-white py-2 rounded">Create Listing</button>
      </form>
    </div>
  );
}
