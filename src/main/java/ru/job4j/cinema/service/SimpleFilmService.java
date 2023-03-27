package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleFilmService implements FilmService {
    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;

    public SimpleFilmService(FilmRepository filmRepository,
                             GenreRepository genreRepository,
                             FileRepository fileRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
    }

    private FilmDto createDto(Film film) {
        var genreOptional = genreRepository.findById(film.getGenreId());
        var fileIdOptional = fileRepository.findById(film.getFileId());
        if (genreOptional.isEmpty() || fileIdOptional.isEmpty()) {
            throw new RuntimeException("Жанр или файл не найдены");
        }
        var genre = genreOptional.get().getName();
        var fileId = fileIdOptional.get().getId();
        return new FilmDto(
                        film.getId(),
                        film.getName(),
                        film.getDescription(),
                        film.getYear(),
                        film.getMinimalAge(),
                        film.getDurationInMinutes(),
                        genre,
                        fileId
        );
    }

    @Override
    public Collection<FilmDto> findAll() {
        List<FilmDto> dtoFilms = new ArrayList<>();
        var films = filmRepository.findAll();
        for (var film : films) {
            dtoFilms.add(createDto(film));
        }
        return dtoFilms;
    }

    @Override
    public Optional<FilmDto> findById(int id) {
        var filmOptional = filmRepository.findById(id);
        if (filmOptional.isEmpty()) {
            return Optional.empty();
        }
        var film = filmOptional.get();
        return Optional.of(createDto(film));
    }
}
