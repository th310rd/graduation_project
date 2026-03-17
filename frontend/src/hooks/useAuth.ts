import { useMemo } from 'react';

export function useAuth() {
  const token = localStorage.getItem('token');
  const userId = useMemo(() => localStorage.getItem('userId'), [token]);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
  };

  return { isAuthenticated: !!token, token, userId, logout };
}
