import { api } from './client';

export type LoginRequest = { email: string; password: string };
export type RegisterRequest = {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  dateOfBirth: string;
  password: string;
  roles: string[];
};

export async function login(payload: LoginRequest) {
  const { data } = await api.post('/user-service/auth/login', payload);
  return data as { accessToken: string };
}

export async function register(payload: RegisterRequest) {
  const { data } = await api.post('/user-service/auth/register', payload);
  return data;
}
