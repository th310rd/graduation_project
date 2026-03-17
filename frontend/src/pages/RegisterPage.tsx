import { FormEvent, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../api/authApi';

export default function RegisterPage() {
  const [firstName, setFirstName] = useState('Ali');
  const [lastName, setLastName] = useState('Karimov');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phone, setPhone] = useState('');
  const navigate = useNavigate();

  const submit = async (e: FormEvent) => {
    e.preventDefault();
    await register({
      firstName,
      lastName,
      email,
      phoneNumber: phone,
      password,
      dateOfBirth: '1995-01-01',
      roles: ['ROLE_HOST', 'ROLE_RENTER'],
    });
    navigate('/login');
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-6 rounded-xl border">
      <h1 className="text-xl font-semibold mb-4">Register</h1>
      <form onSubmit={submit} className="space-y-3">
        <input className="w-full border rounded px-3 py-2" placeholder="First Name" value={firstName} onChange={(e) => setFirstName(e.target.value)} />
        <input className="w-full border rounded px-3 py-2" placeholder="Last Name" value={lastName} onChange={(e) => setLastName(e.target.value)} />
        <input className="w-full border rounded px-3 py-2" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input className="w-full border rounded px-3 py-2" type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input className="w-full border rounded px-3 py-2" placeholder="Phone" value={phone} onChange={(e) => setPhone(e.target.value)} />
        <button className="w-full bg-indigo-600 text-white py-2 rounded">Register</button>
      </form>
    </div>
  );
}
