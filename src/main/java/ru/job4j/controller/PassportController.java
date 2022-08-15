package ru.job4j.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;
import ru.job4j.domain.Passport;
import ru.job4j.service.PassportService;
import ru.job4j.service.PassportServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/passports/")
public class PassportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            PassportController.class.getSimpleName()
    );

    private PassportService passportService;

    public PassportController(final PassportServiceImpl passportService) {
        this.passportService = passportService;
    }

    /**
     * Сохранение нового паспорта
     * @param passport регистрируемый паспорт
     * @return зарегистрированный паспорт
     */
    @PostMapping("save")
    public ResponseEntity<Passport> savePassport(@RequestBody Passport passport) {
        return new ResponseEntity<Passport>(
                passportService.save(passport).orElse(new Passport()),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновление существующего паспорта
     * @param id - идентификатор паспорта в БД
     * @param passport - новые данные паспорта
     * @return обновленный паспорт
     */
    @PutMapping("update")
    public ResponseEntity<Passport> updatePassport(@RequestParam("id") String id,
                                                   @RequestBody Passport passport) {
        return new ResponseEntity<Passport>(
                passportService.updatePassport(
                        Integer.parseInt(id), passport).orElse(new Passport()
                ),
                HttpStatus.OK
        );
    }

    /**
     * Удаление паспотра по id
     * @param id - идентификатор в базе удаляемого пасспорта
     * @return сообщение об удалении
     */
    @DeleteMapping("delete")
    public ResponseEntity<Map<String, String>> deletePassport(@RequestParam("id") String id) {
        passportService.delete(Integer.parseInt(id));
        String message = "Passport with id = " + id + " was deleted!";
        Map<String, String> body = new HashMap<>() {{
            put("DELETED", message);
        }};
        return new ResponseEntity<>(
                body,
                HttpStatus.OK
        );
    }

    /**
     * Возвращает
     * @return список всех паспартов
     */
    @GetMapping("find")
    public List<Passport> findAllPassports() {
        return passportService.findAll();
    }

    /**
     * Находит паспорт по заданной серии
     * @param series серия
     * @return найденный паспорт
     */
    @GetMapping("find-series")
    public ResponseEntity<Passport> findPassportBySeries(@RequestParam("seria") String series) {
        Optional<Passport> passport = passportService.findPassportBySeries(series);
        if (passport.isPresent()) {
            return new ResponseEntity<Passport>(
                    passport.get(),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<Passport>(
            new Passport(),
            new MultiValueMapAdapter<String, String>(
                Map.of("NOT FOUND", List.of(
                "Passport is not found. Please, check require series = " + series)
            )),
            HttpStatus.NOT_FOUND
        );
    }

    /**
     * Находит список паспортов с истекшим сроком
     * @return список паспортов
     */
    @GetMapping("unavailable")
    public List<Passport> findUnavailablePassports() {
        return passportService.findExpiredPassports();
    }

    /**
     * Находит список паспортов со сроком истечения в ближайшие 3 месяца
     * @return список паспортов
     */
    @GetMapping("find-replaceable")
    public List<Passport> findExpirationDatePassports() {
        return passportService.findReplaceablePassports();
    }
}
