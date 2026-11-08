During the introductory internship in the subject Programming on the topic Working with Databases, I developed an educational database for a library and a console Java application for working with it via JDBC. The work was performed using the PostgreSQL DBMS.

Domain Design
I described the domain Library. The database was to store information about books, authors, readers, and book loans. For this purpose, I designed four tables: authors, books, readers, and loans. The table structure is as follows. The authors table contains id, full_name, and country. The books table contains id, title, year, and author_id. The readers table contains id, full_name, and phone. The loans table contains id, book_id, reader_id, loan_date, and return_date. The tables have one-to-many relationships. One author can have many books through authors.id to books.author_id. One book can be loaned many times through books.id to loans.book_id. One reader can borrow books many times through readers.id to loans.reader_id. Thus, the database schema reflects the main business processes of a library: storing the book collection, information about authors and readers, and the loan history.

Development of DDL Scripts
I wrote DDL scripts to create the tables in PostgreSQL. The scripts provide for dropping old tables before creating new ones, creating primary keys via GENERATED ALWAYS AS IDENTITY, creating foreign keys between tables, and creating CHECK constraints. In particular, I added the following constraints. The author’s name and country must not be empty. The book title must not be empty. The book publication year must be greater than 0 and not greater than 2026. The reader’s name and phone must not be empty. The reader’s phone must be unique. The book return date cannot be earlier than the loan date. For the loans table, the loan date is set by default to the current date via DEFAULT CURRENT_DATE, and the return date may be NULL if the book has not yet been returned.

Populating the Database with Test Data
I populated the database with test data: 5 authors, 5 books, 5 readers, and 8 book loan records. The examples used include the following authors: Leo Tolstoy, Fyodor Dostoevsky, Alexander Pushkin, Mark Twain, and William Shakespeare. The books are War and Peace, Crime and Punishment, Eugene Onegin, The Adventures of Tom Sawyer, and Hamlet. I also added readers with phone numbers and several loans; some books have been returned, while others are still with readers.

Development of Business Queries
I composed and demonstrated several business queries using JOIN, filtering, sorting, and row limiting.

Query 5.1 — Books by Authors from a Given Country
The query returns the book title, author name, and publication year for books by authors from a specified country. It uses a join between books and authors, a filter on authors.country, and sorting by publication year. For example, for the country Russia, the following books were returned: Eugene Onegin by Alexander Pushkin, 1831; Crime and Punishment by Fyodor Dostoevsky, 1866; and War and Peace by Leo Tolstoy, 1869. For the country USA, the book The Adventures of Tom Sawyer by Mark Twain, 1876, was returned.

Query 5.2 — Latest Loans of a Given Reader
The query returns the reader’s name, book title, and loan date for a specified reader name. It uses a JOIN of three tables: loans, readers, and books, a LIKE filter, sorting by loan date in descending order, and LIMIT 3. For example, for the reader Mikhail, his latest loans were displayed; for the reader Igor, his latest loans were displayed accordingly.

Query 5.3 — Books Not Returned
The query returns the reader, book, and loan date for books that have not yet been returned, that is, return_date IS NULL, and were borrowed after a specified date. The result is sorted by loan date in ascending order. A variant of the query with the condition loan_date less than 2026-10-03 was also demonstrated.

Development of the Java Application
I implemented a console Java application called Library that connects to the PostgreSQL database via JDBC. The application provides checking for the presence of the JDBC driver org.postgresql.Driver, checking the database connection, connecting to the local database library at localhost, and a console menu with the following items: show table data; show books by authors from a given country; show the latest books of a given reader; show books not returned; add a book loan; exit. The code implements the following methods: checkDriver checks the JDBC driver; checkDB checks the database connection; getAllAuthors displays authors; getAllBooks displays books; getAllReaders displays readers; getAllLoans displays loans; getBooksByCountry searches for books by the author’s country; getLoansByReader searches for loans by reader name; getNotReturnedBooks searches for books not returned; addLoan adds a new loan. For parameterized queries, PreparedStatement is used, for example, when searching for books by country and loans by reader. Connection errors and SQL errors are handled via try-catch and SQLException.

Result
As a result of the work, I designed a relational database for a library, created tables with integrity constraints, populated the database with test data, developed business queries with joins and filtering, and implemented a Java application for working with the database via JDBC. The work helped consolidate skills in database design, writing DDL and DML queries, and connecting a Java application to PostgreSQL.
