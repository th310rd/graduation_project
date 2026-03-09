CREATE TABLE rentals (
    id UUID PRIMARY KEY,
    vehicle_id UUID NOT NULL,
    guest_id UUID NOT NULL,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    pickup_mileage BIGINT,
    return_mileage BIGINT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_rentals_vehicle_dates ON rentals(vehicle_id, start_date_time, end_date_time);
