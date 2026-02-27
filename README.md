# UZ-P2P Car Rental Platform (Turo-style) — Strong Legal Mode (EDS)

A fully online, microservice-based P2P car rental marketplace for Uzbekistan:
- Hosts list vehicles
- Guests book rentals
- All documents are submitted online
- Contracts + inspection acts are signed using **Electronic Digital Signature (EDS)**
- Deposits are authorized/held and can be captured partially for damages/penalties
- Optional geotracking + geofence enforcement + driver behavior scoring

## 1) Goals

### Product Goals
1. Fully online onboarding and booking (no manual office paperwork).
2. Strong legal enforceability via **EDS**:
   - Rental contract signed by both parties
   - Pickup/Return inspection acts signed by both parties
   - Dispute decisions recorded with audit trail (platform operator)
3. Marketplace model:
   - Host listings, availability calendar, pricing configuration
   - Search + booking + reviews
4. Trust & safety:
   - KYC for hosts & guests
   - Risk-based deposit pricing
   - Evidence timeline for damage disputes
5. Uzbekistan operational reality:
   - OSAGO insurance requirement gating listing availability
   - Tech inspection expiry gating
   - Region plans (Tashkent-only vs Uzbekistan-wide) enforceable by geofencing

### Engineering Goals
- Microservice architecture with clean bounded contexts
- PostgreSQL per service (with migrations)
- Docker Compose local environment
- Observability: logs, metrics, tracing, correlation IDs
- Security: RS256 JWT, service-to-service authentication, encrypted PII, audit logs

---

## 2) Core Services (Microservices)

### 2.1 api-gateway
**Responsibilities**
- Single entrypoint
- JWT verification (RS256)
- Routing to downstream services
- Rate limiting (auth, telemetry)
- Correlation ID injection (`X-Request-Id`)

### 2.2 user-service
**Responsibilities**
- User accounts (Host/Guest roles)
- Authentication (RS256 JWT)
- KYC profile & verification status
- Driver license record & eligibility evaluation
- EDS identity linkage (store EDS certificate metadata / identifiers)
- Device/session security (failed login lock, refresh tokens optional)

**Key features**
- Encrypted storage for passportNumber + PINFL (field encryption)
- Consent records for personal data processing
- Verification workflow: NOT_STARTED → IN_REVIEW → VERIFIED → REJECTED

### 2.3 listing-service (Marketplace)
**Responsibilities**
- Host listings (vehicle listing profile, photos, rules)
- Availability calendar
- Pricing configuration per listing
- Search & filtering (city, dates, class, transmission, deposit tier, etc.)
- Publication gating based on compliance status from vehicle-service & host KYC

### 2.4 vehicle-service
**Responsibilities**
- Vehicle registry (VIN, plate, class, docs)
- Legal/compliance validity:
  - OSAGO insurance status and expiry
  - Technical inspection expiry
  - Ownership proof status
- Availability state (AVAILABLE/RENTED/BLOCKED)
- Vehicle locking for rentals (idempotent lock with TTL)

### 2.5 rental-service
**Responsibilities**
- Booking lifecycle and contract engine:
  - CREATED → CONFIRMED → ACTIVE → COMPLETED / CANCELLED / VIOLATION
- Prevent overlapping rentals (Postgres constraints recommended)
- Orchestrate:
  - guest eligibility (user-service)
  - listing availability (listing-service)
  - vehicle availability & lock (vehicle-service)
  - pricing quote snapshot (pricing-service)
  - EDS signature workflow (eds-service)
- Late penalties, mileage overages, violation penalties
- Dispute linking (damage claim workflow)

### 2.6 pricing-service
**Responsibilities**
- Calculate pricing quote breakdown:
  - base price (tiered by duration)
  - fees/discounts
  - deposit (risk-based)
  - mileage plan limits + overage rates
- Provide explainable quote output with reasons
- Version pricing policies; rental snapshots store quote + version

**Uzbekistan starter price policy (UZS)**
- Spark: 300k / 280k / 260k / 240k (1–3 / 4–7 / 8–14 / 15–30)
- Cobalt: 400k / 375k / 350k / 325k
- Gentra: 450k / 420k / 390k / 360k
- Malibu/K5: 1.1m / 1.05m / 1.0m / 0.95m
- Lixiang L9: 3.0m / 2.85m / 2.7m / 2.55m

Deposits baseline:
- Economy/Sedan: 3,000,000
- Comfort/Mid: 4,000,000
- Premium/Luxury: 5,000,000
Risk add-on:
- LOW +0
- MEDIUM +1,000,000
- HIGH +2,000,000

