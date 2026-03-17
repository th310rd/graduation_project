import { userApi } from './client';

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
  const { data } = await userApi.post('/auth/login', payload);
  return data as { accessToken: string };
}

export async function register(payload: RegisterRequest) {
  const { data } = await userApi.post('/auth/register', payload);
  return data;
}
