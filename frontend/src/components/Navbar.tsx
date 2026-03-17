import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export default function Navbar() {
  const { isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <nav className="bg-white border-b sticky top-0 z-10">
      <div className="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
        <Link to="/" className="font-semibold text-lg text-indigo-600">P2P Car Demo</Link>
        <div className="flex gap-4 items-center">
          <Link to="/" className="text-sm">Home</Link>
          <Link to="/create-listing" className="text-sm">Create Listing</Link>
          {!isAuthenticated ? (
            <>
              <Link to="/login" className="text-sm">Login</Link>
              <Link to="/register" className="text-sm">Register</Link>
            </>
          ) : (
            <button
              className="text-sm bg-slate-900 text-white px-3 py-1 rounded"
              onClick={() => {
                logout();
                navigate('/login');
              }}
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}
