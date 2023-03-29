package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.RowPlaceService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpServletRequest;

@ThreadSafe
@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final SessionService sessionService;
    private final RowPlaceService rowPlaceService;
    private final TicketService ticketService;


    public TicketController(SessionService sessionService,
                            RowPlaceService rowPlaceService,
                            TicketService ticketService) {
        this.rowPlaceService = rowPlaceService;
        this.sessionService = sessionService;
        this.ticketService = ticketService;
    }

    @GetMapping("/create/{id}")
    public String getCreationPage(Model model, @PathVariable int id) {
        var sessionOptional = sessionService.findById(id);
        if (sessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("filmSession", sessionOptional.get());
        var hallId = sessionOptional.get().getHallId();
        model.addAttribute("rows", rowPlaceService.findRowByHallId(hallId));
        model.addAttribute("places", rowPlaceService.findPlaceByHallId(hallId));
        return "tickets/create";
    }

    @PostMapping("/create/{id}")
    public String create(Model model, HttpServletRequest request) {
            var user = (User) request.getSession().getAttribute("user");
            var sessionId = Integer.parseInt(request.getParameter("id"));
            var rowNumber = Integer.parseInt(request.getParameter("rowNumber"));
            var placeNumber = Integer.parseInt(request.getParameter("placeNumber"));
            var ticket = new Ticket(0, sessionId, rowNumber, placeNumber, user.getId());
            var ticketOptional = ticketService.save(ticket);
            if (ticketOptional.isEmpty()) {
                var errorMessage = """
                          Не удалось приобрести билет на заданное место. Вероятно оно уже занято.
                          Перейдите на страницу бронирования билетов и попробуйте снова.
                        """;
                model.addAttribute("message", errorMessage);
                return "errors/404";
            }
            var successMessage = "Вы успешно приобрели билет!";
            model.addAttribute("message", successMessage);
            model.addAttribute("place", placeNumber);
            model.addAttribute("row", rowNumber);
            return "tickets/success";
    }
}