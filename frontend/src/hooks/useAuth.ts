import { useMemo } from 'react';

export function useAuth() {
  const token = localStorage.getItem('token');
  const user = useMemo(() => {
    const raw = localStorage.getItem('user');
    return raw ? JSON.parse(raw) : null;
  }, [token]);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
  };

  return { isAuthenticated: !!token, token, user, logout };
}
