package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleSessionService implements SessionService {
    private final SessionRepository sessionRepository;
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;
    private final HallRepository hallRepository;

    public SimpleSessionService(SessionRepository sessionRepository, FilmRepository filmRepository,
                                GenreRepository genreRepository, FileRepository fileRepository,
                                HallRepository hallRepository) {
        this.sessionRepository = sessionRepository;
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
        this.hallRepository = hallRepository;
    }

    private SessionDto createSessionDto(Session session) {
        var filmOptional = filmRepository.findById(session.getFilmId());
        if (filmOptional.isEmpty()) {
            throw new RuntimeException("Фильм не найден");
        }
        var film = filmOptional.get();
        var genreOptional = genreRepository.findById(film.getGenreId());
        if (genreOptional.isEmpty()) {
            throw new RuntimeException("Жанр не найден");
        }
        var genre = genreOptional.get().getName();
        var fileOptional = fileRepository.findById(film.getFileId());
        if (fileOptional.isEmpty()) {
            throw new RuntimeException("Файл не найден");
        }
        var file = fileOptional.get();
        var hallOptional = hallRepository.findById(session.getHallId());
        if (hallOptional.isEmpty()) {
            throw new RuntimeException("Зал не найден");
        }
        var hall = hallOptional.get();
        return new SessionDto(
                session.getId(), session.getFilmId(), film.getName(), film.getYear(),
                film.getMinimalAge(), film.getDurationInMinutes(), genre, file.getId(),
                hall.getId(), hall.getName(), session.getStartTime().toLocalTime(),
                session.getStartTime().toLocalDate(), session.getEndTime().toLocalTime(),
                session.getEndTime().toLocalDate(), session.getPrice()
        );
    }

    @Override
    public Optional<SessionDto> findById(int id) {
        var sessionOptional = sessionRepository.findById(id);
        if (sessionOptional.isEmpty()) {
            return Optional.empty();
        }
        var session = sessionOptional.get();
        return Optional.of(createSessionDto(session));
    }

    @Override
    public Collection<SessionDto> findAll() {
        var sessions = sessionRepository.findAll();
        List<SessionDto> sessionDtoList = new ArrayList<>();
        for (var session : sessions) {
            var sessionDto = createSessionDto(session);
            sessionDtoList.add(sessionDto);
        }
        return sessionDtoList;
    }

}
