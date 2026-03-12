# Production-Ready P2P Car Sharing Platform (Uzbekistan)

This repository contains a microservice backend platform for a peer-to-peer car rental marketplace.

## Services

### Core domain services
- user-service (8081)
- vehicle-service (8082)
- rental-service (8083)
- listing-service (8084)
- pricing-service (8085)
- payment-service (8086)
- document-service (8087)
- signature-service (8088)
- geotracking-service (8089)
- notification-service (8090)

### Infrastructure services
- api-gateway (8080)
- eureka-server (8761)
- config-server (8888)
- Kafka + Zookeeper
- Prometheus + Grafana

## Architecture

- Independent microservices with separate PostgreSQL databases.
- Synchronous communication: REST APIs.
- Event-driven communication: Kafka topics (see `events/event-definitions.md`).
- Flyway migrations for schema versioning.
- Dockerfiles for each service and full orchestration via Docker Compose.

## Core Workflow

1. Register/login user (`/auth/register`, `/auth/login`).
2. Add driver license and validate eligibility.
3. Register vehicle and check availability.
4. Create listing and search listing.
5. Calculate pricing (`/pricing/calculate`).
6. Create and confirm rental.
7. Generate/sign contract.
8. Process payment.
9. Activate rental, ingest telemetry, complete rental.
10. Trigger notifications and owner payout (payment extension point).

## Key APIs (examples)

### User Service
- `POST /auth/register`
- `POST /auth/login`
- `POST /users/{id}/license`
- `GET /users/{id}/eligibility`

### Vehicle Service
- `POST /vehicles`
- `GET /vehicles/{id}/availability`
- `POST /vehicles/{id}/lock`
- `POST /vehicles/{id}/unlock`

### Listing Service
- `POST /listings`
- `GET /listings`
- `GET /listings/{id}`
- `GET /listings/search?city=Tashkent&minPrice=500000&maxPrice=1000000`

### Rental Service
- `POST /rentals`
- `PUT /rentals/{id}/confirm`
- `PUT /rentals/{id}/activate`
- `PUT /rentals/{id}/complete`

### Pricing Service
- `POST /pricing/calculate`

Example response:
```json
{
  "basePrice": 1200000,
  "platformFee": 180000,
  "deposit": 1500000,
  "deliveryFee": 100000,
  "total": 1480000
}
```

### Payment Service
- `POST /payments/rental`
- `POST /payments/refund`
- `GET /payments/{id}`

### Document Service
- `POST /documents/upload`
- `GET /documents/{id}`

### Signature Service
- `POST /contracts`
- `POST /contracts/{id}/sign`
- `POST /contracts/{id}/verify`

### GeoTracking Service
- `POST /tracking/telemetry`
- `GET /tracking/vehicles/{vehicleId}`

### Notification Service
- `POST /notifications`

## Example requests

### Register user
```json
{
  "firstName": "Ali",
  "lastName": "Karimov",
  "email": "ali@example.com",
  "phoneNumber": "+998901112233",
  "dateOfBirth": "1996-04-10",
  "password": "StrongPassword123!",
  "roles": ["ROLE_HOST", "ROLE_RENTER"]
}
```

### Add license
```json
{
  "licenseNumber": "AB1234567",
  "category": "B",
  "issuedDate": "2020-01-01",
  "expiryDate": "2030-01-01",
  "issuedBy": "Tashkent DMV",
  "countryCode": "UZ"
}
```

### Register vehicle
```json
{
  "ownerId": "<host-id>",
  "vin": "1HGCM82633A123456",
  "licensePlate": "01A123BC",
  "brand": "Chevrolet",
  "model": "Cobalt",
  "year": 2022,
  "color": "White",
  "mileage": 42000,
  "insuranceExpiry": "2027-01-01",
  "technicalInspectionExpiry": "2027-01-01",
  "status": "AVAILABLE"
}
```

### Create rental
```json
{
  "vehicleId": "<vehicle-id>",
  "guestId": "<guest-id>",
  "startDateTime": "2027-04-01T10:00:00",
  "endDateTime": "2027-04-05T10:00:00"
}
```

## Running locally

```bash
docker compose up --build
```

## Notes

- JWT payload includes `userId` and `roles` in user-service login response.
- Rental service publishes lifecycle events to Kafka (`rental.created`, `rental.confirmed`, `rental.completed`).
- OpenAPI summary and event definitions are in `docs/` and `events/`.
