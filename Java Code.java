import java.sql.*;
import java.util.Scanner;

public class Library {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String PROTOCOL = "jdbc:postgresql://";
    private static final String URL_LOCALE = "localhost/";
    private static final String DATABASE_NAME = "library";
    private static final String DATABASE_URL = PROTOCOL + URL_LOCALE + DATABASE_NAME;

    private static final String USER_NAME = "postgres";
    private static final String DATABASE_PASS = "postgres";

    public static void main(String[] args) {
        if (checkDriver() && checkDB())
            System.out.println("Успешное подключение к базе данных | " + DATABASE_URL + "\n");
        else return;

        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS)) {
            String choice = "";
            while (!choice.equals("0")) {
                System.out.println("1. Показать все таблицы");
                System.out.println("2. Книги авторов из страны");
                System.out.println("3. Последние книги читателя");
                System.out.println("4. Несданные книги");
                System.out.println("5. Добавить выдачу");
                System.out.println("0. Выход");

                System.out.print("Выберите пункт: ");
                choice = scanner.nextLine();
                System.out.println();

                try {
                    if (choice.equals("1")) {
                        getAllAuthors(connection);
                        getAllBooks(connection);
                        getAllReaders(connection);
                        getAllLoans(connection);
                    }
                    if (choice.equals("2")) {
                        System.out.print("Введите страну: ");
                        String country = scanner.nextLine();
                        getBooksByCountry(connection, country);
                    }
                    if (choice.equals("3")) {
                        System.out.print("Введите имя читателя: ");
                        String name = scanner.nextLine();
                        getLoansByReader(connection, name);
                    }
                    if (choice.equals("4")) {
                        System.out.print("Введите дату: ");
                        String date = scanner.nextLine();
                        getNotReturnedBooks(connection, date);
                    }
                    if (choice.equals("5")) {
                        System.out.print("Введите id книги: ");
                        int bookId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Введите id читателя: ");
                        int readerId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Введите дату выдачи: ");
                        String date = scanner.nextLine();
                        addLoan(connection, bookId, readerId, date);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Неверный ввод!");
                } catch (SQLException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                }
            }

            System.out.println("Программа завершена.");

        } catch (SQLException e) {
            System.out.println("Ошибка соединения " + e.getErrorCode() + " " + e.getSQLState());
            throw new RuntimeException(e);
        }
    }

    public static boolean checkDriver () {
        try {
            Class.forName(DRIVER);
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Нет JDBC-драйвера! Подключите JDBC-драйвер к проекту согласно инструкции.");
            throw new RuntimeException(e);
        }
    }

    public static boolean checkDB () {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, DATABASE_PASS);
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException ex) {throw new RuntimeException(ex);}
            return true;
        } catch (SQLException e) {
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException ex) {throw new RuntimeException(ex);}

            System.out.println("Нет подключения к базе данных! Проверьте имя базы, путь к базе или разверните локально резервную копию согласно инструкции");
            throw new RuntimeException(e);
        }
    }

    private static void getAllAuthors(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM authors;");

        while (rs.next()) {
            String param0 = rs.getString("id");
            String param1 = rs.getString("full_name");
            String param2 = rs.getString("country");
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }

        statement.close();
        System.out.println();
    }

    private static void getAllBooks(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM books;");

        while (rs.next()) {
            String param0 = rs.getString("id");
            String param1 = rs.getString("title");
            String param2 = rs.getString("year");
            String param3 = rs.getString("author_id");
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3);
        }

        statement.close();
        System.out.println();
    }

    private static void getAllReaders(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM readers;");

        while (rs.next()) {
            String param0 = rs.getString("id");
            String param1 = rs.getString("full_name");
            String param2 = rs.getString("phone");
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }

        statement.close();
        System.out.println();
    }

    private static void getAllLoans(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM loans;");

        while (rs.next()) {
            String param0 = rs.getString("id");
            String param1 = rs.getString("book_id");
            String param2 = rs.getString("reader_id");
            String param3 = rs.getString("loan_date");
            String param4 = rs.getString("return_date");
            System.out.println(param0 + " | " + param1 + " | " + param2 + " | " + param3 + " | " + param4);
        }

        statement.close();
        System.out.println();
    }

    private static void getBooksByCountry(Connection connection, String country) throws SQLException {
        if (country == null || country.isBlank())
            return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT books.title, authors.full_name, books.year FROM books " +
                        "JOIN authors ON books.author_id = authors.id " +
                        "WHERE authors.country = ? " +
                        "ORDER BY books.year;");
        statement.setString(1, country);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String param0 = rs.getString("title");
            String param1 = rs.getString("full_name");
            String param2 = rs.getString("year");
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }

        statement.close();
        System.out.println();
    }

    private static void getLoansByReader(Connection connection, String name) throws SQLException {
        if (name == null || name.isBlank())
            return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT readers.full_name, books.title, loans.loan_date FROM loans " +
                        "JOIN readers ON loans.reader_id = readers.id " +
                        "JOIN books ON loans.book_id = books.id " +
                        "WHERE readers.full_name LIKE ? " +
                        "ORDER BY loans.loan_date DESC " +
                        "LIMIT 3;");
        name = '%' + name + '%';
        statement.setString(1, name);
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String param0 = rs.getString("full_name");
            String param1 = rs.getString("title");
            String param2 = rs.getString("loan_date");
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }

        statement.close();
        System.out.println();
    }

    private static void getNotReturnedBooks(Connection connection, String date) throws SQLException {
        if (date == null || date.isBlank())
            return;

        PreparedStatement statement = connection.prepareStatement(
                "SELECT readers.full_name, books.title, loans.loan_date FROM loans " +
                        "JOIN readers ON loans.reader_id = readers.id " +
                        "JOIN books ON loans.book_id = books.id " +
                        "WHERE loans.return_date IS NULL AND loans.loan_date >= ? " +
                        "ORDER BY loans.loan_date;");
        statement.setDate(1, Date.valueOf(date));
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String param0 = rs.getString("full_name");
            String param1 = rs.getString("title");
            String param2 = rs.getString("loan_date");
            System.out.println(param0 + " | " + param1 + " | " + param2);
        }

        statement.close();
        System.out.println();
    }

    private static void addLoan(Connection connection, int bookId, int readerId, String date) throws SQLException {
        if (date == null || date.isBlank())
            return;

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO loans(book_id, reader_id, loan_date) VALUES (?, ?, ?);");
        statement.setInt(1, bookId);
        statement.setInt(2, readerId);
        statement.setDate(3, Date.valueOf(date));

        int count = statement.executeUpdate();
        System.out.println("Добавлено " + count + " выдач");

        statement.close();
        getAllLoans(connection);
    }
}


