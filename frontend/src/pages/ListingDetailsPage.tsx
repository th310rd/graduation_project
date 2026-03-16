import { Link, useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { getListing } from '../api/listingApi';

export default function ListingDetailsPage() {
  const { id } = useParams();

  const { data, isLoading } = useQuery({
    queryKey: ['listing', id],
    queryFn: () => getListing(id as string),
    enabled: !!id,
  });

  if (isLoading) return <div className="p-4">Loading...</div>;
  if (!data) return <div className="p-4">Listing not found.</div>;

  return (
    <div className="max-w-4xl mx-auto p-4">
      <div className="grid md:grid-cols-2 gap-6">
        <img className="w-full h-72 object-cover rounded-xl" src={data.photos?.[0]?.photoUrl || 'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?auto=format&fit=crop&w=1200&q=80'} alt={data.title} />
        <div>
          <h1 className="text-2xl font-semibold">{data.title}</h1>
          <p className="text-slate-500">{data.city}</p>
          <p className="mt-3">{data.description}</p>
          <p className="mt-3 font-medium">Price: ${data.pricePerDay} / day</p>
          <p>Deposit: ${data.depositAmount}</p>
          {data.rules && (
            <div className="mt-4 text-sm bg-white border rounded-lg p-3">
              <p>Min driver age: {data.rules.minDriverAge}</p>
              <p>Rental days: {data.rules.minRentalDays} - {data.rules.maxRentalDays}</p>
              <p>Smoking: {data.rules.smokingAllowed ? 'Allowed' : 'Not allowed'}</p>
            </div>
          )}
          <Link to={`/rent/${data.id}?vehicleId=${data.vehicleId}`} className="inline-block mt-4 bg-indigo-600 text-white px-4 py-2 rounded">Rent This Car</Link>
        </div>
      </div>
    </div>
  );
}
