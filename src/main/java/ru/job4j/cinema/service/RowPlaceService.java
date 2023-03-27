package ru.job4j.cinema.service;

import java.util.Collection;

public interface RowPlaceService {
    Collection<Integer> findPlaceByHallId(int id);
    Collection<Integer> findRowByHallId(int id);
}