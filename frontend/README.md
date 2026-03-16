# Frontend Demo UI (React + TypeScript + Vite)

This frontend demonstrates the main presentation workflow for the P2P car rental platform:

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

Backend gateway base URL is configurable with:

- `VITE_API_URL` (default `http://localhost:8080`)

## API path conventions

Frontend calls gateway service prefixes:

- `/user-service`
- `/vehicle-service`
- `/listing-service`
- `/rental-service`
