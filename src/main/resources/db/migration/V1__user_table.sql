CREATE TYPE privilege AS ENUM ('SYSTEM', 'ADMIN', 'USER', 'GUEST');

CREATE TABLE app_user (
    id SERIAL PRIMARY KEY,
    created_at_local TIMESTAMP,
    created_at_server TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    password TEXT,
    email TEXT UNIQUE NOT NULL,
    name TEXT,
    privilege privilege DEFAULT 'GUEST'
);

