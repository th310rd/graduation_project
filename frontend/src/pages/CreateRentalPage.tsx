import { FormEvent, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { createRental } from '../api/rentalApi';

export default function CreateRentalPage() {
  const [searchParams] = useSearchParams();
  const vehicleId = searchParams.get('vehicleId') || '';

  const [form, setForm] = useState({
    guestId: localStorage.getItem('userId') || '',
    startDateTime: '',
    endDateTime: '',
    pickupLocation: 'Tashkent Center',
  });
  const [result, setResult] = useState<string>('');

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    const rental = await createRental({
      vehicleId,
      guestId: form.guestId,
      startDateTime: form.startDateTime,
      endDateTime: form.endDateTime,
    });
    setResult(`Rental created: ${rental.id}`);
  };

  return (
    <div className="max-w-xl mx-auto mt-8 bg-white border rounded-xl p-6">
      <h1 className="text-xl font-semibold mb-4">Create Rental</h1>
      <form onSubmit={submit} className="space-y-3">
        <input className="w-full border rounded px-3 py-2" value={vehicleId} readOnly placeholder="Vehicle ID" />
        <input className="w-full border rounded px-3 py-2" placeholder="Guest ID" value={form.guestId} onChange={(e) => setForm({ ...form, guestId: e.target.value })} />
        <input className="w-full border rounded px-3 py-2" type="datetime-local" value={form.startDateTime} onChange={(e) => setForm({ ...form, startDateTime: e.target.value })} />
        <input className="w-full border rounded px-3 py-2" type="datetime-local" value={form.endDateTime} onChange={(e) => setForm({ ...form, endDateTime: e.target.value })} />
        <input className="w-full border rounded px-3 py-2" placeholder="Pickup Location" value={form.pickupLocation} onChange={(e) => setForm({ ...form, pickupLocation: e.target.value })} />
        <button className="w-full bg-indigo-600 text-white py-2 rounded">Submit Rental</button>
      </form>
      {result && <p className="mt-3 text-green-700">{result}</p>}
    </div>
  );
}
