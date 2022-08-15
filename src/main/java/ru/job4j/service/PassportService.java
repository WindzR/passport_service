package ru.job4j.service;

import ru.job4j.domain.Passport;

import java.util.List;
import java.util.Optional;

public interface PassportService {

    Optional<Passport> save(Passport passport);

    Optional<Passport> updatePassport(int id, Passport passport);

    void delete(int id);

    List<Passport> findAll();

    Optional<Passport> findPassportBySeries(String series);

    List<Passport> findExpiredPassports();

    List<Passport> findReplaceablePassports();
}
