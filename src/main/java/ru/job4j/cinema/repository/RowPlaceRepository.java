package ru.job4j.cinema.repository;

import java.util.Collection;

public interface RowPlaceRepository {
    Collection<Integer> findPlaceByHallId(int id);
    Collection<Integer> findRowByHallId(int id);
}