package ru.job4j.service;

import java.util.Calendar;
import java.util.Date;

public final class DateConverter {

    public static final Date currentDate() {
        return new Date();
    }

    public static final Date threeMonthPeriod() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 3);
        return calendar.getTime();
    }
}
