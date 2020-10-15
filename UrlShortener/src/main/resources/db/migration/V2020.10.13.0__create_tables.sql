CREATE TABLE redirect (
    id VARCHAR(30) PRIMARY KEY,
    target TEXT NOT NULL
);

CREATE TABLE visitor (
    id SERIAL PRIMARY KEY,
    ip VARCHAR(45) NOT NULL,
    redirect_id VARCHAR(30) NOT NULL,

    FOREIGN KEY(redirect_id)
    REFERENCES redirect(id)
    ON DELETE CASCADE
)
