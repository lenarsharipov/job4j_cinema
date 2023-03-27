package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oSessionRepositoryTest {
    private static Sql2oSessionRepository sessionRepository;
    private final List<Session> sessions = List.of(
            new Session(
                    1, 1, 4,
                    LocalDateTime.of(2023, 4, 1, 10, 20, 0),
                    LocalDateTime.of(2023, 4, 1, 11, 50, 0), 210),
            new Session(2, 1, 3,
                    LocalDateTime.of(2023, 4, 1, 11, 20, 0),
                    LocalDateTime.of(2023, 4, 1, 12, 50, 0), 410),
            new Session(3, 1, 4,
                    LocalDateTime.of(2023, 4, 1, 12, 40, 0),
                    LocalDateTime.of(2023, 4, 1, 14, 10, 0), 250),
            new Session(4, 1, 3,
                    LocalDateTime.of(2023, 4, 1, 13, 30, 0),
                    LocalDateTime.of(2023, 4, 1, 15, 0, 0), 450),
            new Session(5, 1, 4,
                    LocalDateTime.of(2023, 4, 1, 17, 0, 0),
                    LocalDateTime.of(2023, 4, 1, 18, 30, 0), 270)
            );

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oFilmRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sessionRepository = new Sql2oSessionRepository(sql2o);
    }

    @Test
    public void whenFindAllThenGet() {
        var list = sessionRepository.findAll()
                .stream()
                .limit(5)
                .toList();
        assertThat(list).isEqualTo(sessions);
    }

    @Test
    public void whenFindByIdThenFound() {
        assertThat(sessionRepository.findById(1).get()).isEqualTo(sessions.get(0));
        assertThat(sessionRepository.findById(2).get()).isEqualTo(sessions.get(1));
    }

    @Test
    public void whenFindByIdNonExistingItemThenEmptyOptional() {
        assertThat(sessionRepository.findById(0)).isEqualTo(Optional.empty());
        assertThat(sessionRepository.findById(50)).isEqualTo(Optional.empty());
    }

}