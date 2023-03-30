package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void initService() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    /**
     * Тест на метод getRegistrationPage().
     * При обращении возвращает страницу регистрации пользователей.
     */
    @Test
    public void whenRequestUserRegistrationPageThenGetIt() {
        assertThat(userController.getRegistrationPage()).isEqualTo("users/register");
    }

    /**
     * Тест на метод register().
     * При регистрации нового пользователя с уникальной почтой, пользователь сохраняется в БД,
     * клиент переходит на страницу films.
     */
    @Test
    public void whenRequestToRegisterUserThenUserSavedAndGetVacanciesPage() {
        var user = new User(1, "name", "user@email", "password");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(Optional.of(user));

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/films");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Тест на метод register().
     * При регистрации нового пользователя с неуникальной почтой, выбрасывается исключение,
     * клиент переходит на страницу errors/404, где отображается сообщение об ошибке.
     */
    @Test
    public void whenRequestToRegisterUserWithNotUniqueEmailThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Пользователь с такой почтой уже существует");
        when(userService.save(any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.register(model, any());
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Тест на метод getLoginPage().
     * При обращении возвращает страницу логирования.
     */
    @Test
    public void whenRequestLoginPageThenGetIt() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }

    /**
     * Тест на метод loginUser().
     * Клиент обращается к странице /users/login и вводит корректные данные,
     * после чего он входит в систему и его переносит на
     */
    @Test
    public void whenRequestToLoginThenLoggedAndGetVacanciesPage() {
        var user = new User(1, "user1@email", "user1", "password");
        var request = mock(HttpServletRequest.class);
        var session = mock(HttpSession.class);
        when(userService.findByEmailAndPassword("user1@email", "password"))
                .thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, session);
        var actualUser = session.getAttribute("user");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenRequestToLoginAsIllegalUserThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException(
                "Почта или пароль введены неверно"
        );
        var session = mock(HttpSession.class);
        when(userService.findByEmailAndPassword(any(), any()))
                .thenReturn(Optional.empty());

        var model = new ConcurrentModel();
        var view = userController.loginUser(new User(), model, session);
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }


    /**
     * Тест на метод logout().
     * При обращении возвращает страницу логирования.
     */
    @Test
    public void whenRequestToLogoutPageThenGetIt() {
        assertThat(userController.getLoginPage()).isEqualTo("users/login");
    }

}