package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oGenreRepositoryTest {
    private static Sql2oGenreRepository sql2oGenreRepository;
    private final List<Genre> genres = List.of(
            new Genre(1, "Анимация"),
            new Genre(2, "Боевик"),
            new Genre(3, "Военный"),
            new Genre(4, "Детектив"),
            new Genre(5, "Детский"),
            new Genre(6, "Драма"),
            new Genre(7, "Исторический"),
            new Genre(8, "Комедия"),
            new Genre(9, "Мелодрама"),
            new Genre(10, "Мистика"),
            new Genre(11, "Мюзикл"),
            new Genre(12, "Приключения"),
            new Genre(13, "Семейный"),
            new Genre(14, "Триллер"),
            new Genre(15, "Ужасы"),
            new Genre(16, "Фантастика"),
            new Genre(17, "Хоррор"),
            new Genre(18, "Экшн"));

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

        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }


    @Test
    public void whenFindFindByIdThenFound() {
        assertThat(sql2oGenreRepository.findById(1).get()).isEqualTo(genres.get(0));
        assertThat(sql2oGenreRepository.findById(2).get()).isEqualTo(genres.get(1));
        assertThat(sql2oGenreRepository.findById(3).get()).isEqualTo(genres.get(2));
        assertThat(sql2oGenreRepository.findById(4).get()).isEqualTo(genres.get(3));
        assertThat(sql2oGenreRepository.findById(5).get()).isEqualTo(genres.get(4));
        assertThat(sql2oGenreRepository.findById(6).get()).isEqualTo(genres.get(5));
        assertThat(sql2oGenreRepository.findById(7).get()).isEqualTo(genres.get(6));
        assertThat(sql2oGenreRepository.findById(8).get()).isEqualTo(genres.get(7));
        assertThat(sql2oGenreRepository.findById(9).get()).isEqualTo(genres.get(8));
        assertThat(sql2oGenreRepository.findById(10).get()).isEqualTo(genres.get(9));
        assertThat(sql2oGenreRepository.findById(11).get()).isEqualTo(genres.get(10));
        assertThat(sql2oGenreRepository.findById(12).get()).isEqualTo(genres.get(11));
        assertThat(sql2oGenreRepository.findById(13).get()).isEqualTo(genres.get(12));
        assertThat(sql2oGenreRepository.findById(14).get()).isEqualTo(genres.get(13));
        assertThat(sql2oGenreRepository.findById(15).get()).isEqualTo(genres.get(14));
        assertThat(sql2oGenreRepository.findById(16).get()).isEqualTo(genres.get(15));
        assertThat(sql2oGenreRepository.findById(17).get()).isEqualTo(genres.get(16));
        assertThat(sql2oGenreRepository.findById(18).get()).isEqualTo(genres.get(17));
    }

    @Test
    public void whenFindByIdNotExistingThenGetEmptyOptional() {
        assertThat(sql2oGenreRepository.findById(0)).isEqualTo(Optional.empty());
        assertThat(sql2oGenreRepository.findById(19)).isEqualTo(Optional.empty());
    }
}