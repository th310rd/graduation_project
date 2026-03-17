# Online Car Rental System (Implemented Services Only)

This repository currently contains the **implemented and runnable** core services:

- `user-service`
- `vehicle-service`
- `rental-service`
- `listing-service` 
- `frontend part`

Non-implemented/placeholder services are excluded from Maven modules and Docker Compose runtime.

## Architecture

- Microservices with independent PostgreSQL databases.
- REST communication between services.
- Kafka used for listing/rental domain event publishing.
- Flyway migrations per service.

## What is runnable now

### Backend workflow
1. Register/login user (`user-service`).
2. Register vehicle (`vehicle-service`).
3. Create/search/manage listing (`listing-service`).
4. Create/confirm/activate/complete rental (`rental-service`).

### Frontend demo workflow
1. Register
2. Login
3. Create vehicle
4. Create listing
5. Browse listings
6. View listing details
7. Create rental

## Run backend

```bash
docker compose up --build
```

### Started containers
- `zookeeper`, `kafka`
- `user-db`, `vehicle-db`, `listing-db`, `rental-db`
- `user-service`, `vehicle-service`, `listing-service`, `rental-service`

## Run frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```
