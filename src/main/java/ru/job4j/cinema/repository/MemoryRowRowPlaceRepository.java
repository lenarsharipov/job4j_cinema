package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.stream.IntStream;

@ThreadSafe
@Repository
public class MemoryRowRowPlaceRepository implements RowPlaceRepository {
    private final HallRepository hallRepository;

    public MemoryRowRowPlaceRepository(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    private Hall findHall(int id) {
        var placeRowOptional = hallRepository.findById(id);
        if (placeRowOptional.isEmpty()) {
            throw new RuntimeException("Количество мест/рядов не найдено");
        }
        return placeRowOptional.get();
    }

    @Override
    public Collection<Integer> findPlaceByHallId(int id) {
        var places = findHall(id).getPlaceCount() + 1;
        return IntStream.range(1, places)
                .boxed()
                .toList();
    }

    @Override
    public Collection<Integer> findRowByHallId(int id) {
        var rows = findHall(id).getRowCount() + 1;
        return IntStream.range(1, rows)
                .boxed()
                .toList();
    }
}