package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Sql2oTicketRepositoryTest {
    private static Sql2oTicketRepository sql2oTicketRepository;

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

        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
    }

    @AfterEach
    public void clearVacancies() {
        var tickets = sql2oTicketRepository.findAll();
        for (var ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenSaveThenGetItOptional() {
        var ticket = new Ticket(0, 1, 1, 1, 1);
        var ticketOptional = sql2oTicketRepository.save(ticket);
        assertThat(ticketOptional).isNotEmpty();
        var id = ticketOptional.get().getId();
        ticket.setId(id);
        assertThat(ticketOptional.get()).isEqualTo(ticket);
    }

    @Test
    public void whenSaveTicketThenSecondWithSameRowPlaceThenException() {
        var ticket1 = new Ticket(0, 1, 1, 1, 1);
        var ticket2 = new Ticket(0, 1, 1, 1, 2);
        sql2oTicketRepository.save(ticket1);
        var errorMessage = """
                          Не удалось приобрести билет на заданное место. Вероятно оно уже занято.
                          Перейдите на страницу бронирования билетов и попробуйте снова.
                        """;
        assertThatThrownBy(
                () -> sql2oTicketRepository.save(ticket2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(errorMessage);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var ticket1 = sql2oTicketRepository.save(new Ticket(0, 1, 1, 1, 1)).get();
        var ticket2 = sql2oTicketRepository.save(new Ticket(0, 2, 1, 1, 2)).get();
        var ticket3 = sql2oTicketRepository.save(new Ticket(0, 3, 1, 1, 3)).get();
        var result = sql2oTicketRepository.findAll();
        assertThat(result).isEqualTo(List.of(ticket1, ticket2, ticket3));
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oTicketRepository.deleteById(0)).isFalse();
    }

}