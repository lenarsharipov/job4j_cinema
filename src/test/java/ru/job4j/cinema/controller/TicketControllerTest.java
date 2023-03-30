package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.RowPlaceService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {
    private TicketController ticketController;
    private TicketService ticketService;
    private RowPlaceService rowPlaceService;
    private SessionService sessionService;

    @BeforeEach
    public void init() {
        ticketService = mock(TicketService.class);
        rowPlaceService = mock(RowPlaceService.class);
        sessionService = mock(SessionService.class);
        ticketController = new TicketController(sessionService, rowPlaceService, ticketService);
    }

    /**
     * Mock-тест на метод getCreationPage().
     * При успешном нахождении сеанса по id, метод добавляет в модель атрибуты places, rows,
     * filmSession и выдает ссылку на страницу заказа билета на сеанс.
     */
    @Test
    public void whenRequestToSelectFilmSessionThenGetTicketCreationPage() {
        var expectedPlaces = List.of(1, 2, 3);
        var expectedRows = List.of(1, 2, 3, 4, 5);
        var expectedSession = new SessionDto(1, 2, "film", 2023, 16, 120, "genre", 1, 1, "2D",
                LocalTime.of(10, 0, 0), LocalDate.of(2023, 4, 1),
                LocalTime.of(12, 0, 0), LocalDate.of(2023, 4, 1), 250);
        when(sessionService.findById(1)).thenReturn(Optional.of(expectedSession));
        when(rowPlaceService.findPlaceByHallId(1)).thenReturn(expectedPlaces);
        when(rowPlaceService.findRowByHallId(1)).thenReturn(expectedRows);

        var model = new ConcurrentModel();
        var view = ticketController.getCreationPage(model, 1);
        var actualPlaces = model.getAttribute("places");
        var actualRows = model.getAttribute("rows");
        var actualSession = model.getAttribute("filmSession");

        assertThat(view).isEqualTo("tickets/create");
        assertThat(actualSession).isEqualTo(expectedSession);
        assertThat(actualPlaces).isEqualTo(expectedPlaces);
        assertThat(actualRows).isEqualTo(expectedRows);
    }

    /**
     * Mock-тест на метод getCreationPage().
     * При ошибке нахождения сеанса по id, метод выдает ссылку на страницу с ошибкой.
     */
    @Test
    public void whenRequestToSelectFilmSessionWithIllegalIdThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Сеанс с указанным идентификатором не найден");
        when(sessionService.findById(1)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = ticketController.getCreationPage(model, 1);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Mock-тест на метод create().
     * При успешном сохранении билета в БД метод возвращает страницу с сообщением
     * об успешной покупке билета.
     */
    @Test
    public void whenCreateTicketThenGetSuccessPageWithMessage() {
        var expectedMessage = "Вы успешно приобрели билет!";
        var session = mock(HttpSession.class);
        var expectedTicket = new Ticket(0, 2, 5, 3, 5);
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketService.save(ticketArgumentCaptor.capture()))
                .thenReturn(Optional.of(expectedTicket));

        var model = new ConcurrentModel();
        var view = ticketController.create(model, expectedTicket, session);
        var actualMessage = model.getAttribute("message");
        var actualTicket = ticketArgumentCaptor.getValue();

        assertThat(view).isEqualTo("tickets/success");
        assertThat(actualMessage).isEqualTo(expectedMessage);
        assertThat(actualTicket).isEqualTo(expectedTicket);
    }

    /**
     * Mock-тест на метод create().
     * При ошибке сохранения билета в БД метод возвращает страницу с сообщением
     * об ошибке.
     */
    @Test
    public void whenCreateTicketForOccupiedPlaceThenGetErrorPageWithMessage() {
        var expectedMessage = """
                          Не удалось приобрести билет на заданное место. Вероятно оно уже занято.
                          Перейдите на страницу бронирования билетов и попробуйте снова.
                        """;
        var expectedException = new RuntimeException(expectedMessage);
        var session = mock(HttpSession.class);
        var ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);

        when(ticketService.save(ticketArgumentCaptor.capture()))
                .thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = ticketController.create(model, ticketArgumentCaptor.capture(), session);
        var actualMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}