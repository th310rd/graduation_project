# Kafka Event Definitions

- `rental.created`
- `rental.confirmed`
- `rental.completed`
- `payment.completed`
- `contract.signed`
- `vehicle.location.updated`

Each event should include: `eventId`, `eventType`, `occurredAt`, `correlationId`, `payload`.
