import axios, { AxiosInstance } from 'axios';

function withAuth(instance: AxiosInstance) {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  });
  return instance;
}

export const userApi = withAuth(axios.create({
  baseURL: import.meta.env.VITE_USER_API_URL || 'http://localhost:8081',
}));

export const vehicleApi = withAuth(axios.create({
  baseURL: import.meta.env.VITE_VEHICLE_API_URL || 'http://localhost:8082',
}));

export const listingApi = withAuth(axios.create({
  baseURL: import.meta.env.VITE_LISTING_API_URL || 'http://localhost:8084',
}));

export const rentalApi = withAuth(axios.create({
  baseURL: import.meta.env.VITE_RENTAL_API_URL || 'http://localhost:8083',
}));
