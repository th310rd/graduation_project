# Frontend Demo UI (React + TypeScript + Vite)

This frontend demonstrates the implemented workflow for the currently supported backend services:

1. Register
2. Login
3. Create vehicle
4. Create listing
5. Browse/search listings
6. View listing details
7. Create rental

## Tech

- React + TypeScript
- Vite
- Tailwind CSS
- Axios
- React Router
- React Query

## Setup

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

App default URL:
- http://localhost:5173

## Backend URLs

Set service URLs in `.env`:

- `VITE_USER_API_URL` (default `http://localhost:8081`)
- `VITE_VEHICLE_API_URL` (default `http://localhost:8082`)
- `VITE_LISTING_API_URL` (default `http://localhost:8084`)
- `VITE_RENTAL_API_URL` (default `http://localhost:8083`)
