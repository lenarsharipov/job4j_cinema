package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oSessionRepository implements SessionRepository {
    private final Sql2o sql2o;

    public Sql2oSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Session> findById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM film_sessions WHERE id = :id;");
            query.addParameter("id", id);
            var session = query.setColumnMappings(Session.COLUMN_MAPPING)
                    .executeAndFetchFirst(Session.class);
            return Optional.ofNullable(session);
        }

    }

    @Override
    public Collection<Session> findAll() {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("SELECT * FROM film_sessions;");
            return query.setColumnMappings(Session.COLUMN_MAPPING).executeAndFetch(Session.class);
        }
    }
}
