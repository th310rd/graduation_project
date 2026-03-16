CREATE TABLE listings (
    id UUID PRIMARY KEY,
    vehicle_id UUID NOT NULL,
    owner_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    city VARCHAR(120) NOT NULL,
    price_per_day NUMERIC(12,2) NOT NULL,
    deposit_amount NUMERIC(12,2) NOT NULL,
    instant_booking BOOLEAN NOT NULL,
    delivery_available BOOLEAN NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_listings_search ON listings(city, price_per_day, delivery_available, status);

CREATE TABLE listing_photos (
    id UUID PRIMARY KEY,
    listing_id UUID NOT NULL REFERENCES listings(id),
    photo_url TEXT NOT NULL,
    sort_order INTEGER NOT NULL
);

CREATE INDEX idx_listing_photos_listing_sort ON listing_photos(listing_id, sort_order);

CREATE TABLE listing_rules (
    id UUID PRIMARY KEY,
    listing_id UUID NOT NULL UNIQUE REFERENCES listings(id),
    min_driver_age INTEGER NOT NULL,
    min_rental_days INTEGER NOT NULL,
    max_rental_days INTEGER NOT NULL,
    smoking_allowed BOOLEAN NOT NULL,
    pets_allowed BOOLEAN NOT NULL,
    international_travel_allowed BOOLEAN NOT NULL
);