Mileage plans:
- CITY_TASHKENT: 200 km/day, 3,000 UZS per extra km
- UZBEKISTAN: 300 km/day, 2,500 UZS per extra km

Late penalties:
- 0–59 min grace: 0
- 1–6 hours: 0.5 day rate
- >6 hours: 1 day rate
- Cap: 3 days penalty unless manual review

### 2.7 eds-service (Strong Legal)
**Responsibilities**
- Integrate with EDS provider (or stub in dev)
- Manage signature sessions:
  - prepare document hash
  - request signature
  - verify signed payload
  - store certificate metadata + verification evidence
- Provide APIs to:
  - initiate signature for contract/acts
  - verify signature and finalize document state
- Maintain immutable signature audit record

**Documents requiring EDS**
- Rental Contract (Host + Guest)
- Pickup Inspection Act (Host + Guest)
- Return Inspection Act (Host + Guest)
- Dispute Resolution Decision (Platform operator)

### 2.8 document-service (Evidence Vault)
**Responsibilities**
- Store artifacts (photos, PDFs, scans) in object storage (MinIO locally)
- Metadata DB: ownership, timestamps, document type, hash
- Tamper evidence:
  - hash each artifact
  - optionally chain hashes per rental (audit trail)
- Access control: only authorized parties (host/guest/support) per rental

### 2.9 payments-service (Escrow)
**Responsibilities**
- Payment intent + deposit authorization/hold
- Capture rental fee
- Release deposit
- Partial capture for:
  - late return penalty
  - mileage overage
  - damage claim
- Host payouts + platform commission ledger
- Provider adapter interface (Payme/Click later); stub in dev

### 2.10 geotracking-service (Optional but recommended)
**Responsibilities**
- Telemetry ingestion for ACTIVE rentals only
- Geofence enforcement (region plans)
- Violations:
  - outside allowed zone
  - speed threshold exceeded
- Driver behavior score projection per rental/user
- Event to rental-service when violation detected

---

## 3) Key Workflows (End-to-End)

### 3.1 Host onboarding → listing publication
1. Host registers, completes KYC, links EDS identity.
2. Host adds vehicle, uploads ownership proof + OSAGO + inspection docs.
3. vehicle-service validates expiry and status.
4. Host creates listing, uploads photos, sets rules, prices, availability.
5. listing becomes `PUBLISHED` only if:
   - host KYC = VERIFIED
   - vehicle compliance = VALID (OSAGO + inspection)
   - vehicle status not BLOCKED

### 3.2 Guest onboarding
1. Guest registers, completes KYC, uploads license, links EDS identity.
2. user-service sets verification status.
3. Eligibility evaluation:
   - age >= 21 (configurable)
   - license category B
   - license not expired
   - license issued >= 1 year
   - user status ACTIVE
   - verification VERIFIED

### 3.3 Booking + contract signing (Strong Legal)
1. Guest searches listings with dates → listing-service returns availability.
2. Guest requests booking → rental-service creates `CREATED`.
3. rental-service requests quote from pricing-service.
4. Host accepts OR system instant-books (configurable).
5. rental-service transitions to `CONFIRMED` after:
   - guest eligibility OK
   - listing still available
   - vehicle lock acquired
   - payment deposit authorized + rental fee captured (or preauth)
6. EDS flow:
   - rental-service generates contract PDF (template + snapshot data)
   - document-service stores contract
   - eds-service initiates signature session for Guest + Host
   - once both signatures verified, contract status = SIGNED

### 3.4 Pickup & return acts (Strong Legal)
Pickup:
- Host + Guest perform checklist + photos + mileage + fuel.
- pickup act PDF generated + stored + EDS signed by both.
- rental-service → ACTIVE.

Return:
- Similar process; return act signed by both.
- rental-service computes penalties & finalization.

### 3.5 Dispute process
- Host can open damage claim within a configured window.
- Evidence: before/after photos, acts, telemetry, notes.
- Platform operator reviews and issues decision document (EDS).
- payments-service captures partial/full deposit accordingly.

---

## 4) Data & Security Requirements

### 4.1 PII handling (passport, PINFL, license)
- Encrypt sensitive fields at rest (field-level encryption).
- Strict RBAC: support/admin access is audited.
- Store explicit consent records (time, version, purpose).
- Retention policy: configurable.

### 4.2 Auth
- RS256 JWT issued by user-service
- Gateway validates JWT
- Services validate JWT as defense-in-depth
- Service-to-service auth:
  - internal client credentials OR mTLS (optional)
- Correlation IDs propagated (`X-Request-Id`)

### 4.3 Audit logging (must-have for Strong Legal)
Record immutable audit events:
- who did what, when, from where, on which entity
- include document version IDs + hash references
- include EDS verification evidence IDs

