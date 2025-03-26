package org.example.chickenstock.model.repository;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.chickenstock.model.dto.Account;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository implements JDBCRepository{
    final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    String URL = dotenv.get("DB_URL");
    String USER = dotenv.get("DB_USER");
    String PASSWORD = dotenv.get("DB_PASSWORD");

    public List<Account> findAll() throws Exception {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            // PreparedStatement로 왜 안 만듦?
            String query = "SELECT * FROM accounts ORDER BY account_id";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                accounts.add(new Account(resultSet.getLong("account_id"), resultSet.getString("nickname")));
            }
        }
        return accounts;
    }

    public void save(Account account) throws Exception {
        try(Connection connection = getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "INSERT INTO accounts(nickname) VALUES ('%s')".formatted(account.nickname());
//            작은 따옴표 안넣어주면 column 이름으로 생각함
            int rowsAffected = statement.executeUpdate(query);
            System.out.println("rowsAffected = " + rowsAffected);
        }
    }

    public void delete(long id) throws Exception {
        try(Connection connection = getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM accounts WHERE account_id = %d".formatted(id);
            int rowAffected = statement.executeUpdate(query);
            System.out.println("rowAffected = " + rowAffected);
        }
    }
}
