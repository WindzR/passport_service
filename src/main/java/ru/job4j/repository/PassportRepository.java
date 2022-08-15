package ru.job4j.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.domain.Passport;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PassportRepository extends CrudRepository<Passport, Integer> {

    List<Passport> findAll();

    Optional<Passport> findPassportBySeries(String series);

    @Query("select p from Passport p where p.expiredDate < :todayDate")
    List<Passport> findPassportByExpiredDate(@Param("todayDate") Date today);

    List<Passport> findAllByExpiredDateBetween(Date today, Date expirationPeriod);
}