---

## 5) Database Notes

### 5.1 Overlapping rental prevention
Use PostgreSQL exclusion constraint / range types in rental-service, or equivalent robust logic.

### 5.2 Vehicle locking
vehicle-service supports:
- `LOCK(vehicleId, rentalId, ttl)`
- idempotent lock acquisition for same rentalId
- lock expiry cleanup

---

## 6) APIs (High-level)

### user-service
- POST /auth/register
- POST /auth/login
- POST /kyc/submit
- GET /users/me
- GET /users/{id}/eligibility
- POST /eds/link (store EDS identity binding)

### listing-service
- POST /listings
- PUT /listings/{id}
- PUT /listings/{id}/availability
- GET /listings/search
- POST /listings/{id}/photos

### vehicle-service
- POST /vehicles
- PUT /vehicles/{id}/docs
- GET /vehicles/{id}/availability
- POST /vehicles/{id}/lock
- POST /vehicles/{id}/unlock

### rental-service
- POST /rentals (create booking)
- PUT /rentals/{id}/confirm
- PUT /rentals/{id}/activate (after pickup act signed)
- PUT /rentals/{id}/complete (after return act signed)
- POST /rentals/{id}/disputes
- GET /rentals/{id}

### pricing-service
- POST /pricing/quote

### document-service
- POST /docs/upload
- GET /docs/{id}/download (authorized)
- GET /docs/rentals/{rentalId}/timeline

### eds-service
- POST /eds/signatures/initiate (docId + signer)
- POST /eds/signatures/verify (signedPayload)
- GET /eds/signatures/{id}

### payments-service
- POST /payments/authorize-deposit
- POST /payments/capture-rental
- POST /payments/release-deposit
- POST /payments/capture-penalty
- POST /payouts/host

### geotracking-service (optional)
- POST /tracking/telemetry
- GET /tracking/rentals/{id}/violations
- GET /tracking/rentals/{id}/score

---

## 7) Implementation Plan (Claude Code Tasks)

### Milestone 1 — Platform Skeleton
- [ ] Create gateway + service templates
- [ ] Docker Compose with Postgres per service + MinIO
- [ ] Common libs: logging, tracing, JWT verification, correlation IDs

### Milestone 2 — Strong Legal Identity & EDS
- [ ] Implement eds-service with provider adapter + stub
- [ ] Implement document-service (MinIO + metadata + hashing)
- [ ] Implement contract/act generation (PDF templates) in rental-service
- [ ] Implement signature workflow + audit log

### Milestone 3 — Marketplace Core
- [ ] Host/Guest roles + KYC flows
- [ ] Vehicle compliance gating (OSAGO + inspection)
- [ ] Listing creation, photos, rules, availability
- [ ] Search with date filters

### Milestone 4 — Booking & Payments
- [ ] Pricing-service quote + snapshot in rental
- [ ] Vehicle lock integration
- [ ] payments-service escrow model (stub provider)
- [ ] Booking confirm → EDS contract signed → pickup act signed → activate

### Milestone 5 — Disputes & Evidence
- [ ] Pickup/return inspection acts + evidence timeline
- [ ] Dispute workflow + decision document signed by platform operator
- [ ] Deposit capture/release flows

### Milestone 6 — Geo Plans (optional but recommended)
- [ ] Geofences for CITY vs UZBEKISTAN
- [ ] Violations → penalties + risk updates

---

## 8) Acceptance Criteria (Definition of Done)

### Strong Legal (EDS) minimum
- Contract PDF is generated from snapshot and stored with hash
- Both guest and host EDS signatures verified and stored
- Pickup & return acts signed with EDS
- Audit log contains:
  - document IDs + hashes
  - signature IDs + verification evidence
  - timestamps and actors
- System can demonstrate:
  - a fully online booking
  - fully online contract signing
  - fully online pickup/return documentation
  - dispute workflow with evidence

### Marketplace minimum
- Hosts can list vehicles and set availability/pricing
- Guests can search and book for specific dates
- Overlapping rentals prevented
- Vehicle cannot be booked if OSAGO or inspection expired

---

## 9) Local Development

### Prerequisites
- Docker + Docker Compose
- JDK 21 (or 17)
- Maven/Gradle
- Optional: Postman/Insomnia

### Run
1. `docker compose up -d` (databases, MinIO, observability stack)
2. Start services locally or via compose profiles

---

## 10) Notes
- This repository targets "Strong Legal Mode". EDS provider integration may be stubbed locally but must be architected as a replaceable adapter.
- All prices and policies are versioned and snapshot at confirmation to ensure legal/financial integrity.
