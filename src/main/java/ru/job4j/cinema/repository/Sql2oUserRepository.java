package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (var connection = sql2o.open()) {
            var sql =
                    """
                    INSERT INTO users(full_name, email, password)
                    VALUES (:fullName, :email, :password)
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("fullName", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.of(user);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (var connection = sql2o.open()) {
            var sql = """
                    SELECT * FROM users
                    WHERE email = :email AND password = :password
                    """;
            var query = connection.createQuery(sql, true)
                    .addParameter("email", email)
                    .addParameter("password", password);
            User user = query
                    .setColumnMappings(User.COLUMN_MAPPING)
                    .executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM users");
            return query.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
        }
    }

    @Override
    public boolean deleteByEmail(String email) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM users WHERE email = :email");
            query.addParameter("email", email);
            var affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}
