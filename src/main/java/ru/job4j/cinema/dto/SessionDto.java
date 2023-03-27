package ru.job4j.cinema.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SessionDto {
    private int id;
    private int filmId;
    private String filmName;
    private int year;
    private int minimalAge;
    private int durationInMinutes;
    private String genre;
    private int fileId;
    private int hallId;
    private String hallName;
    private LocalTime startTime;
    private LocalDate startDate;
    private LocalTime endTime;
    private LocalDate endDate;
    private int price;

    public SessionDto(int id, int filmId, String filmName, int year, int minimalAge,
                      int durationInMinutes, String genre, int fileId, int hallId,
                      String hallName, LocalTime startTime, LocalDate startDate,
                      LocalTime endTime, LocalDate endDate, int price) {
        this.id = id;
        this.filmId = filmId;
        this.filmName = filmName;
        this.year = year;
        this.minimalAge = minimalAge;
        this.durationInMinutes = durationInMinutes;
        this.genre = genre;
        this.fileId = fileId;
        this.hallId = hallId;
        this.hallName = hallName;
        this.startTime = startTime;
        this.startDate = startDate;
        this.endTime = endTime;
        this.endDate = endDate;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}