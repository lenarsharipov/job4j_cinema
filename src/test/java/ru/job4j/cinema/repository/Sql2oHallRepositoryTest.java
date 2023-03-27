package ru.job4j.cinema.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oHallRepositoryTest {
    private static Sql2oHallRepository sql2oHallRepository;
    private final List<Hall> halls = List.of(
            new Hall(1, "2D", 12, 19,
                    """
                    Стандартный кинозал с возможностью демонстрации фильмов в 2D формате.
                    """),
            new Hall(2, "3D", 11, 13,
                    """
                    Стандартный кинозал с возможностью демонстрации фильмов в 3D формате.
                    """),
            new Hall(3, "VIP", 5, 8,
                    """
                    Премиальный кинозал с возможностью демонстрации фильмов в 2D и 3D
                    форматах. Отличительная особенность - камерная атмосфера,
                    суперкомфортные кресла-реклайнеры с возможностью регулировки и
                    фиксации положения спинки, подножки и сидения.
                    """),
            new Hall(4, "2D", 9, 13,
                    """
                    Стандартный кинозал с возможностью демонстрации фильмов в 2D формате.
                    """),
            new Hall(5, "2D", 9, 13,
                    """
                    Стандартный кинозал с возможностью демонстрации фильмов в 2D формате.
                    """),
            new Hall(6, "2D", 9, 13,
                    """
                    Стандартный кинозал с возможностью демонстрации фильмов в 2D формате.
                    """),
            new Hall(7, "2D", 9, 13,
                    """
                    Стандартный кинозал с возможностью демонстрации фильмов в 2D формате.
                    """)
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

        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @Test
    public void whenFindAllThenListsEqual() {
        assertThat(sql2oHallRepository.findAll()).isEqualTo(halls);
    }

    @Test
    public void whenFindByIdThenFound() {
        assertThat(sql2oHallRepository.findById(1).get()).isEqualTo(halls.get(0));
        assertThat(sql2oHallRepository.findById(2).get()).isEqualTo(halls.get(1));
        assertThat(sql2oHallRepository.findById(3).get()).isEqualTo(halls.get(2));
        assertThat(sql2oHallRepository.findById(4).get()).isEqualTo(halls.get(3));
        assertThat(sql2oHallRepository.findById(5).get()).isEqualTo(halls.get(4));
        assertThat(sql2oHallRepository.findById(6).get()).isEqualTo(halls.get(5));
        assertThat(sql2oHallRepository.findById(7).get()).isEqualTo(halls.get(6));
    }

    @Test
    public void whenFindByIdNotExistingHallThenGetEmptyOptional() {
        assertThat(sql2oHallRepository.findById(0)).isEqualTo(Optional.empty());
        assertThat(sql2oHallRepository.findById(8)).isEqualTo(Optional.empty());
    }

}