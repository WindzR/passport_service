package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.controller.KafkaPassportController;
import ru.job4j.domain.MessageDTO;
import ru.job4j.domain.Passport;
import ru.job4j.repository.PassportRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PassportServiceImpl implements PassportService {

    private final static Date TODAY = DateConverter.currentDate();

    private final static Date EXPIRED_PERIOD = DateConverter.threeMonthPeriod();

    private final AtomicInteger id = new AtomicInteger(0);

    private final KafkaPassportController kafkaController;

    private final PassportRepository passportDAO;

    public PassportServiceImpl(final PassportRepository passportDAO,
                               final KafkaPassportController kafkaController) {
        this.passportDAO = passportDAO;
        this.kafkaController = kafkaController;
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

    /**
     * Метод находит в базе данных просроченные паспорта,
     * если список полученных паспортов НЕ пустой, тогда вызывается контроллер,
     * ответственный за отправку извещения в сервис почты
     * @return просроченные паспорта
     */
    @Override
    public List<Passport> findExpiredPassports() {
        List<Passport> passports = passportDAO.findPassportByExpiredDate(TODAY);
        if (!passports.isEmpty()) {
            String report = passportReport(passports);
            MessageDTO messageDTO = MessageDTO.of(id.getAndIncrement(), report);
            System.out.println(messageDTO);
            kafkaController.sendNotification(messageDTO);
        }
        return passports;
    }

    @Override
    public List<Passport> findReplaceablePassports() {
        return passportDAO.findAllByExpiredDateBetween(TODAY, EXPIRED_PERIOD);
    }

    private String passportReport(List<Passport> passports) {
        StringBuilder sb = new StringBuilder();
        sb.append(passports.size())
                .append(" passports are has been unavailable!")
                .append(System.lineSeparator());
        int count = 1;
        for (Passport passport : passports) {
            sb.append(count).append(". ")
                    .append("Passport with series = ")
                    .append(passport.getSeries())
                    .append(", expired ")
                    .append(passport.getExpiredDate())
                    .append(System.lineSeparator());
            count++;
        }
        return sb.toString();
    }
}
