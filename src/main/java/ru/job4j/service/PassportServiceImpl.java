package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.domain.Passport;
import ru.job4j.repository.PassportRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PassportServiceImpl implements PassportService {

    private final static Date TODAY = DateConverter.currentDate();

    private final static Date EXPIRED_PERIOD = DateConverter.threeMonthPeriod();

    private final PassportRepository passportDAO;

    public PassportServiceImpl(final PassportRepository passportDAO) {
        this.passportDAO = passportDAO;
    }

    @Override
    public Optional<Passport> save(Passport passport) {
        passportDAO.save(passport);
        return Optional.of(passport);
    }

    @Override
    public Optional<Passport> updatePassport(int id, Passport passport) {
        var updatePassport = passportDAO.findById(id);
        if (updatePassport.isPresent()) {
            passport.setId(id);
            Passport savePassport = passportDAO.save(passport);
            return Optional.of(savePassport);
        }
        return Optional.empty();
    }

    @Override
    public void delete(int id) {
        Passport passport = new Passport();
        passport.setId(id);
        passportDAO.delete(passport);
    }

    @Override
    public List<Passport> findAll() {
        return passportDAO.findAll();
    }

    @Override
    public Optional<Passport> findPassportBySeries(String series) {
        return passportDAO.findPassportBySeries(series);
    }

    @Override
    public List<Passport> findExpiredPassports() {
        return passportDAO.findPassportByExpiredDate(TODAY);
    }

    @Override
    public List<Passport> findReplaceablePassports() {
        return passportDAO.findAllByExpiredDateBetween(TODAY, EXPIRED_PERIOD);
    }
}
