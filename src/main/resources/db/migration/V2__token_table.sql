CREATE TABLE refresh_token (
    id SERIAL PRIMARY KEY,
    token TEXT NOT NULL,
    user_fk INT REFERENCES app_user (id) ON DELETE CASCADE NOT NULL,
    created_at_server TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);