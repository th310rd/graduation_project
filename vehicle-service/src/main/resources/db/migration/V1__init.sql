CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    vin VARCHAR(255) NOT NULL UNIQUE,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    brand VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    color VARCHAR(100) NOT NULL,
    mileage BIGINT NOT NULL,
    insurance_expiry DATE NOT NULL,
    technical_inspection_expiry DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    current_rental_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
