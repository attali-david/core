CREATE TABLE channel (
    id SERIAL PRIMARY KEY,
    created_at_local TIMESTAMP WITH TIME ZONE,
    created_at_server TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    space_fk INT NOT NULL REFERENCES space(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    description TEXT
)