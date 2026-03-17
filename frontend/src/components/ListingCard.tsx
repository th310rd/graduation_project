import { Link } from 'react-router-dom';
import type { ListingSummary } from '../types/listing';

export default function ListingCard({ listing }: { listing: ListingSummary }) {
  return (
    <div className="bg-white rounded-xl shadow-sm border p-4">
      <img
        className="h-40 w-full object-cover rounded-md mb-3"
        src="https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80"
        alt={listing.title}
      />
      <h3 className="font-semibold">{listing.title}</h3>
      <p className="text-sm text-slate-500">{listing.city}</p>
      <p className="mt-2 font-medium">${listing.pricePerDay} / day</p>
      <Link to={`/listings/${listing.id}`} className="inline-block mt-3 text-indigo-600 text-sm">View Details</Link>
    </div>
  );
}
