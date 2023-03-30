package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleFileService implements FileService {
    private final static String DEFAULT_POSTER_PATH = "files/default/p_default.jpg";

    private final FileRepository fileRepository;

    public SimpleFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private void createStorageDirectory(String path) {
        try {
            Files.createDirectories(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FileDto> getFileById(int id) {
        var fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            return Optional.empty();
        }
        byte[] content;
        try {
            content = readFileAsBytes(fileOptional.get().getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(new FileDto(fileOptional.get().getName(), content));
    }

    /**
     * В блоке catch при сценарии, когда изображение не найдено,
     * загружается фото по-умолчанию вместо того, чтобы кидать исключение,
     * которое остановит работу сервера
     */
    private byte[] readFileAsBytes(String path) throws IOException {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            return Files.readAllBytes(Path.of(DEFAULT_POSTER_PATH));
        }
    }

}