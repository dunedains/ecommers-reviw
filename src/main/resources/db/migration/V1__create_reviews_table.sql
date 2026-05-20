CREATE TABLE reviews (
    id             BIGINT        GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id     BIGINT        NOT NULL,
    user_id        BIGINT        NOT NULL,
    rating         INTEGER       NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_comment VARCHAR(1000)
);
