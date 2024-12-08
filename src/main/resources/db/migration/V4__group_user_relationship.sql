CREATE TABLE space_user_relationship(
    PRIMARY KEY (space_fk, user_fk),
    space_fk INT NOT NULL REFERENCES space(id) ON DELETE CASCADE,
    user_fk INT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    request_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    acknowledge_timestamp TIMESTAMP WITH TIME ZONE,
    accepted BOOLEAN
)

