DROP TABLE IF EXISTS loans, books, readers, authors;

CREATE TABLE authors (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    full_name varchar(128),
    country varchar(128),

    CONSTRAINT authors_check_full_name_not_empty CHECK (full_name IS NOT NULL AND trim(full_name) <> ''),
    CONSTRAINT authors_check_country_not_empty CHECK (country IS NOT NULL AND trim(country) <> '')
);

CREATE TABLE books (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title varchar(128),
    year int NOT NULL,
    author_id int NOT NULL,

    CONSTRAINT books_check_title_not_empty CHECK (title IS NOT NULL AND trim(title) <> ''),
    
    CONSTRAINT books_check_year CHECK (year > 0 AND year <= 2026),
    
    CONSTRAINT books_authors_fk FOREIGN KEY (author_id) REFERENCES authors(id)
);

CREATE TABLE readers (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    full_name varchar(128),
    phone varchar(128) UNIQUE,

    CONSTRAINT readers_check_full_name_not_empty CHECK (full_name IS NOT NULL AND trim(full_name) <> ''),
    CONSTRAINT readers_check_phone_not_empty CHECK (phone IS NOT NULL AND trim(phone) <> '')
);

CREATE TABLE loans (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    book_id int NOT NULL,
    reader_id int NOT NULL,
    loan_date date NOT NULL DEFAULT CURRENT_DATE,
    return_date date,

    CONSTRAINT loans_check_return_date CHECK (return_date IS NULL OR return_date >= loan_date),
    
    CONSTRAINT loans_books_fk FOREIGN KEY (book_id) REFERENCES books(id),
    CONSTRAINT loans_readers_fk FOREIGN KEY (reader_id) REFERENCES readers(id)
);
