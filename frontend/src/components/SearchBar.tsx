import { FormEvent, useState } from 'react';

export default function SearchBar({ onSearch }: { onSearch: (city: string) => void }) {
  const [city, setCity] = useState('');

  const submit = (e: FormEvent) => {
    e.preventDefault();
    onSearch(city);
  };

  return (
    <form onSubmit={submit} className="flex gap-2 mb-6">
      <input
        className="w-full border rounded-lg px-3 py-2"
        placeholder="Search by city (e.g. Tashkent)"
        value={city}
        onChange={(e) => setCity(e.target.value)}
      />
      <button className="bg-indigo-600 text-white px-4 rounded-lg">Search</button>
    </form>
  );
}
