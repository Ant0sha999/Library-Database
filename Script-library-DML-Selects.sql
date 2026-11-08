--SELECT books.title, authors.full_name, books.year FROM books
--JOIN authors ON books.author_id = authors.id
--WHERE authors.country = 'США'
--ORDER BY books.year;

--SELECT readers.full_name, books.title, loans.loan_date FROM loans
--JOIN readers ON loans.reader_id = readers.id
--JOIN books ON loans.book_id = books.id
--WHERE readers.full_name LIKE 'Игорь%'
--ORDER BY loans.loan_date DESC
--LIMIT 3;

--SELECT readers.full_name, books.title, loans.loan_date FROM loans
--JOIN readers ON loans.reader_id = readers.id
--JOIN books ON loans.book_id = books.id
--WHERE loans.return_date IS NULL AND loans.loan_date < '2026-10-03'
--ORDER BY loans.loan_date;


