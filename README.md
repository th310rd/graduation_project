# P2P Car Rental Platform MVP (Uzbekistan)

This repository contains a working first version of a microservice-based P2P car rental backend with 3 services:

- `user-service` (port `8081`)
- `vehicle-service` (port `8082`)
- `rental-service` (port `8083`)

Implemented core workflow:

`User registers → host registers vehicle → renter creates rental → rental confirmed → rental active → rental completed → vehicle available again`

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven
- Docker + Docker Compose
- REST communication (`rental-service` calls `user-service` and `vehicle-service`)

## Project Structure

```text
.
├── user-service
├── vehicle-service
├── rental-service
└── docker-compose.yml
```

Each service follows this package structure:

- `controller`
- `service`
- `repository`
- `entity`
- `dto`
- `config`
- `exception`

## Business Logic Implemented

### User Service

- Register user: `POST /users`
- Add driver license: `POST /users/{id}/license`
- Check eligibility: `GET /users/{id}/eligibility`

Eligibility rules:

- `status == ACTIVE`
- age >= 21
- license category contains `B`
- license expiry is in the future
- license issued at least 1 year ago

### Vehicle Service

- Register vehicle: `POST /vehicles`
- Get vehicle: `GET /vehicles/{id}`
- Check availability: `GET /vehicles/{id}/availability`
- Lock vehicle: `POST /vehicles/{id}/lock`
- Unlock vehicle: `POST /vehicles/{id}/unlock`

Availability rules:

- insurance expiry > today
- technical inspection expiry > today
- status == AVAILABLE

### Rental Service

- Create rental: `POST /rentals`
- Confirm rental: `PUT /rentals/{id}/confirm`
- Activate rental: `PUT /rentals/{id}/activate`
- Complete rental: `PUT /rentals/{id}/complete`
- Get rental: `GET /rentals/{id}`

Create rental flow:

1. Call user-service eligibility endpoint
2. Call vehicle-service availability endpoint
3. Validate no overlapping rentals
4. Save with status `CREATED`

Lifecycle:

`CREATED -> CONFIRMED -> ACTIVE -> COMPLETED`

- Confirm locks vehicle in vehicle-service
- Complete unlocks vehicle (status back to `AVAILABLE`)

Overlapping prevention:

- rentals for same vehicle cannot overlap in `[startDateTime, endDateTime)` range for statuses `CREATED`, `CONFIRMED`, `ACTIVE`

## Run with Docker Compose

```bash
docker compose up --build
```

Services:

- User API: `http://localhost:8081`
- Vehicle API: `http://localhost:8082`
- Rental API: `http://localhost:8083`

Databases:

- user-db: localhost:5433
- vehicle-db: localhost:5434
- rental-db: localhost:5435

## Example Payloads

### 1) Register user

`POST /users`

```json
{
  "firstName": "Ali",
  "lastName": "Karimov",
  "email": "ali@example.com",
  "phoneNumber": "+998901112233",
  "dateOfBirth": "1998-04-10",
  "status": "ACTIVE"
}
```

### 2) Add license

`POST /users/{userId}/license`

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

### 3) Register vehicle

`POST /vehicles`

```json
{
  "ownerId": "<host-user-id>",
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

### 4) Create rental

`POST /rentals`

```json
{
  "vehicleId": "<vehicle-id>",
  "guestId": "<guest-user-id>",
  "startDateTime": "2027-04-01T10:00:00",
  "endDateTime": "2027-04-05T10:00:00"
}
```

### 5) Confirm rental

`PUT /rentals/{rentalId}/confirm`

### 6) Activate rental

`PUT /rentals/{rentalId}/activate`

```json
{
  "pickupMileage": 42000
}
```

### 7) Complete rental

`PUT /rentals/{rentalId}/complete`

```json
{
  "returnMileage": 42500
}
```

## Notes

Not implemented in this MVP (planned later):

- geotracking
- payments
- listing marketplace
- pricing service
- EDS signature
- Kafka
- notifications
