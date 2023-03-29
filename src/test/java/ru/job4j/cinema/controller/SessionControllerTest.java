package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.service.SessionService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SessionControllerTest {
    private SessionService sessionService;
    private SessionController sessionController;

    @BeforeEach
    public void initService() {
        sessionService = mock(SessionService.class);
        sessionController = new SessionController(sessionService);
    }

    /**
     * Mock-тест на метод getAll().
     * Метод выводит список сеансов сохраненных в базу данных на странице /sessions
     */
    @Test
    public void whenRequestSessionListThenGetPageWithSessions() {
        var session1 = new SessionDto(
                1, 1, "film1", 2023, 16, 120,
                "genre1", 1, 1, "2D",
                LocalTime.of(10, 20, 0), LocalDate.of(2023, 4, 1),
                LocalTime.of(12, 20, 0), LocalDate.of(2023, 4, 1), 250);
        var session2 = new SessionDto(
                2, 2, "film2", 2023, 18, 180,
                "genre2", 2, 2, "3D",
                LocalTime.of(11, 0, 0), LocalDate.of(2023, 4, 1),
                LocalTime.of(14, 0, 0), LocalDate.of(2023, 4, 1), 350);
        var expectedSession = List.of(session1, session2);
        when(sessionService.findAll()).thenReturn(expectedSession);

        var model = new ConcurrentModel();
        var view = sessionController.getAll(model);
        var actualSessions = model.getAttribute("sessions");

        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualSessions).isEqualTo(expectedSession);
    }

    /**
     * Mock-тест на метод findById().
     * Выводит найденный по id сеанс на страницу сеанса - sessions/one.
     */
    @Test
    public void whenRequestSessionByIdThenGetSessionPage() {
        var session1 = new SessionDto(
                1, 1, "film1", 2023, 16, 120,
                "genre1", 1, 1, "2D",
                LocalTime.of(10, 20, 0), LocalDate.of(2023, 4, 1),
                LocalTime.of(12, 20, 0), LocalDate.of(2023, 4, 1), 250);
        var expectedSession = Optional.of(session1);
        when(sessionService.findById(1)).thenReturn(expectedSession);

        var model = new ConcurrentModel();
        var view = sessionController.findById(model, session1.getId());
        var actualSession = model.getAttribute("filmSession");

        assertThat(view).isEqualTo("sessions/one");
        assertThat(actualSession).isEqualTo(expectedSession.get());
    }

    /**
     * Mock-тест на метод findById().
     * Выводит страницу с ошибкой, при сценарии, когда сеанс не найден по id.
     */
    @Test
    public void whenRequestNonExistingSessionByIdThenGetMessageAnd404Page() {
        var expectedException = new RuntimeException("Сеанс с указанным идентификатором не найден");
        when(sessionService.findById(0)).thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = sessionController.findById(model, 0);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

}