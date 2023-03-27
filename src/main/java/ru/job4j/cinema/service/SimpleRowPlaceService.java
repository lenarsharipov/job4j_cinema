package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.repository.RowPlaceRepository;

import java.util.Collection;

@ThreadSafe
@Service
public class SimpleRowPlaceService implements RowPlaceService {
    private final RowPlaceRepository rowPlaceRepository;

    public SimpleRowPlaceService(RowPlaceRepository rowPlaceRepository) {
        this.rowPlaceRepository = rowPlaceRepository;
    }
    @Override
    public Collection<Integer> findPlaceByHallId(int id) {
        return rowPlaceRepository.findPlaceByHallId(id);
    }

    @Override
    public Collection<Integer> findRowByHallId(int id) {
        return rowPlaceRepository.findRowByHallId(id);
    }
}