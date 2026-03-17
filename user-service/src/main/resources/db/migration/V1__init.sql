CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE driver_licenses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    license_number VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    issued_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    issued_by VARCHAR(255) NOT NULL,
    country_code VARCHAR(10) NOT NULL
);
