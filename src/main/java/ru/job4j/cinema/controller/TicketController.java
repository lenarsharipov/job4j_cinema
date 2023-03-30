package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.RowPlaceService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;

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
    public String create(Model model, @ModelAttribute Ticket ticket, HttpSession session) {
        var user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        try {
            ticketService.save(ticket);
            var successMessage = "Вы успешно приобрели билет!";
            model.addAttribute("message", successMessage);
            model.addAttribute("place", ticket.getPlaceNumber());
            model.addAttribute("row", ticket.getRowNumber());
            return "tickets/success";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }
}