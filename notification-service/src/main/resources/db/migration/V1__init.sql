CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    external_ref VARCHAR(255) NOT NULL,
    status VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
